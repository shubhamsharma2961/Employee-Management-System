package com.company.ems.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.company.ems.common.MenuType;

import jakarta.validation.constraints.NotBlank;

public class MenuDto {
    private UUID id; 
    
    @NotBlank(message = "Menu name is required")
    private String name;
    private String url;
    private String icon;
    private Integer sortOrder;
    private String permissionKey;  
    private List<MenuDto> children = new ArrayList<>();
    private UUID parentId;
    private String apiPath;
    private String httpMethod;
    private MenuType menuType;
    
	public String getApiPath() {
		return apiPath;
	}
	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public MenuType getMenuType() {
		return menuType;
	}
	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}
	public List<MenuDto> getChildren() {
		return children;
	}
	public UUID getParentId() {
		return parentId;
	}
	public void setParentId(UUID parentId) {
		this.parentId = parentId;
	}
	public void setChildren(List<MenuDto> children) {
		this.children = children;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getPermissionKey() {
		return permissionKey;
	}
	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
	}
}
