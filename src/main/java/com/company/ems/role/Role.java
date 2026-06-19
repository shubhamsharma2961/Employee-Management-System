package com.company.ems.role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.company.ems.common.BaseEntity;
import com.company.ems.common.DataScope;
import com.company.ems.rolemenu.RoleMenu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity{
	
    @Column(nullable = false, unique = true)
    private String name;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<RoleMenu> roleMenus = new ArrayList<>();
	
    private String description;
    
    @Enumerated(EnumType.STRING)
    private DataScope dataScope;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_hierarchy",
        joinColumns = @JoinColumn(name = "parent_role_id"),
        inverseJoinColumns = @JoinColumn(name = "child_role_id"))
    private Set<Role> childRoles = new HashSet<>();
    
    @ManyToMany(mappedBy = "childRoles", fetch = FetchType.LAZY)
    private Set<Role> parentRoles = new HashSet<>();

    public DataScope getDataScope() {
		return dataScope;
	}

	public void setDataScope(DataScope dataScope) {
		this.dataScope = dataScope;
	}

	public Role() {}

    public Role(String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<RoleMenu> getRoleMenus() {
		return roleMenus;
	}

	public void setRoleMenus(List<RoleMenu> roleMenus) {
		this.roleMenus = roleMenus;
	}
	
}
