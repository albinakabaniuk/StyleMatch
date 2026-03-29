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

            helper.setFrom("StyleMatch <" + fromEmail + ">");
            helper.setTo(to);
            helper.setSubject("StyleMatch: Reset Your Password ✨");

            log.info("Attempting to send password reset email to: {}", to);
            // ... (htmlContent generation omitted for brevity in instruction, keeping same as before)
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
            log.info("SUCCESS: Password reset email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("SMTP FAILURE: Failed to send reset email to {}. Error: {}", to, e.getMessage());
            log.error("TIP: Ensure SPRING_MAIL_HOST and SPRING_MAIL_PASSWORD are set correctly in your environment.");
        } catch (Exception e) {
            log.error("UNEXPECTED EMAIL ERROR for {}: {}", to, e.getMessage());
        }
    }
}
