package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now(); // TODO : 이 둘은 클래스 내에서 기본값으로 설정해놨으니까 객체 생성시 항상 이 값으로 같은 값.
    private Instant updatedAt; // TODO : 이렇게 초기화 안하고 선언만 하면 자동으로 null, false 같은 값으로 초기화해줌
    private final UUID userId;
    @NonNull
    private Instant lastActiveAt;

    // TODO : 롬복 말고 생성자를 명시해줬다면 이런 식이었을 것. 안에서 id, createdAT 값 넣어주는데 파라미터로는 전달 X -> 기본 고정값
//    public UserStatus(UUID userId, Instant lastActiveAt) {
//        this.id = UUID.randomUUID();
//        this.createdAt = Instant.now();
//        //
//        this.userId = userId;
//        this.lastActiveAt = lastActiveAt;
//    }

    public Boolean isOnline() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }

//    고치기 전
//    public void isOnline(){
//        if(lastActiveAt!=null && Instant.now().minusSeconds(300).isBefore(lastActiveAt)) {
//            isActive = true;
//        } else {
//            isActive = false;
//        }
//    }


    public void update(Instant lastActiveAt) {
        boolean anyValueUpdated = false;
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

}
