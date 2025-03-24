package com.ra.inventory_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String token) {
        String url = "http://localhost:8089/app/auth/verify?token=" + token;
        String message = "Vui lòng nhấn vào đường link sau để xác thực tài khoản: " + url;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Xác thực tài khoản");
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}

