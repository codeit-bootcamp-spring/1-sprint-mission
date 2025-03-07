package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.aspectj.bridge.IMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter @Setter
@Entity @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private UUID id;

    private String name;

    private String email;

    private String password;

    private boolean online;
    @Lob
    private byte[] profileImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadStatus> readStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStatus> userStatuses = new ArrayList<>();


    public void addUserStatus(UserStatus status) {
        userStatuses.add(status);
        status.setUser(this);
    }

    public void removeUserStatus(UserStatus status) {
        userStatuses.remove(status);
        status.setUser(null);
    }
}