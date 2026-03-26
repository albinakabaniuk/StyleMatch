package com.stylematch.dto;

import java.util.UUID;

public class UserProfileResponse {

    private UUID id;
    private String email;
    private String name;
    private Integer age;
    private Double weight;
    private Double height;
    private String language;
    private String gender;
    private String avatar;
    private String clothingSize;

    public UserProfileResponse() {}

    public UserProfileResponse(UUID id, String email, String name, Integer age, Double weight, Double height, 
                              String language, String gender, String avatar, String clothingSize) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.language = language;
        this.gender = gender;
        this.avatar = avatar;
        this.clothingSize = clothingSize;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getClothingSize() { return clothingSize; }
    public void setClothingSize(String clothingSize) { this.clothingSize = clothingSize; }

    // Manual builder because Lombok @Builder is failing
    public static class UserProfileResponseBuilder {
        private UUID id;
        private String email;
        private String name;
        private Integer age;
        private Double weight;
        private Double height;
        private String language;
        private String gender;
        private String avatar;
        private String clothingSize;

        public UserProfileResponseBuilder id(UUID id) { this.id = id; return this; }
        public UserProfileResponseBuilder email(String email) { this.email = email; return this; }
        public UserProfileResponseBuilder name(String name) { this.name = name; return this; }
        public UserProfileResponseBuilder age(Integer age) { this.age = age; return this; }
        public UserProfileResponseBuilder weight(Double weight) { this.weight = weight; return this; }
        public UserProfileResponseBuilder height(Double height) { this.height = height; return this; }
        public UserProfileResponseBuilder language(String language) { this.language = language; return this; }
        public UserProfileResponseBuilder gender(String gender) { this.gender = gender; return this; }
        public UserProfileResponseBuilder avatar(String avatar) { this.avatar = avatar; return this; }
        public UserProfileResponseBuilder clothingSize(String clothingSize) { this.clothingSize = clothingSize; return this; }

        public UserProfileResponse build() {
            return new UserProfileResponse(id, email, name, age, weight, height, language, gender, avatar, clothingSize);
        }
    }

    public static UserProfileResponseBuilder builder() {
        return new UserProfileResponseBuilder();
    }
}
