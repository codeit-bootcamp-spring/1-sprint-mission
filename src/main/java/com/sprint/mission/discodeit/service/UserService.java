package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.user.UserCommonRequest;
import com.sprint.mission.discodeit.entity.user.UserCommonResponse;
import com.sprint.mission.discodeit.entity.user.UserResponse;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    UserCommonResponse createUser(UserCommonRequest user); // `username`과 `email`은 다른 유저와 같으면 안됩니다, `UserStatus`를 같이 생성합니다.

    default UserCommonResponse createUserWithProfile(UserCommonRequest createDto, BinaryContent binaryContentDto) {
        return null;
    } // 선택적으로 프로필 이미지를 같이 등록할 수 있습니다.


    /**
     * - [x] 사용자의 온라인 상태 정보를 같이 포함하세요.
     * - [x] 패스워드 정보는 제외하세요.
     */
    UserResponse find(UUID userId); // 유저 찾기

    Map<UUID, UserResponse> findAll(); // 유저 전부 찾기

    UserCommonResponse update(UUID updateUserId, UserCommonRequest updateDto); // 유저 정보 업데이트

    default UserCommonResponse updateUserWithProfile(UUID userId, BinaryContentRequest binaryContent) {
        return null;
    } // 선택적으로 프로필 이미지를 대체할 수 있습니다.

    /**
     * 관련된 도메인도 같이 삭제합니다.
     * - `BinaryContent`(프로필), `UserStatus`
     */
    void deleteUser(UUID userId);

}
