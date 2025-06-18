package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDto createUser(UserCreateRequestDto request
            , MultipartFile profile);

    UserDto getUserById(UUID id);

    List<UserDto> getAllUsers();

    UserDto updateUser(UUID userId, UserUpdateRequestDto request
            , MultipartFile profile);

    void deleteUser(UUID id);

    BinaryContentDto saveProfileImage(MultipartFile profileFile);
}

