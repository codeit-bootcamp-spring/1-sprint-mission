package com.sprint.mission.discodeit.entity.status.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.BaseObject;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserStatus extends BaseObject {
    private static final String USER_ONLINE = "online";
    private static final String USER_OFFLINE = "offline";

    @NotEmpty
    @JsonProperty("userId")
    private final UUID userId;

    private String userName;

    @Setter
    private String state;

    @Setter
    private Instant lastAccessTime;

    public static UserStatus createUserStatus(UUID userId, String userName) {
        return new UserStatus(userId, userName);
    }

    public UserStatus changeUserStatus(String userName) {
        this.userName = userName;
        updateUserStatus();

        return this;
    }

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.state = USER_ONLINE;
        updateLastAccessTime();
    }

    public UserStatus(UUID userId, String userName) {
        super(userId);
        this.userId = userId;
        this.userName = userName;
        this.state = USER_ONLINE;
        updateLastAccessTime();
    }

    public void updateUserStatus() {
        Duration betweenTime = Duration.between(lastAccessTime, Instant.now());
        changeUserStatus(betweenTime);
        updateLastAccessTime();
    }

    public void updateUserStatus(String state) {
        this.state = state;
        updateLastAccessTime();
    }

    private void changeUserStatus(Duration betweenTime) {
        if (betweenTime.toMinutes() > 5) {
            setState(USER_OFFLINE);
        } else {
            setState(USER_ONLINE);
        }
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = Instant.now();
    }

    @JsonCreator
    public UserStatus(
            @JsonProperty("id") UUID id,        // BaseObject의 id
            @JsonProperty("userId") UUID userId,   // 이 클래스의 userId
            @JsonProperty("userName") String userName,   // 이 클래스의 userId
            @JsonProperty("state") String state,
            @JsonProperty("lastAccessTime") Instant lastAccessTime
    ) {
        super(id);  // BaseObject의 id 설정
        this.userId = userId;
        this.state = state;
        this.lastAccessTime = lastAccessTime;
    }
}
