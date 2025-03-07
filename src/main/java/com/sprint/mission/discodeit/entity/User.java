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

    @ManyToMany(mappedBy = "channel_id")
    private List<Channel> channels = new ArrayList<>();


}