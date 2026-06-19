package com.company.ems.auth;

import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.ems.common.MenuType;
import com.company.ems.common.UserStatus;
import com.company.ems.employee.Employee;
import com.company.ems.menu.Menu;
import com.company.ems.menu.MenuDto;
import com.company.ems.security.jwt.DataScopeService;
import com.company.ems.security.jwt.JwtService;
import com.company.ems.security.jwt.UserPrincipal;
import com.company.ems.user.User;
import com.company.ems.user.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final DataScopeService dataScopeService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder,
                           DataScopeService dataScopeService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.dataScopeService = dataScopeService;
    }

    @Override
    public JwtDto login(LoginDto request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmailAndIsDeletedFalse(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));                
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("Your account is deactivated. Please contact administration.");
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(userPrincipal);
        return new JwtDto(token, "Bearer", user.getIsPasswordChanged());
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordDto request) {

        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password does not match");
        }
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new RuntimeException("New passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setPasswordChanged(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserMeResponseDto getCurrentUserProfile(String email) {
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User context not found"));
        
        if (user.getEmployee() != null) {
            Employee emp = initializeAndUnproxy(user.getEmployee());
            user.setEmployee(emp); 
            Optional.ofNullable(emp.getDepartment()).ifPresent(d -> emp.setDepartment(initializeAndUnproxy(d)));
            Optional.ofNullable(emp.getCompany()).ifPresent(c -> emp.setCompany(initializeAndUnproxy(c)));
            Optional.ofNullable(emp.getDesignation()).ifPresent(d -> emp.setDesignation(initializeAndUnproxy(d)));
        }
                
        UserMeResponseDto dto = new UserMeResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setPasswordChanged(user.getIsPasswordChanged());

        List<String> roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .distinct()
                .toList();
        dto.setRoles(roles);

        Set<Menu> assignedMenus = user.getUserRoles().stream()
                .flatMap(ur -> ur.getRole().getRoleMenus().stream())
                .map(rm -> rm.getMenu()) 
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        System.out.println("MENU BUILDING HIT | USER: " + email + " | COUNT: " + assignedMenus.size());

        Set<Menu> completeMenuSet = new HashSet<>();
        for (Menu menu : assignedMenus) {
            initializeAndUnproxy(menu);
            
            if (menu.getMenuType() == MenuType.PAGE) {
                completeMenuSet.add(menu);
            } else if (menu.getMenuType() == MenuType.ACTION || menu.getMenuType() == MenuType.LOOKUP) {
                completeMenuSet.add(menu); 
                
                Menu parent = menu.getParent();
                while (parent != null) {
                    initializeAndUnproxy(parent);
                    if (parent.getMenuType() == MenuType.PAGE) {
                        completeMenuSet.add(parent);
                        break;
                    }
                    parent = parent.getParent();
                }
            }
        }

        Map<UUID, MenuDto> map = new HashMap<>();
        for (Menu m : completeMenuSet) {
            initializeAndUnproxy(m);
            MenuDto dtoMenu = new MenuDto();
            dtoMenu.setId(m.getId());
            dtoMenu.setName(m.getName());
            dtoMenu.setUrl(m.getUrl());
            dtoMenu.setIcon(m.getIcon());
            dtoMenu.setPermissionKey(m.getPermissionKey());
            dtoMenu.setSortOrder(m.getSortOrder());
            dtoMenu.setMenuType(m.getMenuType());
            dtoMenu.setParentId(m.getParent() != null ? m.getParent().getId() : null);
            dtoMenu.setChildren(new ArrayList<>());
            map.put(m.getId(), dtoMenu);
        }

        List<MenuDto> rootMenus = new ArrayList<>();
        for (Menu m : completeMenuSet) {
            MenuDto currentDto = map.get(m.getId());

            if (m.getParent() == null) {
                rootMenus.add(currentDto);
            } else {
                UUID parentId = m.getParent().getId();
                MenuDto parentDto = map.get(parentId);
                if (parentDto != null) {
                    if (!parentDto.getChildren().contains(currentDto)) {
                        parentDto.getChildren().add(currentDto);
                    }
                } else {
                    if (!rootMenus.contains(currentDto) && m.getMenuType() == MenuType.PAGE) {
                        rootMenus.add(currentDto);
                    }
                }
            }
        }

        Comparator<MenuDto> sortComparator = Comparator.comparing(
                MenuDto::getSortOrder, 
                Comparator.nullsLast(Integer::compareTo)
        );

        for (MenuDto m : map.values()) {
            if (m.getChildren() != null) {
                m.getChildren().sort(sortComparator);
            }
        }
        rootMenus.sort(sortComparator);
        dto.setMenus(rootMenus);

        List<String> permissions = assignedMenus.stream()
                .map(m -> m.getPermissionKey())
                .filter(p -> p != null && !p.isBlank())
                .distinct()
                .toList();
        dto.setPermissions(permissions);            

        if (this.dataScopeService != null) {
            dto.setDataScope(this.dataScopeService.getCurrentUserScope().name());
        } else {
            dto.setDataScope("SELF"); 
        }

        if (user.getEmployee() != null && user.getEmployee().getDepartment() != null) {
            dto.setDepartmentId(user.getEmployee().getDepartment().getId());
        } else {
            dto.setDepartmentId(null);
        }

        if (user.getEmployee() != null && user.getEmployee().getCompany() != null) {
            dto.setCompanyId(user.getEmployee().getCompany().getId());
            dto.setCompanyName(user.getEmployee().getCompany().getCompanyName());
        }
        return dto;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public UserMeResponseDto getCurrentUserProfile(String email) {
//
//        User user = userRepository.findByEmailWithRoles(email)
//                .orElseThrow(() -> new RuntimeException("User context not found"));
//        
//        if (user.getEmployee() != null) {
//            initializeAndUnproxy(user.getEmployee());
//            if (user.getEmployee().getDepartment() != null) {
//                initializeAndUnproxy(user.getEmployee().getDepartment());
//            }
//        }
//                
//        UserMeResponseDto dto = new UserMeResponseDto();
//        dto.setId(user.getId());
//        dto.setEmail(user.getEmail());
//        dto.setStatus(user.getStatus());
//        dto.setPasswordChanged(user.getIsPasswordChanged());
//
//        List<String> roles = user.getUserRoles()
//                .stream()
//                .map(ur -> ur.getRole().getName())
//                .distinct()
//                .toList();
//        dto.setRoles(roles);
//
//        Set<Menu> menuSet = user.getUserRoles()
//                .stream()
//                .flatMap(userRole -> userRole.getRole().getRoleMenus().stream())
//                .map(roleMenu -> roleMenu.getMenu())
//                .collect(Collectors.toSet());
//
//        System.out.println("MENU BUILDING HIT");
//        System.out.println("USER: " + email);
//        System.out.println("MENU COUNT: " + menuSet.size());
//
//        Set<Menu> completeMenuSet = new HashSet<>();
//        for (Menu menu : menuSet) {
//            if (menu.getMenuType() == MenuType.PAGE) {
//                completeMenuSet.add(menu);
//            }
//            if (menu.getMenuType() == MenuType.ACTION || menu.getMenuType() == MenuType.LOOKUP) {
//                completeMenuSet.add(menu); 
//                
//                Menu parent = menu.getParent();
//                while (parent != null) {
//                    if (parent.getId() != null) { 
//                        parent.getName(); 
//                    }
//                    
//                    if (parent.getMenuType() == MenuType.PAGE) {
//                        completeMenuSet.add(parent);
//                        break;
//                    }
//                    parent = parent.getParent();
//                }
//            }
//        }
//
//        Map<UUID, MenuDto> map = new HashMap<>();
//        for (Menu menu : completeMenuSet) {
//            MenuDto dtoMenu = new MenuDto();
//            dtoMenu.setId(menu.getId());
//            dtoMenu.setName(menu.getName());
//            dtoMenu.setUrl(menu.getUrl());
//            dtoMenu.setIcon(menu.getIcon());
//            dtoMenu.setPermissionKey(menu.getPermissionKey());
//            dtoMenu.setSortOrder(menu.getSortOrder());
//            dtoMenu.setMenuType(menu.getMenuType());
//            dtoMenu.setParentId(menu.getParent() != null ? menu.getParent().getId() : null);
//            dtoMenu.setChildren(new ArrayList<>());
//            map.put(menu.getId(), dtoMenu);
//        }
//
//        List<MenuDto> rootMenus = new ArrayList<>();
//        for (Menu menu : completeMenuSet) {
//            MenuDto current = map.get(menu.getId());
//
//            if (menu.getParent() == null) {
//                rootMenus.add(current);
//            } else {
//                Menu parentMenu = menu.getParent();
//                if (parentMenu != null) {
//                    MenuDto parentDto = map.get(parentMenu.getId());
//                    if (parentDto != null) {
//                        parentDto.getChildren().add(current);
//                    } else {
//                        if (!rootMenus.contains(current) && menu.getMenuType() == MenuType.PAGE) {
//                            rootMenus.add(current);
//                        }
//                    }
//                }
//            }
//        }
//
//        for (MenuDto m : map.values()) {
//            if (m.getChildren() != null) {
//                m.getChildren().sort(Comparator.comparing(
//                        MenuDto::getSortOrder,
//                        Comparator.nullsLast(Integer::compareTo)
//                ));
//            }
//        }
//        rootMenus.sort(Comparator.comparing(
//                MenuDto::getSortOrder,
//                Comparator.nullsLast(Integer::compareTo)
//            ));
//            dto.setMenus(rootMenus);
//            List<String> permissions = user.getUserRoles()
//                    .stream()
//                    .flatMap(ur -> ur.getRole().getRoleMenus().stream())
//                    .map(rm -> rm.getMenu().getPermissionKey())
//                    .filter(Objects::nonNull)
//                    .filter(p -> !p.isBlank())
//                    .distinct()
//                    .toList();
//            dto.setPermissions(permissions);            
//
//            
//            if (this.dataScopeService != null) {
//                dto.setDataScope(this.dataScopeService.getCurrentUserScope().name());
//            } else {
//                dto.setDataScope("SELF"); 
//            }
//            if (user.getEmployee() != null && user.getEmployee().getDepartment() != null) {
//                dto.setDepartmentId(user.getEmployee().getDepartment().getId());
//            } else {
//                dto.setDepartmentId(null);
//            }
//            return dto;
//        }
    
    @SuppressWarnings("unchecked")
    public static <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            return null;
        }
        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            return (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }
}