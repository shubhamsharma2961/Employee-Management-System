package com.company.ems.config;

import com.company.ems.menu.Menu;
import com.company.ems.menu.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuSeeder {

    private final MenuRepository menuRepository;

    public MenuSeeder(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void seedAllMenus() {
        seedUserManagement(10);
        seedRoleManagement(20);
        seedMenuManagement(30);
        seedUserRoleManagement(40);
        seedRoleMenuManagement(50);
    }

    @Transactional
    public void seedUserManagement(int order) {
        Menu parent = menuRepository.findByName("Users")
                .orElseGet(() -> {
                    Menu m = new Menu();
                    m.setName("Users");
                    m.setApiPath("/api/v1/users/**");
                    m.setUrl("/users");
                    m.setIcon("user-group-icon");
                    m.setSortOrder(order);
                    return menuRepository.saveAndFlush(m);
                });

        seedChild("View Users", "user:read", "/api/v1/users", "GET", "/users", 1, parent);
        seedChild("Create User", "user:create", "/api/v1/users", "POST", "/users/create", 2, parent);
        seedChild("Update User", "user:update", "/api/v1/users/**", "PUT", "/users/edit", 3, parent);
        seedChild("Delete User", "user:delete", "/api/v1/users/**", "DELETE", "/users/delete", 4, parent);
    }

    @Transactional
    public void seedRoleManagement(int order) {
        Menu parent = menuRepository.findByName("Roles")
                .orElseGet(() -> {
                    Menu m = new Menu();
                    m.setName("Roles");
                    m.setApiPath("/api/v1/roles/**");
                    m.setUrl("/roles");
                    m.setIcon("shield-check-icon");
                    m.setSortOrder(order);
                    return menuRepository.saveAndFlush(m); 
                });

        seedChild("View Roles", "role:read", "/api/v1/roles", "GET", "/roles", 1, parent);
        seedChild("Create Role", "role:create", "/api/v1/roles", "POST", "/roles/create", 2, parent);
        seedChild("Update Role", "role:update", "/api/v1/roles", "PUT", "/roles/edit", 3, parent);
        seedChild("Delete Role", "role:delete", "/api/v1/roles", "DELETE", "/roles/delete", 4, parent);
    }

    @Transactional 
    public void seedMenuManagement(int order) {
        Menu parent = menuRepository.findByName("Menus")
                .orElseGet(() -> {
                    Menu m = new Menu();
                    m.setName("Menus");
                    m.setApiPath("/api/v1/menus/**");
                    m.setUrl("/menus");
                    m.setIcon("menu-list-icon");
                    m.setSortOrder(order);
                    return menuRepository.saveAndFlush(m);
                });

        seedChild("Create Menu Item", "menu:create", "/api/v1/menus", "POST", "/menus/new", 1, parent);
        seedChild("View Menus", "menu:read", "/api/v1/menus", "GET", "/menus", 2, parent);
        seedChild("Update Menu Item", "menu:update", "/api/v1/menus/**", "PUT", "/menus/edit", 3, parent);
        seedChild("Delete Menu Item", "menu:delete", "/api/v1/menus/**", "DELETE", "/menus/delete", 4, parent);
    }

    @Transactional
    public void seedUserRoleManagement(int order) {
        Menu parent = menuRepository.findByName("User-Role")
                .orElseGet(() -> {
                    Menu m = new Menu();
                    m.setName("User-Role");
                    m.setApiPath("/api/v1/user-roles/**");
                    m.setUrl("/user-roles");
                    m.setIcon("user-cog-icon");
                    m.setSortOrder(order);
                    return menuRepository.saveAndFlush(m);
                });

        seedChild("Assign User Role", "user-role:create", "/api/v1/user-roles", "POST", "/user-roles/assign", 1, parent);
        seedChild("Update User Role", "user-role:update", "/api/v1/user-roles", "PUT", "/user-roles/edit", 2, parent);
        seedChild("View User Roles", "user-role:read", "/api/v1/user-roles", "GET", "/user-roles", 3, parent);
    }

    @Transactional
    public void seedRoleMenuManagement(int order) {
        Menu parent = menuRepository.findByName("Role-Menu")
                .orElseGet(() -> {
                    Menu m = new Menu();
                    m.setName("Role-Menu");
                    m.setApiPath("/api/v1/role-menus/**");
                    m.setUrl("/role-menus");
                    m.setIcon("lock-open-icon");
                    m.setSortOrder(order);
                    return menuRepository.saveAndFlush(m);
                });

        seedChild("Assign Role Menu", "role-menu:create", "/api/v1/role-menus", "POST", "/role-menus/assign", 1, parent);
        seedChild("Update Role Menu", "role-menu:update", "/api/v1/role-menus", "PUT", "/role-menus/edit", 2, parent);
        seedChild("View Role Menus", "role-menu:read", "/api/v1/role-menus", "GET", "/role-menus", 3, parent);
    }


    @Transactional
    public void seedChild(String name, String key, String apiPath,
                           String method, String url, int order,
                           Menu parent) {

        if (menuRepository.findByPermissionKey(key).isEmpty()) {
            Menu child = new Menu();
            child.setName(name);
            child.setPermissionKey(key);
            child.setApiPath(apiPath);
            child.setHttpMethod(method);
            child.setUrl(url);
            child.setSortOrder(order);
            child.setParent(parent);

            menuRepository.save(child);
        }
    }
}