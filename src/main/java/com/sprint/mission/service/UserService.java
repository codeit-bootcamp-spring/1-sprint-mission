package com.sprint.mission.service;


import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.dto.request.UserDtoForCreate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    User create(UserDtoForCreate requestDTO, MultipartFile profile);

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    User update(UUID userId, UserDtoForUpdate requestDTO);

    User findById(UUID id);
    List<User> findAll();
    void delete(UUID userId);
}
