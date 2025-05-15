package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.request.RegisterRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.UserGoogle;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.reponsitory.UserGoogleRepository;
import com.ra.inventory_management.service.RoleService;
import com.ra.inventory_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserGoogleRepository userGoogleRepository;

    @Autowired
    private RoleService roleService;

    @PostMapping("search")
    public ResponseEntity<?> userPage(@RequestBody SearchRequest request) {
        Page<Users> users = userService.search(request);
        return ResponseEntity.ok().body(new BaseResponse<>(users));
    }
    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody RegisterRequest request) {
        Users users = userService.handleRegister(request);
        return ResponseEntity.created(URI.create("/app/user/" + users.getId())).body(new BaseResponse<>(users));
    }
    @GetMapping()
    public ResponseEntity<List<Users>> getAll(){
         List<Users> users = userService.getAll();
         return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Users users = userService.findById(id);
        return ResponseEntity.ok().body(new BaseResponse<>(users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RegisterRequest request) {
        Users users = userService.update(request, id);
        return ResponseEntity.ok().body(new BaseResponse<>(users));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().body(new BaseResponse<>(true));
    }

    @GetMapping("getAllUsersGG")
    public ResponseEntity<?> getAllUserGoogle() {
        List<UserGoogle> userGoogles = userGoogleRepository.findAll();
        return ResponseEntity.ok().body(new BaseResponse<>(userGoogles));
    }
    @GetMapping("/profile")
    public ResponseEntity<Users> getUserProfile(Principal principal) {
        String username = principal.getName();
        Optional<Users> user = userService.getAll()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst(); // Tìm người dùng theo tên đăng nhập
        // Nếu có người dùng, trả về thông tin người dùng
        // Trả về mã 404 nếu không tìm thấy người dùng
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @GetMapping("/gmail/messages")
    public ResponseEntity<?> getGmailMessages() {
        try {
            // Bước 1: Lấy accessToken (đã lưu từ quá trình OAuth)
            String accessToken = "YOUR_ACCESS_TOKEN"; // hoặc lấy từ DB / session / cache...

            // Bước 2: Gửi request tới Gmail API
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://gmail.googleapis.com/gmail/v1/users/me/messages"))
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Bước 3: Trả response về frontend
            return ResponseEntity.ok().body(response.body());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy email: " + e.getMessage());
        }
    }

}
