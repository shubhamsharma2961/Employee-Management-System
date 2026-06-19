package com.company.ems.userrole;

import com.company.ems.user.User;
import com.company.ems.user.UserRepository;
import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.role.Role;
import com.company.ems.role.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, 
                               UserRepository userRepository, 
                               RoleRepository roleRepository, 
                               UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    @Transactional
    public List<UserRoleDto> assignUserRole(AssignUserRoleDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
        List<UserRole> userRolesToSave = dto.getRoleIds().stream()
                .map(roleId -> {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
                    return new UserRole(user, role);
                })
                .collect(Collectors.toList());
        List<UserRole> savedUserRoles = userRoleRepository.saveAll(userRolesToSave);
        return userRoleMapper.toDtoList(savedUserRoles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleDto> getRolesByUserId(UUID userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoleMapper.toDtoList(userRoles);
    }
    

    @Override
    @Transactional
    public List<UserRoleDto> replaceUserRoles(AssignUserRoleDto dto) {
        UUID userId = dto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Set<UUID> requestedRoleIds =
                dto.getRoleIds() == null ? new HashSet<>() : new HashSet<>(dto.getRoleIds());
        List<UserRole> existingAssignments = userRoleRepository.findByUserId(userId);
        Set<UUID> existingRoleIds = existingAssignments.stream()
                .map(ur -> ur.getRole().getId())
                .collect(Collectors.toSet());
        List<UserRole> toDelete = existingAssignments.stream()
                .filter(ur -> !requestedRoleIds.contains(ur.getRole().getId()))
                .toList();
        if (!toDelete.isEmpty()) {
            userRoleRepository.deleteAllInBatch(toDelete);
        }
        Set<UUID> toInsertIds = requestedRoleIds.stream()
                .filter(id -> !existingRoleIds.contains(id))
                .collect(Collectors.toSet());
        if (!toInsertIds.isEmpty()) {
            List<Role> roles = roleRepository.findAllById(toInsertIds);
            if (roles.size() != toInsertIds.size()) {
                throw new ResourceNotFoundException("One or more role IDs are invalid.");
            }
            List<UserRole> newAssignments = roles.stream().map(role -> {
                UserRole ur = new UserRole();
                ur.setUser(user);
                ur.setRole(role);
                return ur;
            }).toList();
            userRoleRepository.saveAll(newAssignments);
        }
        List<UserRole> finalState = userRoleRepository.findByUserId(userId);
        return userRoleMapper.toDtoList(finalState);
    }
}
