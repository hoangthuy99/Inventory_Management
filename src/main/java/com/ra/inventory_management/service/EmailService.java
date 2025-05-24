package com.ra.inventory_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;  // Địa chỉ email gửi đi

    public void sendEmail(String toEmail, String subject, String message) {
        String fromEmail = "hithuy98@gmail.com";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        try {
            javaMailSender.send(simpleMailMessage);
            System.out.println("Email đã được gửi thành công tới: " + toEmail);
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void sendVerificationEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);  // Sử dụng từ cấu hình
        message.setTo(email);
        message.setSubject("Xác nhận tài khoản");
        message.setText("Nhấp vào liên kết sau để xác nhận tài khoản: " +
                "http://localhost:8080/app/auth/verify?code=" + verificationCode);
        try {
            javaMailSender.send(message);
            System.out.println("Đã gửi thành công!");
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi chi tiết
        }
    }


}