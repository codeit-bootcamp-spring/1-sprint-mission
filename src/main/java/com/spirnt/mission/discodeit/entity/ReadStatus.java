package com.spirnt.mission.discodeit.entity;

import com.spirnt.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

// 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인
// '유저'가 '채널'에서 마지막으로 읽은 '메세지' 기록
@Entity
@Table(name = "read_statuses")
@NoArgsConstructor
@Getter
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;

    @Column(name = "last_read_at")
    private Instant lastReadAt;

    private boolean notificationEnabled;

    public ReadStatus(User user, Channel channel, Instant lastReadAt, boolean notificationEnabled) {
        super();
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
        this.notificationEnabled = notificationEnabled;
    }

    public void update(Instant lastReadAt, boolean notificationEnabled) {
        this.lastReadAt = lastReadAt;
        this.notificationEnabled = notificationEnabled;
    }

    // UUID만으로 객체를 비교하기 위해 추가
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ReadStatus readStatus = (ReadStatus) obj;
        return Objects.equals(this.getId(), readStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
