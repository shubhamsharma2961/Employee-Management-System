package com.company.ems.user;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.ems.common.UserStatus;
import com.company.ems.role.Role;
import com.company.ems.role.RoleRepository;
import com.company.ems.userrole.UserRole;
import com.company.ems.userrole.UserRoleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.userRoleRepository = userRoleRepository;
		this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, EditUserDto dto) {
        String email = dto.getEmail();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));       
        if (!user.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email " + email + " is already taken by another account.");
        }
        user.setEmail(email);
        user.setStatus(dto.getStatus());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll(); 
        return userMapper.toDtoList(users);
    }
    
    @Override
    @Transactional
    public List<UserDto> searchByEmail(String email) {
        List<User> users = userRepository.findByEmailContainingIgnoreCase(email);
        return userMapper.toDtoList(users);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setDeleted(true); 
        userRepository.save(user);
    }
    
    @Override
    @Transactional
    public UserDto createUser(CreateUserDto dto) {
        String email = dto.getEmail();
        List<UUID> roleIds = dto.getRoleIds();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists.");
        }
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPasswordChanged(false);
        user.setStatus(UserStatus.LOCKED); 
        User savedUser = userRepository.save(user);
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.isEmpty()) {
            throw new RuntimeException("No valid roles found for the provided IDs.");
        }
        List<UserRole> userRoles = roles.stream()
                .map(role -> new UserRole(savedUser, role))
                .collect(Collectors.toList());
        userRoleRepository.saveAll(userRoles);
        savedUser.setUserRoles(userRoles);
        return userMapper.toDto(savedUser);
    }
}
