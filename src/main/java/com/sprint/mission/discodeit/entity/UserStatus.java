package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = null;
    @NonNull
    private boolean active = false;
    private final UUID userId;  // User 객체를 참조

    public void checkActiveStatus(){
        if(updatedAt!=null && Instant.now().minusSeconds(300).isBefore(updatedAt)) {
            active = true;
        } else {
            active = false;
        }
    }

    public void update(boolean active) {
        this.active = active;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "UserStatus{active =" + active + "}";
    }
}
