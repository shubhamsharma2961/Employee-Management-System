package com.company.ems.role;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleDto> getAllRoles() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    public RoleDto getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role configuration not found with ID: " + id));
        return roleMapper.toDto(role);
    }
    
    public RoleDto createRole(CreateRoleDto dto) {
        roleRepository.findByName(dto.getName())
                .ifPresent(ignore -> {throw new RuntimeException("Role already exists with name: " + dto.getName());});
        Role role = roleMapper.toEntity(dto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    public RoleDto updateRole(UUID id, EditRoleDto dto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
        roleMapper.updateEntityFromDto(dto, existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toDto(updatedRole);
    }
    
    public void deleteRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
        if (!role.getRoleMenus().isEmpty()) {
            throw new RuntimeException("Cannot delete role assigned to menus. Unassign first.");
        }
        role.setDeleted(true);
        roleRepository.save(role);
    }

}
