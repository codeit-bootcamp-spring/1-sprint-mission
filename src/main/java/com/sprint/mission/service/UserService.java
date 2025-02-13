package com.sprint.mission.service;


import com.sprint.mission.entity.main.User;
import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.dto.response.FindUserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    User create(UserDtoForRequest dto) ;
    void update(UUID userId, UserDtoForRequest dto);
    User findById(UUID id);
    List<User> findAll();
    void delete(UUID userId);
    void isDuplicateNameEmail(String name, String email);
}
