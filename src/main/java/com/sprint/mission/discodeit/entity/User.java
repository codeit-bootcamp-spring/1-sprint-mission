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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@Builder
@Entity
@RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "2자 이상 20자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, message = "4자리 이상 입력해주세요.")
    private String password;

    private boolean online;
    @Lob
    private byte[] profileImage;

    @ManyToMany(mappedBy = "members")
    private Set<Channel> channels = new HashSet<>();

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
    }

}