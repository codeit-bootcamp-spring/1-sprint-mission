package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class UserMapper {

    private UsersDto dto;
    private UserDto dtos;

    public UsersDto toDto(User user) {
        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            dto.setProfileImage(Base64.getEncoder().encodeToString(user.getProfileImage()));
        } else {
            dto.setProfileImage("");
        }

        return UsersDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .online(user.isOnline())
                .build();
    }

    public UserDto toDtos(User user) {
        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            dtos.setProfileImage(Base64.getEncoder().encodeToString(user.getProfileImage()));
        } else {
            dtos.setProfileImage("");
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .online(user.isOnline())
                .build();
    }

    private UsersDto toDTO(User user) {

        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            String base64Str = Base64.getEncoder().encodeToString(user.getProfileImage());
            dto.setProfileImage(base64Str);
        } else {
            dto.setProfileImage("");
        }

        return  UsersDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .online(user.isOnline())
                .build();
    }

}
