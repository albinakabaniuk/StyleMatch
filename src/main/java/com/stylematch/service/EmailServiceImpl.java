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

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.host:localhost}")
    private String mailHost;

    @Value("${spring.mail.port:587}")
    private String mailPort;

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("StyleMatch <" + fromEmail + ">");
            helper.setTo(to);
            helper.setSubject("StyleMatch: Reset Your Password ✨");

            String resetLink = frontendUrl + "/reset-password?token=" + token;

            log.info("Attempting to send password reset email to: {}", to);
            String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 12px;'>"
                    + "<h2 style='color: #c084fc; text-align: center;'>StyleMatch</h2>"
                    + "<p>Hello,</p>"
                    + "<p>We received a request to reset your password. Click the button below to set a new one:</p>"
                    + "<div style='text-align: center; margin: 30px 0;'>"
                    + "<a href='" + resetLink + "' style='background-color: #c084fc; color: white; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;'>Reset Password ✨</a>"
                    + "</div>"
                    + "<p>Or copy and paste this link into your browser:</p>"
                    + "<p style='word-break: break-all; color: #c084fc;'>" + resetLink + "</p>"
                    + "<p style='margin-top: 20px;'>Your reset token is: <strong>" + token + "</strong></p>"
                    + "<p>If you didn't request this, you can safely ignore this email.</p>"
                    + "<p style='font-size: 0.8rem; color: #999; margin-top: 30px;'>Stay stylish! ✨<br>The StyleMatch Team</p>"
                    + "</div></body></html>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("SUCCESS: Password reset email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("SMTP FAILURE: Failed to send reset email to {}. Host: {}:{}, User: {}. Error: {}", to, mailHost, mailPort, fromEmail, e.getMessage());
            throw new RuntimeException("SMTP Connection Failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("UNEXPECTED EMAIL ERROR for {}: {}", to, e.getMessage());
            throw new RuntimeException("Email Error: " + e.getMessage());
        }
    }

    @Override
    public void sendPasswordChangedNotification(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("StyleMatch <" + fromEmail + ">");
            helper.setTo(to);
            helper.setSubject("StyleMatch: Password Successfully Changed ✅");

            log.info("Attempting to send password change notification to: {}", to);
            String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 12px;'>"
                    + "<h2 style='color: #22c55e; text-align: center;'>Security Alert</h2>"
                    + "<p>Hello,</p>"
                    + "<p>The password for your StyleMatch account has been <b>successfully changed</b>.</p>"
                    + "<p>If you made this change, you can safely ignore this email.</p>"
                    + "<div style='background: #fff7ed; padding: 15px; border-radius: 8px; border: 1px solid #fdba74; color: #9a3412; margin: 20px 0; font-size: 0.9rem;'>"
                    + "If you did NOT make this change, please contact our support team immediately or reset your password again to secure your account."
                    + "</div>"
                    + "<p style='font-size: 0.8rem; color: #999; margin-top: 30px;'>Stay secure! ✨<br>The StyleMatch Team</p>"
                    + "</div></body></html>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("SUCCESS: Password change notification sent to: {}", to);
        } catch (MessagingException e) {
            log.error("SMTP FAILURE: Failed to send change notification to {}. Error: {}", to, e.getMessage());
            log.error("TIP: Ensure SPRING_MAIL_HOST and SPRING_MAIL_PASSWORD are set correctly.");
        } catch (Exception e) {
            log.error("UNEXPECTED EMAIL ERROR for {}: {}", to, e.getMessage());
        }
    }
}
