package com.sprint.mission.discodeit.dto.user;

public record UserCreateDTO
        (String name, String password, String email
        , String filePath) {
}
