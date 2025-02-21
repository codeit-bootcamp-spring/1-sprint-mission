package com.sprint.mission.service;


import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.dto.request.UserDtoForCreate;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    User create(UserDtoForCreate requestDTO, Optional<BinaryContentDto> profileDTO);

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    User update(UUID userId, UserDtoForUpdate requestDTO);

    User findById(UUID id);
    List<User> findAll();
    void delete(UUID userId);
    void isDuplicateNameEmail(String name, String email);
}
