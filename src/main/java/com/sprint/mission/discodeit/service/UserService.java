package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {


    // 사용자 생성
    UserResponseDto create(UserSignupRequestDto userReqDto, MultipartFile profile) throws IOException;

    UserResponseDto find(UUID id);

    List<UserResponseDto> findAll();

    // 사용자 정보 업데이트
    // 비밀번호랑 Active 수정은 따로 빼기
    UserResponseDto update(UUID publicUserId, UserUpdateDto request, MultipartFile updateProfile);

    // 사용자 삭제
    UserResponseDto delete(UUID id);
}
