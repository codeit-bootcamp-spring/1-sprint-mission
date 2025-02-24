package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.common.Email;
import com.sprint.mission.discodeit.common.Name;
import com.sprint.mission.discodeit.enums.RegionCode;
import com.sprint.mission.discodeit.enums.UserType;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class UserDTO {
    @Builder
    public record request(String userName, String nickname, String email, String password, UserType userType,
                          RegionCode regionCode,
                          String phone, String introduce) {
    }

    @Builder
    public record response(Long id, UUID uuid, Name username, Name nickname, Email email, boolean online) {
    }

    @Builder
    public record update(Long id, UserDTO.request userReqDTO, MultipartFile profile) {
    }

    @Builder
    public record idResponse(Long userId, UUID uuid) {
    }
}
