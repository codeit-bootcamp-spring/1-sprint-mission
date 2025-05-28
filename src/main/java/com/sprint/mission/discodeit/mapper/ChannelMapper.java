package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public abstract class ChannelMapper {

  @Autowired
  protected ReadStatusRepository readStatusRepository;

  @Autowired
  protected MessageRepository messageRepository;

  @Autowired
  UserMapper userMapper;

  @Mapping(target = "participants", source = "id", qualifiedByName = "participants")
  @Mapping(target = "lastMessageAt", source = "id", qualifiedByName = "lastMessageAt")
  public abstract ChannelDto toDto(Channel channel);

  // @MappingTarget 새 인스턴스를 생성하지 않고, 대신 해당 유형의 기존 인스턴스를 업데이트하는 매핑
  @Named("participants")
  List<UserDto> participants(UUID channelId) {
    // readStatus를 통해 User을 가져온다
    return readStatusRepository.findByChannelId(channelId)
        .stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();
  }

  @Named("lastMessageAt")
  Optional<Instant> lastMessageAt(UUID channelId) { // 채널을
    // MessageRepository 에서 마지막 메세지 시간 조회
    return messageRepository.findByChannelId(channelId).stream()
        .map(Message::getCreatedAt)
        // Instant 클래스는 Comparable<Instant>를 구현 -> 시간 순서대로 정렬 가능
        .max(Comparator.naturalOrder());
  }

}
