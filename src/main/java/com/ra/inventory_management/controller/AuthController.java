package com.ra.inventory_management.controller;


import com.ra.inventory_management.model.dto.request.UserRegister;
import com.ra.inventory_management.model.entity.Roles;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.service.UserService;
import com.ra.inventory_management.common.Constant;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/auth")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        Users user = new Users();
        user.setActiveFlag(1);
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/register")
    public String save(@Valid @ModelAttribute("user") UserRegister userRegister, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.handleRegister(userRegister);
        } catch (IllegalArgumentException e) {
            model.addAttribute("err", e.getMessage());
            return "auth/register";
        }

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new Users());
        return "login/login";
    }

    @PostMapping("/processLogin")
    public String processLogin(@ModelAttribute("loginForm") @Validated Users users,
                               BindingResult result,
                               Model model,
                               HttpSession session,
                               @RequestParam(defaultValue = "12", name = "limit") int limit,
                               @RequestParam(defaultValue = "0", name = "page") int page,
                               @RequestParam(defaultValue = "id", name = "sort") String sort,
                               @RequestParam(defaultValue = "asc", name = "order") String order) {

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        if (order.equals("desc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }

        if (result.hasErrors()) {
            return "login/login";
        }

        List<Users> userList = userService.getAll(pageable).getContent();
        if (userList.isEmpty()) {
            model.addAttribute("error", "Tài khoản không tồn tại!");
            return "login/login";
        }

        Users user = userList.get(0);
        if (user.getRoles().isEmpty()) {
            model.addAttribute("error", "Người dùng không có vai trò!");
            return "login/login";
        }

        Roles role = user.getRoles().iterator().next();

        session.setAttribute(Constant.USER_INFO, user);

        return "redirect:/index";
    }

}