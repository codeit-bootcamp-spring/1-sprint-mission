package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserStatusCreate;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
public class UserStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UserStatusType userstatusType;
    private Instant lastActiveAt;

    public UserStatus(UserStatusCreate dto) {
        super();
        this.userId = dto.userId();
        this.userstatusType = dto.type();
        this.lastActiveAt = dto.lastActiveAt();
    }
    public void update(UserStatusUpdate dto) {
        if(dto.type() == null&& isOnline()){
            this.lastActiveAt = dto.lastActiveAt();
            this.userstatusType = UserStatusType.ONLINE;
        } else if (dto.type() == null && !isOnline()) {
            this.userstatusType = UserStatusType.OFFLINE;
        } else {
            this.userstatusType = dto.type();
        }

    }
}
