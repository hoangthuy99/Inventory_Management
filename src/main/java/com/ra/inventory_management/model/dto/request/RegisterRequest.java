package com.ra.inventory_management.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterRequest {
    @Size(min = 6, max = 100, message = "số kí tự không chính xác")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "không chứa kí tự đặc biệt")
    private String username;
    private String userCode;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email bạn chưa đúng định dạng")
    private String email;
    @NotNull(message = "Không được null")
    private String fullname;
    @NotNull(message = "Không được null")
    @Pattern(regexp = "\\A(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])\\S{8,}\\z", message = "Password chưa đúng định dạng, hãy nhập đủ 8 ký tự , có ký tự viết hoa và ký tự đặc biệt")
    private String password;
    private String confirmPassword;
    @Pattern(regexp = "^0[1-9]\\d{8}$", message = "Số điện thoại chưa được định dạng đúng")
    private String phone;
    @NotNull(message = "Không được null")
    private String address;

    private Integer activeFlag = 1;

    private Long roleId;

}
