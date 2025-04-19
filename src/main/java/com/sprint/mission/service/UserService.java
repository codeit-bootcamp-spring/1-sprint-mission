package com.sprint.mission.service;


import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.User;
import com.sprint.mission.dto.request.UserDtoForCreate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    User create(UserDtoForCreate requestDTO, MultipartFile profile);
    User update(UUID userId, UserDtoForUpdate requestDTO);
    User findById(UUID id);
    List<User> findAll();
    void delete(UUID userId);
}
