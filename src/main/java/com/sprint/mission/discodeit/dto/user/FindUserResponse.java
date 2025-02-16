package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class FindUserResponse {
    private final String name;
    private final String email;

    private final BinaryContent profileImage; // 선택
    private final UserStatus status; // 온라인 상태 정보
}
