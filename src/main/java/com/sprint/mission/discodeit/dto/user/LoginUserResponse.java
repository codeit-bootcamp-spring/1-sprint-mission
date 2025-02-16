package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserResponse {
    private final String name;
    private final String email;

    private final BinaryContent profileImage;
}
