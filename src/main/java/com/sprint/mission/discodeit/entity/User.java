package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("userId")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final UUID id;
    @Setter
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("createdAt")
    private final Instant createdAt;
    @Setter
    @JsonProperty("updatedAt")
    private Instant updatedAt;
    @Setter
    @JsonProperty("updatedAt")
    private String email;

    private transient String password;
    private int hashedPassword;
    //직렬화 적용 안됨 -> 따로 Hash함수 적용해서 저장하고 
    //password 받은 후 Hashing 한 결과와 저장 값과 비교해서 Auth 통과할 수 있도록
    //검증 로직은 나중에 구현하자.. //AuthService


    public User(String userName, String password, String email) {
        this.id = UUID.randomUUID();
        this.userName = userName;
        this.password = password;
        this.hashedPassword = Objects.hashCode(password);
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.email = email;
    }
    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    } //사용처?

    public void update(String userName, String email) {
        this.userName = userName;
        this.email = email;
        updatedAt = Instant.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체라면 true
        if (o == null || getClass() != o.getClass()) return false; // null이거나 클래스가 다르면 false
        User user = (User) o;
        //수정했는데 확인해봐야할 듯
        return  Objects.equals(userName, user.userName) || user.email.equals(email); // 모든 필드를 비교
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email, createdAt, updatedAt); // 비교 대상 필드 기반 hashCode 생성
    }
}
