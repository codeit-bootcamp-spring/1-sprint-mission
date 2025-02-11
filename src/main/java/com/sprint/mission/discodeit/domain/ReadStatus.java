package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Entity
@Getter
@RequiredArgsConstructor
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

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
        this.id = UUID.randomUUID();
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

    public ReadStatus update(ReadStatusDto newStatus) {
        if(newStatus.createdAt() != null) { this.createdAt = newStatus.createdAt();}
        if(newStatus.updatedAt() != null) { this.updatedAt = newStatus.updatedAt();}
        if(newStatus.channelId() != null) { this.channelId = newStatus.channelId();}
        if(newStatus.userId() != null) { this.userId = newStatus.userId();}
        if(newStatus.lastReadAt() != null) { this.lastReadAt = newStatus.lastReadAt();}
        //일단 다 바꾸어 줄 수 있도록 했습니다.
        return this;
    }

    @Override
    public String toString() {
        return "ReadStatus{" + "id=" + id + ", userId=" + userId + ", channelId=" + channelId + ", lastReadAt=" + lastReadAt + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
