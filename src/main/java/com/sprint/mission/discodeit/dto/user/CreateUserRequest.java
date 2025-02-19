package com.sprint.mission.discodeit.dto.user;
import java.util.UUID;

public record CreateUserRequest (String name, String phone, String password, UUID profileId) {
}
