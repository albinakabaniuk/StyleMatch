package com.stylematch.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}
