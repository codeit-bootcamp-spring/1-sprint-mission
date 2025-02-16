package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

    @Mappings({
            @Mapping(target = "id" ),
            @Mapping(target = "userId"),
            @Mapping(target = "status"),
            @Mapping(target = "updatedAt")
    })
    UserStatusResponse toUserStatusResponse(UserStatus userStatus);

    List<UserStatusResponse> toUserStatusResponseList(List<UserStatus> userStatuses);

    UserStatus toEntity(CreateUserStatusRequest request);
}
