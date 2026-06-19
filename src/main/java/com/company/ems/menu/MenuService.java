package com.company.ems.menu;

import java.util.List;
import java.util.UUID;

public interface MenuService {
	MenuDto createMenu(CreateMenuDto dto);
    List<MenuDto> getAllActiveMenus();
    MenuDto getMenuById(UUID id);
    MenuDto updateMenu(UUID id, EditMenuDto dto);
    void deleteMenu(UUID id);

}
