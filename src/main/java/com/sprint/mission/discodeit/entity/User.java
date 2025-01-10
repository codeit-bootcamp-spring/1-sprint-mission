package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class User {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String name;
    private String phone;
    private String password;

    public User(String name, String phone, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void update(String password) {
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt) && Objects.equals(name, user.name) && Objects.equals(phone, user.phone) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, name, phone, password);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String createdAtFormatted = createdAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";
        String updatedAtFormatted = updatedAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";

        return "User {\n" +
                "  id=" + id + ",\n" +
                "  name='" + name + "',\n" +
                "  phone='" + phone + "',\n" +
                "  password='" + password + "',\n" +
                "  createdAt=" + createdAtFormatted + ",\n" +
                "  updatedAt=" + updatedAtFormatted + "\n" +
                "}";
    }

}
