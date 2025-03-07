package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@Builder
@AllArgsConstructor
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private UUID profileImageId;

    protected User() { } // JPA 기본 생성자

    public User(String username, String password, String email, String phoneNumber){
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
