package com.company.ems.rolemenu;

import com.company.ems.common.BaseEntity;
import com.company.ems.menu.Menu;
import com.company.ems.role.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "role_menus", uniqueConstraints = {@UniqueConstraint(name = "uk_role_menu", columnNames = {"role_id", "menu_id"} )})
public class RoleMenu extends BaseEntity{
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    public RoleMenu() {}

    public RoleMenu(Role role, Menu menu) {
        this.role = role;
        this.menu = menu;
    }

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
    
}
