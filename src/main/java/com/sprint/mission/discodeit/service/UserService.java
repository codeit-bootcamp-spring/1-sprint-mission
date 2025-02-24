package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // 유일한 지 찾는거

    // 사용자 생성
    UserDTO.idResponse create(UserDTO.request userReqDto, MultipartFile profile);

    // 사용자 조회
    UserDTO.response find(Long id);

    UserDTO.response find(UUID uuid);

    List<UserDTO.response> findAll();

    // 사용자 정보 업데이트
    // 비밀번호랑 Active 수정은 따로 빼기
    UserDTO.idResponse update(UserDTO.update updateDTO);

    // 사용자 삭제
    UserDTO.idResponse delete(Long id);

    UserDTO.idResponse delete(UUID uuid);
}
