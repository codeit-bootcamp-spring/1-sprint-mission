package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
//사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.


    @Serial
    private static final long serialVersionUID = 1L;
    private final Instant createdAtl;
    private Instant updatedAt;
    private Instant lastAccessedAt;
    private boolean status;
    private final UUID userId;
    private final UUID id;


    public UserStatus(UUID userId){
        this.id= UUID.randomUUID();
        this.createdAtl = Instant.now();
        this.updatedAt = this.createdAtl;
        this.userId = userId;
    }

    public void updateLastAccessed(Instant currentTime) {
        this.lastAccessedAt = currentTime;
        this.updatedAt = this.lastAccessedAt; // updatedAt도 같이 업데이트!
    }


    private boolean isUserOnline(Instant currentTime) {
        if (lastAccessedAt == null) {
            return false; //
        }
        // 5분 이내면 true, 5분이 지났으면 false
        return Duration.between(lastAccessedAt, currentTime).toMinutes() < 5;
    }

    public void statusUpdate(Instant currentTime){
        status = isUserOnline(currentTime);
    }

}
