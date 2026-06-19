package com.company.ems.menu;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/menus")
public class MenuController {
	private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('menu:read')")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getMenuTree() {
        ApiResponse<List<MenuDto>> response = new ApiResponse<>(true,"Menu tree fetched successfully",menuService.getAllActiveMenus() );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('menu:create')")
    public ResponseEntity<ApiResponse<MenuDto>> createMenu(@RequestBody @Valid CreateMenuDto dto) {
        ApiResponse<MenuDto> response = new ApiResponse<>(true, "Menu created successfully", menuService.createMenu(dto));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('menu:read')")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getAllActiveMenus() {
        ApiResponse<List<MenuDto>> response = new ApiResponse<>(true, "Active menus fetched successfully", menuService.getAllActiveMenus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('menu:read')")
    public ResponseEntity<ApiResponse<MenuDto>> getMenuById(@PathVariable UUID id) {
        ApiResponse<MenuDto> response = new ApiResponse<>(true, "Menu asset fetched successfully", menuService.getMenuById(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('menu:update')")
    public ResponseEntity<ApiResponse<MenuDto>> updateMenu(@PathVariable UUID id, @RequestBody @Valid EditMenuDto dto) {
        ApiResponse<MenuDto> response = new ApiResponse<>(true, "Menu asset updated successfully", menuService.updateMenu(id, dto));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('menu:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable UUID id) {
        menuService.deleteMenu(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Menu asset soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }

}
