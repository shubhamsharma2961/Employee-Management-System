package com.company.ems.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.ems.common.MenuType;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuRepository menuRepository, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    private String clean(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    @Override
    public MenuDto createMenu(CreateMenuDto dto) {

        validateMenu(dto);
        Menu menu = menuMapper.toEntity(dto);
        menu.setPermissionKey(clean(menu.getPermissionKey()));
        if (dto.getParentId() != null) {
            Menu parent = menuRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            menu.setParent(parent);
        }
        Menu saved = menuRepository.save(menu);
        return menuMapper.toDto(saved);
    }
    
    private void validateMenu(CreateMenuDto dto) {
        if (dto.getMenuType() == MenuType.PAGE) {
            if (dto.getPermissionKey() != null && !dto.getPermissionKey().isBlank()) {
                throw new RuntimeException("PAGE menu should not have permissionKey");
            }
        }
        if (dto.getMenuType() == MenuType.ACTION || dto.getMenuType() == MenuType.LOOKUP) {
            if (dto.getPermissionKey() == null || dto.getPermissionKey().isBlank()) {
                throw new RuntimeException("ACTION/LOOKUP must have permissionKey");
            }
        }
    }

    @Override
    public List<MenuDto> getAllActiveMenus() {
        List<Menu> menus =menuRepository.findAllByIsDeletedFalseOrderBySortOrderAsc();
        List<MenuDto> dtos = menuMapper.toDtoList(menus);
        Map<UUID, MenuDto> map = new HashMap<>();
        for (MenuDto dto : dtos) {
            dto.setChildren(new ArrayList<>());
            map.put(dto.getId(), dto);
        }
        List<MenuDto> roots = new ArrayList<>();
        for (Menu menu : menus) {
            MenuDto dto = map.get(menu.getId());
            if (menu.getParent() != null && menu.getParent().getId() != null) {
                MenuDto parent = map.get(menu.getParent().getId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }

            } else {
                roots.add(dto);
            }
        }
        sortTree(roots);
        return roots;
    }
    
    private void sortTree(List<MenuDto> menus) {
        menus.sort(Comparator.comparing(MenuDto::getSortOrder));
        for (MenuDto menu : menus) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                sortTree(menu.getChildren());
            }
        }
    }

    @Override
    public MenuDto getMenuById(UUID id) {
        Menu menu = menuRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        return menuMapper.toDto(menu);
    }

    @Override
    public MenuDto updateMenu(UUID id, EditMenuDto dto) {
    	
    	validateMenu(dto);
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        if (dto.getParentId() != null && dto.getParentId().equals(id)) {
            throw new RuntimeException("Menu cannot be its own parent");
        }
        menuMapper.updateEntityFromDto(dto, menu);
        menu.setPermissionKey(clean(menu.getPermissionKey()));
        if (dto.getParentId() != null) {
            Menu parent = menuRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            menu.setParent(parent);
        } else {
            menu.setParent(null);
        }

        Menu updated = menuRepository.save(menu);
        return menuMapper.toDto(updated);
    }
    
    private void validateMenu(EditMenuDto dto) {
        if (dto.getMenuType() == MenuType.PAGE) {
            if (dto.getPermissionKey() != null && !dto.getPermissionKey().isBlank()) {
                throw new RuntimeException("PAGE menu should not have permissionKey");
            }
        }
        if (dto.getMenuType() == MenuType.ACTION || dto.getMenuType() == MenuType.LOOKUP) {
            if (dto.getPermissionKey() == null || dto.getPermissionKey().isBlank()) {
                throw new RuntimeException("ACTION/LOOKUP must have permissionKey");
            }
        }
    }

    @Override
    public void deleteMenu(UUID id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        menu.setDeleted(true);
        menuRepository.save(menu);
    }
}