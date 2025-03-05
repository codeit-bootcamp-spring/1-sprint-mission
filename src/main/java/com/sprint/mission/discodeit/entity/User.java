package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private UUID profileImageId;

    @Column(nullable = false)
    private String password;

    private boolean online;

    @Column(name = "last_active", nullable = false)
    private Instant lastActive = Instant.now();

    public User(UUID id, String username, String email, UUID profileImageId, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImageId = profileImageId;
        this.password = password;
        this.online = false;
        this.lastActive = Instant.now();
    }

    public void updateStatus(boolean online, Instant lastActive) {
        this.online = online;
        this.lastActive = lastActive;
    }
}
