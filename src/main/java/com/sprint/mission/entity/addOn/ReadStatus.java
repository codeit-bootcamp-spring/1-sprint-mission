package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.BaseUpdatableEntity;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;

import static jakarta.persistence.FetchType.*;
@Entity
@EqualsAndHashCode(of = {"user", "channel"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"lastReadAt"})
@Getter
@Schema(description = "메시지 읽음 상태 정보")
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id", unique = true)
    private Channel channel;

    @NotNull
    private Instant lastReadAt;

    public void update(Instant newLastReadAt) {
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
        }
    }
}
