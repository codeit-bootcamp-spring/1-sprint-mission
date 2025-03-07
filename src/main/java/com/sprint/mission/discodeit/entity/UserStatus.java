package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_statuses")
public class UserStatus {

    @Id @GeneratedValue
    @Column(name = "user_status_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Instant lastSeen;
    private Instant createdAt;

    public UserStatus(UUID id, Instant lastSeen) {
        this.id = id;
        this.lastSeen = lastSeen;
        this.createdAt = Instant.now();
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

}