package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * - 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현
 * - 사용자 별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "read_statuses",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_channel", columnNames = {"user_id", "channel_id"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "fk_channel"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;

    @Column(name = "last_read_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadAt() {    // 읽을 시 시간 갱신을 위함
        this.lastReadAt = Instant.now();
    }

    public void updateLastReadAt(Instant lastReadAt) {    // 읽을 시 시간 갱신을 위함
        this.lastReadAt = lastReadAt;
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "lastReadAt=" + lastReadAt +
                '}';
    }
}
