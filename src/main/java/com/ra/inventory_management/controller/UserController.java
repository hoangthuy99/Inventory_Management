package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String userPage(Model model,
                           @RequestParam(defaultValue = "3", name = "limit") int limit,
                           @RequestParam(defaultValue = "0", name = "page") int page,
                           @RequestParam(defaultValue = "id", name = "sort") String sort,
                           @RequestParam(defaultValue = "asc", name = "order") String order,
                           @RequestParam(value = "nameSearch",required = false) String nameSearch

    ) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }

        Page<Users> users = userService.getAll(pageable);
        int currentPage = users.getNumber();
        model.addAttribute("users", users);
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("totalPage", users.getTotalPages());
        model.addAttribute("nameSearch", nameSearch);

        return "/admin/user/user";
        }

    @GetMapping("/user/status/{id}")
    public String updateStatus(@PathVariable("id") Long id) {
        Users user = userService.findById(id);

        userService.save(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/user/search")
    public String searchByName(@RequestParam("nameSearch") String keyword, Model model) {
        List<Users> users = userService.searchByName(keyword);
        model.addAttribute("users", users);
        return "/admin/user/user";
    }
}
