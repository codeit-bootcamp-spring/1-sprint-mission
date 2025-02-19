package com.sprint.mission.discodeit.entity.status.user;

import com.sprint.mission.discodeit.entity.BaseObject;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다.<br/>
 * 사용자의 온라인 상태를 확인하기 위해 활용합니다.
 * <p>
 * TODO: 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.<br/>
 * TODO: 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
 */

@Getter
public class UserStatus extends BaseObject {
    private static final String USER_ONLINE = "online";
    private static final String USER_OFFLINE = "offline";

    @NotEmpty
    private final UUID userId;

    @Setter
    private String state;

    @Setter
    private Instant lastAccessTime;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.state = USER_ONLINE;
        updateLastAccessTime();
    }

    public void updateUserStatus() {
        Duration betweenTime = Duration.between(lastAccessTime, Instant.now());
        changeUserStatus(betweenTime);
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
}
