package discodeit.dto.user;

import discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserDto(UUID id,
                      Instant createdAt,
                      String name,
                      String email,
                      String phoneNum,
                      Instant updatedAt,
                      boolean online) {


    public static UserDto fromDomain(User user, boolean isOnline) {
        return UserDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .name(user.getUsername())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .updatedAt(user.getUpdatedAt())
                .online(isOnline)
                .build();
    }
}