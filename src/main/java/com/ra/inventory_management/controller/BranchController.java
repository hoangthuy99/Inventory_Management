package com.ra.inventory_management.controller;

import com.ra.inventory_management.model.dto.response.BaseResponse;
import com.ra.inventory_management.model.entity.Branch;
import com.ra.inventory_management.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/app/branch")
public class BranchController {
    @Value("${path-upload}")
    private String uploadDir;

    @Autowired
    private BranchService branchService;

    @PreAuthorize("hasAuthority('ALL_BRANCH')")
    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAll());
    }

    @PreAuthorize("hasAuthority('ALL_BRANCH')")
    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Optional<Branch> branch = branchService.findById(id);
        return branch.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ADD_BRANCH')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBranch(@ModelAttribute Branch branch,
                                          @RequestParam(value = "img", required = false) MultipartFile image
    ) {
        // Kiểm tra xem có upload file không
        if (image != null && !image.isEmpty()) {
            try {
                // Đọc ảnh từ input stream
                BufferedImage originalImage = ImageIO.read(image.getInputStream());
                if (originalImage == null) {
                    return ResponseEntity.badRequest().body("Tệp tải lên không hợp lệ hoặc bị lỗi!");
                }
                ByteArrayOutputStream os = new ByteArrayOutputStream();

                // Lưu file vào thư mục "uploads"
                Path uploadPath = Paths.get(uploadDir + "/branch");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(image.getInputStream(), uploadPath.resolve(image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

                String imagePath = "uploads/branch/" + image.getOriginalFilename();
                // Gọi service để lưu sản phẩm
                branch.setMapImage(imagePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu ảnh!");
            }
        }

        return ResponseEntity.ok(branchService.save(branch));
    }

    @PreAuthorize("hasAuthority('EDIT_BRANCH')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @ModelAttribute Branch branchDetails,
                                          @RequestParam(value = "img", required = false) MultipartFile image
    ) {
        Optional<Branch> existingBranch = branchService.findById(id);

        if (existingBranch.isPresent()) {
            // Kiểm tra xem có upload file không
            if (image != null && !image.isEmpty()) {
                try {
                    // Đọc ảnh từ input stream
                    BufferedImage originalImage = ImageIO.read(image.getInputStream());
                    if (originalImage == null) {
                        return ResponseEntity.badRequest().body("Tệp tải lên không hợp lệ hoặc bị lỗi!");
                    }
                    ByteArrayOutputStream os = new ByteArrayOutputStream();

                    // Lưu file vào thư mục "uploads"
                    Path uploadPath = Paths.get(uploadDir + "/branch");
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Files.copy(image.getInputStream(), uploadPath.resolve(image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

                    // Set value branch detail
                    branchDetails.setMapImage("uploads/branch/" + image.getOriginalFilename());
                    branchDetails.setId(id);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu ảnh!");
                }

            }

            return ResponseEntity.ok(branchService.save(branchDetails));
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('EDIT_BRANCH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ALL_BRANCH')")
    @GetMapping("/search")
    public ResponseEntity<List<Branch>> searchBranches(@RequestParam String keyword) {
        return ResponseEntity.ok(branchService.searchByName(keyword));
    }

    // API import file excel
    @PreAuthorize("hasAuthority('ADD_BRANCH')")
    @PostMapping(value = "importExcel", produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> importExcel(
            @RequestParam("file") MultipartFile file
    )
            throws IOException {
        List<Branch> response = branchService.importExcel(file);
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }

    @PreAuthorize("hasAuthority('ALL_BRANCH')")
    @GetMapping("sampleExcel")
    public ResponseEntity<?> getSampleExcel() throws IOException {
        Map<String, String> response = branchService.getSampleExcel();
        return ResponseEntity.ok().body(new BaseResponse<>(response));
    }
}
