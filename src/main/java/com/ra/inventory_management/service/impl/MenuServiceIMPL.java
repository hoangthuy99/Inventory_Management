package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.MenuRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Menu;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.reponsitory.MenuRepository;
import com.ra.inventory_management.sercurity.exception.ResourceNotFoundException;
import com.ra.inventory_management.service.MenuService;
import com.ra.inventory_management.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuServiceIMPL implements MenuService {
    private final MenuRepository menuRepository;
    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public void delete(Long id) {
   menuRepository.deleteById(id);
    }

    @Override
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu update(MenuRequest request, Long id) {
        Optional<Menu> optionalMenu = menuRepository.findById(id);
        if (optionalMenu.isEmpty()) {
            throw new ResourceNotFoundException("Menu not found with id: " + id);
        }
        Menu menu = optionalMenu.get();
        // Cập nhật thông tin từ request
        menu.setCode(request.getCode());
        menu.setName(request.getName());
        menu.setPath(request.getPath());
        menu.setIcon(request.getIcon());
        menu.setParentId(request.getParentId());
        menu.setActiveFlag(request.getActiveFlag());
        return menuRepository.save(menu);
    }

    @Override
    public Page<Menu> search(SearchRequest request) {
        // Lấy thông tin user hiện tại từ context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users userPrincipal = (Users) authentication.getPrincipal();

        // Lấy danh sách menu mà user hiện tại có quyền truy cập thông qua Role → Auth → Permission → Menu
        Set<Menu> userMenus = userPrincipal.getRoles().stream()
                .flatMap(role -> role.getAuths().stream())
                .map(auth -> auth.getPermission().getMenu())
                .collect(Collectors.toSet());

        // Tạo đối tượng phân trang Pageable
        Pageable pageable = PageableUtil.create(request.getPageNum(), request.getPageSize(), request.getSortBy(), request.getSortType());

        // Lấy danh sách menu có phân trang từ repository
        Page<Menu> menuPage = menuRepository.searchMenu(request.getSearchKey(), request.getStatus(), pageable);

        // Lọc bỏ các menu mà user hiện tại đã có quyền truy cập
        List<Menu> filteredMenus = menuPage.getContent().stream()
                .filter(menu -> !userMenus.contains(menu))
                .collect(Collectors.toList());

        return new PageImpl<>(filteredMenus, pageable, filteredMenus.size());
    }
}
