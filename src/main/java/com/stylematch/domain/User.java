package com.stylematch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users") // "user" is a reserved keyword in some databases like PostgreSQL
public class User implements UserDetails {

    public User() {}

    public User(UUID id, String email, String password, Role role, String preferredLanguage, 
                String name, Integer age, Double weight, Double height, String gender, 
                String avatar, String clothingSize, List<TestResult> testResults) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.preferredLanguage = preferredLanguage;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.avatar = avatar;
        this.clothingSize = clothingSize;
        this.testResults = testResults != null ? testResults : new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "gender")
    private String gender;
    
    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "clothing_size")
    private String clothingSize;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults = new ArrayList<>();

    public void addTestResult(TestResult result) {
        testResults.add(result);
        result.setUser(this);
    }

    public void removeTestResult(TestResult result) {
        testResults.remove(result);
        result.setUser(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Explicit getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getClothingSize() { return clothingSize; }
    public void setClothingSize(String clothingSize) { this.clothingSize = clothingSize; }
    public List<TestResult> getTestResults() { return testResults; }
    public void setTestResults(List<TestResult> testResults) { this.testResults = testResults; }

    // Manual builder
    public static class UserBuilder {
        private UUID id;
        private String email;
        private String password;
        private Role role;
        private String preferredLanguage;
        private String name;
        private Integer age;
        private Double weight;
        private Double height;
        private String gender;
        private String avatar;
        private String clothingSize;
        private List<TestResult> testResults = new ArrayList<>();

        public UserBuilder id(UUID id) { this.id = id; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder preferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; return this; }
        public UserBuilder name(String name) { this.name = name; return this; }
        public UserBuilder age(Integer age) { this.age = age; return this; }
        public UserBuilder weight(Double weight) { this.weight = weight; return this; }
        public UserBuilder height(Double height) { this.height = height; return this; }
        public UserBuilder gender(String gender) { this.gender = gender; return this; }
        public UserBuilder avatar(String avatar) { this.avatar = avatar; return this; }
        public UserBuilder clothingSize(String clothingSize) { this.clothingSize = clothingSize; return this; }
        public UserBuilder testResults(List<TestResult> testResults) { this.testResults = testResults; return this; }

        public User build() {
            User user = new User(id, email, password, role, preferredLanguage, name, age, weight, height, gender, avatar, clothingSize, testResults);
            return user;
        }
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }
}
