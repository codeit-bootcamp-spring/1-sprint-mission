package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.FindUserResponse;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    CreateUserResponse register(CreateUserRequest user);

    FindUserResponse getUserDetails(UUID id);

    List<FindUserResponse> getAllUsers();

    void updateUserProfile(UUID id, UpdateUserRequest request);

    void deleteUser(UUID id);

}

/**
 *
 * updateUserProfile
 *
 * void 리턴
 * 실패 시 구체적인 예외 처리 가능 (UserNotFoundException, InvalidProfileException 등)
 * 트랜잭션 실패 시 자동 롤백
 * 컨트롤러에서 결과 처리가 명확함
 * 필요하다면 컨트롤러에서 업데이트된 정보를 다시 조회할 수 있음
 *
 *
 * deleteUser
 * boolean 리턴: 구체적인 실패 이유를 알기 어려움
 *
 * void 리턴:
 * 삭제는 성공/실패가 명확한 작업
 * 실패 시 예외로 처리하는 것이 자연스러움 (UserNotFoundException )
 * 삭제 후 반환할 의미 있는 데이터가 없음
 * void + 예외 처리 방식이 트랜잭션 관리에 용이
 */
