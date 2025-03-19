package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapperHelper.class})
@Named("UserMapper")
public interface UserMapper {

  @Mapping(source = "id", target = "online", qualifiedByName = "mapOnline")
  @Mapping(source = "profileId", target = "profile", qualifiedByName = "mapProfileById")
  UserDto toDto(User user);
}

