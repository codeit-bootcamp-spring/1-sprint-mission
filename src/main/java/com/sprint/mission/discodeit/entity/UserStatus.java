package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
@Entity
@Data
@RequiredArgsConstructor
public class UserStatus {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private Instant createdAt;
    @Setter
    private Instant updatedAt;

    private Status status;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = Status.ONLINE;
    }
    public UserStatus updateFields(UserStatusDto userStatusDto) { //현재 시각이던 과거 시간이던 필드 업데이트
        if(userStatusDto.updatedAt() != null) { this.updatedAt = userStatusDto.updatedAt();}
        if (this.updatedAt != null) { 
            Instant fiveMinutesAgo = Instant.now().minusSeconds(300);
            this.status = this.updatedAt.isBefore(fiveMinutesAgo) ? Status.OFFLINE : Status.ONLINE;
        }       
        return this;
    }

    public UserStatus checkStatus() { //Status 상태 계산 후 반환
        if (this.updatedAt != null) {
            Instant fiveMinutesAgo = Instant.now().minusSeconds(300); //300 에서 test 확인을 위해 1으로 변경
            //Instant fiveMinutesAgo = Instant.now().minusSeconds(300);
            this.status = this.updatedAt.isBefore(fiveMinutesAgo) ? Status.OFFLINE : Status.ONLINE;
        }
        return this;
    }
}
