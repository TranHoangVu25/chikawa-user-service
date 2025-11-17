package com.chikawa.user_service.services;

public interface EmailService {
    void sendMail(String to, String subject, String body);
}
