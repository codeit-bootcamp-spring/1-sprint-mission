package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.configure.Role;

public record UserRoleUpdateRequest(String username,
                                    Role role) {

}