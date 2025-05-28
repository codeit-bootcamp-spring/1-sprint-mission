package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface UserMapper {

  default UserDto toDto(User user) {
    if (user == null) {
      return null;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return toDtoWithOnlineStatus(user, authentication);
  }

  // @Context : 매핑 메소드 실행 중, 외부의 컨텍스트 정보 매핑 로직 내부로 전달
  @Mapping(source = "profile", target = "profile")
  @Mapping(source = "roles", target = "roles")
  UserDto toDtoWithOnlineStatus(User user, @Context Authentication authentication);

  // 매핑 후에 online 필드 설정
  @AfterMapping
  default void updateOnlineStatus(User user, @MappingTarget UserDto dto,
      @Context Authentication authentication) {
    if (dto == null) {
      return;
    }
    boolean online = false;
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof CustomUserDetails) {
        CustomUserDetails currentUserDetails = (CustomUserDetails) principal;
        if (user.getId() != null && currentUserDetails.getId() != null && user.getId()
            .equals(currentUserDetails.getId())) {
          online = true;
        }
      }
    }
    dto.updateOnline(online);
  }
}
