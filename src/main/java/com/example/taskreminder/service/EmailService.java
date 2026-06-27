package com.example.taskreminder.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReminder(String toEmail, String messageText) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("⏰ Task Reminder");
            message.setText(messageText);

            mailSender.send(message);
            System.out.println("✅ EMAIL SENT TO: " + toEmail);

        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED");
            e.printStackTrace();
        }
    }



}
