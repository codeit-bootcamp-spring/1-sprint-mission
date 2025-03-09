package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.BaseUpdatableEntity;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.*;
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString @Getter
@Schema(description = "메시지 읽음 상태 정보")
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant newLastReadAt) {
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
        }
    }
}
