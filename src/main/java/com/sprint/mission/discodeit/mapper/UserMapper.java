package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface UserMapper {


    // 프로필 이미지가 null이 아닐 경우 id를 매핑
    @Mapping(target = "profileId", expression = "java(request.getProfileImage() != null ? request.getProfileImage().getId() : null)")
    User toEntity(CreateUserRequest request);

    @Mapping(target = "profileImage", source = "profileImage")
    CreateUserResponse toCreateUserResponse(User user, BinaryContent profileImage);

    @Mappings({
            @Mapping(target = "name", source = "user.name"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "profileImage", source = "profileImage"),
            @Mapping(target = "userStatus", source = "userStatus")
    })
    FindUserResponse toFindUserResponse(User user, BinaryContent profileImage, UserStatus userStatus);

    @Mappings({
            @Mapping(target = "name"),
            @Mapping(target = "password", source = "request.password")
    })
    User toLoginEntity(LoginUserRequest request);

    @Mappings({
            @Mapping(target = "name", source = "user.name"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "profileImage", source = "binaryContent")
    })
    LoginUserResponse toLoginUserResponse(
            User user,
            BinaryContent binaryContent
    );
}
