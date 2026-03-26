package com.stylematch.dto;

import jakarta.validation.constraints.Positive;

public class UpdateUserProfileRequest {
    private String name;
    private String email;
    private String gender;

    @Positive(message = "Age must be greater than 0")
    private Integer age;

    @Positive(message = "Weight must be greater than 0")
    private Double weight;

    @Positive(message = "Height must be greater than 0")
    private Double height;

    private String preferredLanguage;
    private String avatar;
    private String clothingSize;

    public UpdateUserProfileRequest() {}

    public UpdateUserProfileRequest(String name, String email, String gender, Integer age, Double weight, 
                                   Double height, String preferredLanguage, String avatar, String clothingSize) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.preferredLanguage = preferredLanguage;
        this.avatar = avatar;
        this.clothingSize = clothingSize;
    }

    // Manual builder
    public static class UpdateUserProfileRequestBuilder {
        private String name;
        private String email;
        private String gender;
        private Integer age;
        private Double weight;
        private Double height;
        private String preferredLanguage;
        private String avatar;
        private String clothingSize;

        public UpdateUserProfileRequestBuilder name(String name) { this.name = name; return this; }
        public UpdateUserProfileRequestBuilder email(String email) { this.email = email; return this; }
        public UpdateUserProfileRequestBuilder gender(String gender) { this.gender = gender; return this; }
        public UpdateUserProfileRequestBuilder age(Integer age) { this.age = age; return this; }
        public UpdateUserProfileRequestBuilder weight(Double weight) { this.weight = weight; return this; }
        public UpdateUserProfileRequestBuilder height(Double height) { this.height = height; return this; }
        public UpdateUserProfileRequestBuilder preferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; return this; }
        public UpdateUserProfileRequestBuilder avatar(String avatar) { this.avatar = avatar; return this; }
        public UpdateUserProfileRequestBuilder clothingSize(String clothingSize) { this.clothingSize = clothingSize; return this; }

        public UpdateUserProfileRequest build() {
            return new UpdateUserProfileRequest(name, email, gender, age, weight, height, preferredLanguage, avatar, clothingSize);
        }
    }

    public static UpdateUserProfileRequestBuilder builder() {
        return new UpdateUserProfileRequestBuilder();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getClothingSize() { return clothingSize; }
    public void setClothingSize(String clothingSize) { this.clothingSize = clothingSize; }
}
