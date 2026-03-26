package com.stylematch.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    public PasswordResetToken() {}
    public PasswordResetToken(UUID id, String token, User user, LocalDateTime expiresAt, boolean used) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
        this.used = used;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public static class PasswordResetTokenBuilder {
        private UUID id;
        private String token;
        private User user;
        private LocalDateTime expiresAt;
        private boolean used;

        public PasswordResetTokenBuilder id(UUID id) { this.id = id; return this; }
        public PasswordResetTokenBuilder token(String token) { this.token = token; return this; }
        public PasswordResetTokenBuilder user(User user) { this.user = user; return this; }
        public PasswordResetTokenBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public PasswordResetTokenBuilder used(boolean used) { this.used = used; return this; }

        public PasswordResetToken build() {
            return new PasswordResetToken(id, token, user, expiresAt, used);
        }
    }

    public static PasswordResetTokenBuilder builder() {
        return new PasswordResetTokenBuilder();
    }
}
