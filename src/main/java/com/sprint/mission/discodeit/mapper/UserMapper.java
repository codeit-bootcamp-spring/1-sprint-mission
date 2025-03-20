package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// UserMapper가 BinaryContentMapper를 사용 -> 메퍼라서 uses 이용
@Mapper(componentModel = "spring", uses = BinaryContentMapper.class)
public interface UserMapper {

  // DTO 객체 반환
  // UserMapper에 @Mapper(componentModel = "spring", uses = BinaryContentMapper.class) 를 지정하면
  // @Mapping(source = "profile", target = "profile") 에서
  // User 엔티티의 profile 필드를 BinaryContentMapper를 통해 BinaryContentDto로 변환하여 UserDto에 전달
  @Mapping(source = "profile", target = "profile")
  // MapStruct 문서 5.3번, 중첩된 빈 매핑 제어
  @Mapping(source = "userStatus.online", target = "online")
  UserDto toDto(User user);
}
