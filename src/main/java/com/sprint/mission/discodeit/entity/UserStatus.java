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
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = null;
    private boolean isActive = false;

    private final UUID userId;  // User 객체를 참조

    // TODO 질문 : 얘 뭘로 초기화해줘야 하지...? 왜 모범답안에는 초기화를 안해줬지
    @NonNull
    private Instant lastActiveAt;

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

    @Override
    public String toString() {
        return "UserStatus{active =" + isActive + "}";
    }

}
