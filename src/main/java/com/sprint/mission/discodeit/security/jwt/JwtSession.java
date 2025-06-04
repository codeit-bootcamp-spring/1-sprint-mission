package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession extends BaseEntity {

    private UUID userId;

    @Column(unique = true, columnDefinition = "TEXT")
    private String accessToken;

    @Column(unique = true, columnDefinition = "TEXT")
    private String refreshToken;
}
