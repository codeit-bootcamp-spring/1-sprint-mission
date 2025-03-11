package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record UserCreateDTO
    (String name, String password, String email
        , BinaryContent profile) {

}
