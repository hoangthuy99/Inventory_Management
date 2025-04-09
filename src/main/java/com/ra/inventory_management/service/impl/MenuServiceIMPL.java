package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.model.dto.request.MenuRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.Menu;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.reponsitory.MenuRepository;
import com.ra.inventory_management.reponsitory.RoleRepository;
import com.ra.inventory_management.reponsitory.UserRepository;
import com.ra.inventory_management.sercurity.exception.ResourceNotFoundException;
import com.ra.inventory_management.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuServiceIMPL implements MenuService {
    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public Optional<Menu> getByIdMenu(Long id) {
        return menuRepository.findByIdWithRoles(id);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

//    @Override
//    public Menu create(MenuRequest request) {
//        if (menuRepository.existsByName(request.getName())) {
//            throw new RuntimeException("Tên quyền đã tồn tại");
//        }
//        Menu menu = new Menu();
//        Roles roles = roleRepository.findById(request.getRoleId()).
//                orElseThrow(() -> new IllegalArgumentException("Vai trò không tồn tại vai trò với id: " + request.getRoleId()));
//
//        Set<Roles> rolesExits = menu.getRoles();
//        rolesExits.removeAll(menu.getRoles());
//        rolesExits.add(roles);
//        menu.setCode(request.getCode());
//        menu.setName(request.getName());
//        menu.setPath(request.getPath());
//        menu.setIcon(request.getIcon());
//        menu.setParentId(request.getParentId());
//        menu.setActiveFlag(request.getActiveFlag());
//        menu.setRoles(rolesExits);
//
//        return menuRepository.save(menu);
//    }

    @Override
    public Menu update(MenuRequest request, Long id) {
        // Tìm menu theo id
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu không tồn tại với id: " + id));

        // Cập nhật các thuộc tính menu
        menu.setCode(request.getCode());
        menu.setName(request.getName());
        menu.setPath(request.getPath());
        menu.setIcon(request.getIcon());
        menu.setParentId(request.getParentId());
        menu.setActiveFlag(request.getActiveFlag());
        menu.setUpdatedDate(LocalDateTime.now());

        // Xử lý danh sách roleIds
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Roles> roles = request.getRoleIds().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new IllegalArgumentException("Vai trò không tồn tại với id: " + roleId)))
                    .collect(Collectors.toSet());

            menu.setRoles(roles); // Gán danh sách roles
        } else {
            menu.setRoles(new HashSet<>()); // Không có roles
        }

        // Lưu menu với roles đã cập nhật
        return menuRepository.save(menu);
    }

    @Override
    public Page<Menu> search(SearchRequest request) {
        return null;
    }

    @Override
    public List<Menu> getMenuByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users userPrincipal = (Users) authentication.getPrincipal();

        List<Menu> menus = userRepository.findById(userPrincipal.getId()).orElse(new Users()).getRoles().get(0).getMenus();

        return menus;
    }
}
