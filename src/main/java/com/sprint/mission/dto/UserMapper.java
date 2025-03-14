package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.User;
import org.mapstruct.*;

import static org.mapstruct.MappingInheritanceStrategy.*;

// 기본값은 EXPLICIT
// 즉, 상위 매핑 메서드나 설정을 자동으로 상속하지 않고, 하위 매퍼가 명시적으로 재정의하거나 필요한 설정을 매번 작성해줘야 함
//@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface UserMapper {

    //@Mapping(target = "online", source = "status.isOnline")
    @Mapping(target = "online", expression = "java(user.getStatus() != null ? user.getStatus().isOnline() : null)")
    UserDto toDto(User user);
//
//    User update(UserDtoForUpdate updateUserDto, @MappingTarget User user);

    User toEntityWithoutProfile(UserDtoForCreate userDto);

    User toEntityWithProfile(UserDtoForCreate userDto, BinaryContent profile);
}
