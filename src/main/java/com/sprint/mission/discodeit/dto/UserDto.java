package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDto(UUID id,String password, Instant createdAt, Instant updatedAt, String userName, String email, BinaryContentDto binaryContentDto, Integer hashedPassword) {
    public UserDto(UUID userId,String password, String userName, String email) { // user Update 시 사용
        this(userId, password, null, null, userName, email,  null,null);
    }
    public UserDto(String userName, String password, String email) { // user create 시 사용
        this(null,password,  null, null, userName, email,  null,null);
    }
    public UserDto(User user) {
        this(user.getId(),null, user.getCreatedAt(), user.getUpdatedAt(), user.getUserName(), user.getEmail(), null, user.getHashedPassword());
    }
    public UserDto(String userName, String password, String email, BinaryContentDto binaryContentDto) {
        this(null,password,  null, null, userName, email,  binaryContentDto,null);
    }

    public UserDto(User user, BinaryContentDto binaryContentDto) {
        this(user.getId(),null, user.getCreatedAt(), user.getUpdatedAt(), user.getUserName(), user.getEmail(), binaryContentDto, user.getHashedPassword());
    }
}