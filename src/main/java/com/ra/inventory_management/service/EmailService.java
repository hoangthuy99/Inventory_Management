package com.ra.inventory_management.service;

import com.ra.inventory_management.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

//    @KafkaListener(id = "email", topics = "spring-boot-topic")
//    @Async

    public void sendEmail(UserDTO userDTO) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("hithuy98@gmail.com");
        simpleMailMessage.setTo(userDTO.getEmail());
        simpleMailMessage.setText("Cảm ơn " + userDTO.getUserName() + " đã đăng ký tài khoản thành công");
        simpleMailMessage.setSubject("Thư cảm ơn");
        javaMailSender.send(simpleMailMessage);

    }

    public void sendVerificationEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hithuy98@gmail.com");
        message.setTo(email);
        message.setSubject("Xác nhận tài khoản");
        message.setText("Nhấp vào liên kết sau để xác nhận tài khoản: " +
                "http://localhost:8080/app/auth/verify?code=" + verificationCode);
        javaMailSender.send(message);
    }
}

