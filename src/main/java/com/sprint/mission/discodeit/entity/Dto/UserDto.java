package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        Instant createdAt,
        Instant updatedAt,
        UUID id,
        String email,
        String userName,
        UUID profilePictureId,
        UUID userStatusId
) {

    public static UserDto from(User user) {
        return new UserDto(user.getCreatedAt(), user.getUpdatedAt(), user.getId(), user.getEmail(), user.getUserName(), user.getProfilePictureId(), user.getUserStatusId());
    }

    public record CreateUserRequest(String userName, String email, String password, UUID userStatusId) {
    }

    public record UpdateProfilePictureRequest(UUID userId, UUID profilePictureId) {}

    public record DeleteProfilePictureRequest(UUID userId) {}

    public record GetProfilePictureRequest(UUID userId) {}


    //리스폰스 dto 시험삼아 만들어봄
    public record ShowUserNameResponse(String userName) {
        public static ShowUserNameResponse from(User user) {
            return new ShowUserNameResponse(user.getUserName());
        }
    }

}

