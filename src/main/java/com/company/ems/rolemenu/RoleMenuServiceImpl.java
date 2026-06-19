package com.company.ems.rolemenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.menu.Menu;
import com.company.ems.menu.MenuGroupDto;
import com.company.ems.menu.MenuMapper;
import com.company.ems.menu.MenuPermissionDto;
import com.company.ems.menu.MenuRepository;
import com.company.ems.role.Role;
import com.company.ems.role.RoleRepository;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final RoleMenuRepository roleMenuRepository;
    private final RoleMenuMapper roleMenuMapper;
    private final MenuMapper menuMapper;

    public RoleMenuServiceImpl(RoleRepository roleRepository, MenuRepository menuRepository, 
                               RoleMenuRepository roleMenuRepository, RoleMenuMapper roleMenuMapper, MenuMapper menuMapper) {
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
        this.roleMenuRepository = roleMenuRepository;
        this.roleMenuMapper = roleMenuMapper;
        this.menuMapper = menuMapper;
    }

    @Override
    @Transactional
    public List<RoleMenuDto> assignRoleMenu(AssignRoleMenuDto dto) {
        UUID targetRoleId = dto.getRoleId();
        List<UUID> targetMenuIds = dto.getMenuIds();
        Role role = roleRepository.findById(targetRoleId)
                .orElseThrow(() -> new RuntimeException("Role configuration not found with ID: " + targetRoleId));
        List<RoleMenu> existingAssociations = roleMenuRepository.findByRoleId(targetRoleId);
        if (!existingAssociations.isEmpty()) {
            throw new RuntimeException("Menus are already assigned to this role. Please use the PUT endpoint to update them.");
        }
        List<Menu> menus = menuRepository.findAllById(targetMenuIds);
        if (menus.isEmpty()) {
            throw new RuntimeException("None of the provided Menu IDs could be found");
        }
        List<RoleMenu> associations = menus.stream()
                .map(menu -> new RoleMenu(role, menu))
                .collect(Collectors.toList());
        List<RoleMenu> savedAssociations = roleMenuRepository.saveAll(associations);
        return roleMenuMapper.toDtoList(savedAssociations);
    }
    
    @Override
    @Transactional
    public List<RoleMenuDto> updateRoleMenu(AssignRoleMenuDto dto) {
        UUID roleId = dto.getRoleId();
        Set<UUID> requestedMenuIds = dto.getMenuIds() == null
                ? Collections.emptySet()
                : new HashSet<>(dto.getMenuIds());

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        List<RoleMenu> existingAssociations = roleMenuRepository.findByRoleId(roleId);
        Set<UUID> existingMenuIds = existingAssociations.stream()
                .map(roleMenu -> roleMenu.getMenu().getId())
                .collect(Collectors.toSet());
        List<RoleMenu> associationsToDelete = existingAssociations.stream()
                .filter(roleMenu -> !requestedMenuIds.contains(roleMenu.getMenu().getId()))
                .toList();
        if (!associationsToDelete.isEmpty()) {
            roleMenuRepository.deleteAllInBatch(associationsToDelete);
        }
        Set<UUID> menuIdsToInsert = requestedMenuIds.stream()
                .filter(menuId -> !existingMenuIds.contains(menuId))
                .collect(Collectors.toSet());
        if (!menuIdsToInsert.isEmpty()) {
            List<Menu> menusToAssign = menuRepository.findAllById(menuIdsToInsert);
            if (menusToAssign.size() != menuIdsToInsert.size()) {
                throw new ResourceNotFoundException("One or more menu IDs are invalid.");
            }
            List<RoleMenu> newAssociations = menusToAssign.stream()
                    .map(menu -> {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setRole(role);
                        roleMenu.setMenu(menu);
                        return roleMenu;
                    })
                    .toList();
            roleMenuRepository.saveAll(newAssociations);
        }
        List<RoleMenu> finalAssociations = roleMenuRepository.findByRoleId(roleId);
        return roleMenuMapper.toDtoList(finalAssociations);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleMenuDto> getMenusByRoleId(UUID roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found: " + roleId);
        }
        List<RoleMenu> associations = roleMenuRepository.findByRoleId(roleId);
        if (associations.isEmpty()) {
            return List.of();
        }
        Role role = associations.get(0).getRole();
        RoleMenuDto dto = new RoleMenuDto();
        dto.setRoleId(role.getId());
        dto.setRoleName(role.getName());
        dto.setAssignedMenus(
            associations.stream()
                .map(RoleMenu::getMenu)
                .distinct()
                .map(menuMapper::toDto)
                .toList()
        );
        return List.of(dto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MenuGroupDto> getRoleMenuGrouped(UUID roleId) {
        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
        Map<String, List<MenuPermissionDto>> grouped = new LinkedHashMap<>();
        for (RoleMenu rm : roleMenus) {
            String menuName = rm.getMenu().getName();
            grouped
                .computeIfAbsent(menuName, ignored -> new ArrayList<>())
                .add(new MenuPermissionDto(rm.getMenu().getPermissionKey()));
        }
        return grouped.entrySet().stream()
            .map(e -> {
                MenuGroupDto dto = new MenuGroupDto();
                dto.setMenuName(e.getKey());
                dto.setPermissions(e.getValue());
                return dto;
            })
            .toList();
    }
}