package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.User;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
        private UUID id;
        private Instant createdAt;
        private Instant updatedAt;
        private String name;
        private String email;
        private UUID profileImageId;
        private UUID userStatusId;

    public static UserDTO fromDomain(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageId(user.getProfileImageId())
                .userStatusId(user.getUserStatusId())
                .build();
    }
}
