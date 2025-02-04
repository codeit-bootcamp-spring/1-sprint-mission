package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String phone;
    private String password;

    public User(String name, String phone, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public void update(String password) {
        this.password = password;
        this.updatedAt = Instant.now();
    }

    //8자리 이상 15자리 이하 대문자 및 특수문자 하나 이상 포함해야 한다
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[\\W_])(?=.*[a-zA-Z\\d]).{8,15}$";
        return password.matches(passwordRegex);
    }

    public static boolean isValidPhone(String phoneNumber) {
        String phoneRegex = "^010-\\d{4}-\\d{4}$";
        return phoneNumber.matches(phoneRegex);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(phone);
    }

    @Override
    public String toString() {
        return "User {\n" +
                "  id=" + id + ",\n" +
                "  name='" + name + "',\n" +
                "  phone='" + phone + "',\n" +
                "  password='" + password + "',\n" +
                "  createdAt=" + createdAt + ",\n" +
                "  updatedAt=" + updatedAt + "\n" +
                "}";
    }
}
