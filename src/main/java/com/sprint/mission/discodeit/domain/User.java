package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String phone;
    private String password;
    private UUID profileImageId;
    private UUID userStatusId;

    public User(String name, String phone, String password, UUID profileImageId, UUID userStatusId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.profileImageId = profileImageId;
        this.userStatusId = userStatusId;
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
    public boolean equals(Object o) { // User 객체는 UUID, name, phone 3개의 필드가 동일하면 같은 유저라고 판단한다.
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone);
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
