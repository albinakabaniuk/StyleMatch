package com.stylematch.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@stylematch.com}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("StyleMatch: Reset Your Password ✨");

            String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 12px;'>"
                    + "<h2 style='color: #c084fc; text-align: center;'>StyleMatch</h2>"
                    + "<p>Hello,</p>"
                    + "<p>We received a request to reset your password. Use the token below to set a new one:</p>"
                    + "<div style='background: #f9f9f9; padding: 15px; border-radius: 8px; text-align: center; font-size: 1.2rem; font-weight: bold; color: #c084fc; margin: 20px 0;'>"
                    + token
                    + "</div>"
                    + "<p>If you didn't request this, you can safely ignore this email.</p>"
                    + "<p style='font-size: 0.8rem; color: #999; margin-top: 30px;'>Stay stylish! ✨<br>The StyleMatch Team</p>"
                    + "</div></body></html>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}: {}", to, e.getMessage());
            // We don't throw here to avoid disclosing email existence vs failure to the client
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}: {}", to, e.getMessage());
        }
    }
}
