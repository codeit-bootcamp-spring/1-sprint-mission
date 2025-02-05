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

    @Serial
    private static final long serialVersionUID = 1L;
    private final Instant createdAtl;
    private Instant updatedAt;
    private Instant lastAccessedAt;
    private final User user;
    private final UUID uuID;


    public UserStatus(User user){
        this.uuID = UUID.randomUUID();
        this.createdAtl = Instant.now();
        this.updatedAt = this.createdAtl;
        this.user = user;
    }

    // 유저 접속시 AccessedAt 초기화
    public void updateLastAccessed() {
        this.lastAccessedAt = Instant.now();
        this.updatedAt = this.lastAccessedAt; // updatedAt도 같이 업데이트!
    }

    // 아래의 코드는 음 비지니스 로직에 더 가까운것 같아서 음 수정을 해야 할 것 같음....
    public boolean isUserOnline() {
        if (lastAccessedAt == null) {
            return false; // 마지막 접속 기록이 없으면 오프라인, 생성하고 접속하지 않았다면 null
        }
        return Duration.between(lastAccessedAt, Instant.now()).toMinutes() < 5;
    }

}
