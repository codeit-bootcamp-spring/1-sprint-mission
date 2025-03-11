package com.sprint.mission.dto.response;

import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.BaseEntity;
import com.sprint.mission.entity.main.User;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


public record SaveUserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String email,
        UUID profileImgId) {
//    public SaveUserDto(User user) {
//        this(
//                user.getId(),
//                user.getCreatedAt(),
//                user.getUpdatedAt(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getProfile().map(BaseEntity::getId).orElse(null) // null로 처리
//        );
//    }
}
