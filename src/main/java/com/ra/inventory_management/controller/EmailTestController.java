package com.ra.inventory_management.controller;

import com.ra.inventory_management.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/api/email/send-test-email")
    public String sendTestEmail(@RequestBody EmailRequest request) {
        String email = request.getEmail();
        emailService.sendEmail(email, "Test Subject", "This is a test email sent at " + new java.util.Date());
        return "Email test đã được gửi tới: " + email;
    }

    // Class hỗ trợ nhận dữ liệu JSON từ request body
    static class EmailRequest {
        private String email;

        // Getter và Setter
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}


