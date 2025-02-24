package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;
    private String email;
    private String password;

    private boolean online;
    @Lob
    private byte[] profileImage;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID().toString(); // 직접 UUID 생성
        this.name = name;
        this.email = email;
        this.password = password;
    }

}