package com.sprint.mission.discodeit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Entity
@Getter
@RequiredArgsConstructor
public class ReadStatus {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private UUID channelId;

    @Setter
    private Instant lastReadAt; //유저의 마지막 수정 시각
    private Instant createdAt;

    @Setter
    private Instant updatedAt; //ReadStatus의 업데이트 시각 //필요성 의문

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = null;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateLastReadAt(Instant newReadTime) {
        this.lastReadAt = newReadTime;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus{" + "id=" + id + ", userId=" + userId + ", channelId=" + channelId + ", lastReadAt=" + lastReadAt + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
