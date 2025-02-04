package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("userId")
    private final UUID id;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("createdAt")
    private final long createdAt;
    @JsonProperty("updatedAt")
    private long updatedAt;

    public User(String userName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.userName = userName;
    }
    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public void update(String userName) {
        this.userName = userName;
        updatedAt = System.currentTimeMillis();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName='" + userName + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체라면 true
        if (o == null || getClass() != o.getClass()) return false; // null이거나 클래스가 다르면 false
        User user = (User) o;
        return  Objects.equals(userName, user.userName); // 모든 필드를 비교
        /*
        * createdAt == user.createdAt &&
                updatedAt == user.updatedAt &&
                Objects.equals(id, user.id) &&
        * */
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, userName, createdAt, updatedAt); // 비교 대상 필드 기반 hashCode 생성
    }
}
