package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.common.Email;
import com.sprint.mission.discodeit.common.Name;
import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public class UserDTO {
    @Builder
    public record request(String userName, String nickname, String email, String password, User.UserType userType,
                          Phone.RegionCode regionCode,
                          String phone, String introduce) {
    }

    @Builder
    public record response(Long id, UUID uuid, Name username, Name nickname, Email email, boolean online) {
        public static response from(Map.Entry<Long, User> userEntry, boolean isOnline) {
            User user = userEntry.getValue();
            return UserDTO.response.builder()
                    .id(userEntry.getKey())
                    .uuid(user.getId())
                    .username(user.getUserName())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .online(isOnline)
                    .build();
        }
    }

    @Builder
    public record update(Long id, UserDTO.request userReqDTO, MultipartFile profile) {
        public static update of(Long id, UserDTO.request userReqDTO, MultipartFile profile) {
            return update.builder()
                    .id(id)
                    .userReqDTO(userReqDTO)
                    .profile(profile)
                    .build();
        }
    }
}
