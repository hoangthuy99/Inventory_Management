package com.ra.inventory_management.Controller;

import com.ra.inventory_management.model.dto.request.PasswordRequest;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.sercurity.UserDetail.UserPrincipal;
import com.ra.inventory_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;



@Controller
@RequestMapping("/user/account")
public class AccountController {
    @Value("${path-upload}")
    private String pathUpload;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUsers().getId();
    }

    @GetMapping("")
    public String getAccount(Model model) {
        Long userId = getUserId();
        Users user = userService.findById(userId);
        PasswordRequest passwordRequest = new PasswordRequest();
        model.addAttribute("user", user);
        model.addAttribute("passwordRequest", passwordRequest);
        return "/dashboard/my-account";
    }



    @PostMapping("/edit-account")
    public String updateAcc(@ModelAttribute("user") Users user, @RequestParam("imageUser") MultipartFile file) {
        Long id = getUserId();
        String fileName = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(),new File(pathUpload+fileName));
            // lưu tên file vào database
            user.setAvatar(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userService.updateAcc(user, id);
        return "redirect:/user/account";
    }


    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("passwordRequest") PasswordRequest passwordRequest) {
        Long id = getUserId();
        Users user = userService.findById(id);
        String oldPassword = user.getPassword();

        boolean isPaswordMatch = passwordEncoder.matches(passwordRequest.getOldPass(), oldPassword);
        if (isPaswordMatch) {
            if (!passwordRequest.getNewPass().equals(passwordRequest.getConfirmNewPass())) {
//                return new ResponseEntity<>("Xác nhận mật khẩu không chính xác!", HttpStatus.BAD_REQUEST);
            }
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPass()));
            userService.save(user);
//            return new ResponseEntity<>("cập nhật mật khẩu thành công", HttpStatus.OK);
        }

        return "redirect:/user/account";
    }
}
