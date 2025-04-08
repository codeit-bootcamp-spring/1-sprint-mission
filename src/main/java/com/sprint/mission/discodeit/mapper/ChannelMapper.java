package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMapper {

  protected MessageRepository messageRepository;
  protected ReadStatusRepository readStatusRepository;
  private UserRepository userRepository;
  private UserMapper userMapper; // 이렇게 하니까 BinaryContentDto 타입의 profile을 new로 생성하고, 그 profile이 들어간 User타입의 participants를 또 생성하고 이를 위해 또 online 생성하고..하는 과정이 한줄로 간결화 됨.

  public ChannelDto toDto(Channel channel) {
    // 1. lastMessageAt과 participantIds 포함한 채널dto 값 반환
    // 2. lastMessageAt과 participantIds 정의
    // 3. private만 participantIds 포함하도록

    Instant lastMessageAt = Instant.MIN; // 초기값은 가장 과거의 시간인 .MIN으로 초기화
    Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
    Page<Message> msgs = messageRepository.findAllByChannelId(channel.getId(), pageable);
    
    if (!msgs.isEmpty()) {
      // 가장 최근 메시지를 가져오고, 그 메시지의 createdAt 값을 lastMessageAt에 설정
      lastMessageAt = msgs.getContent().get(0).getCreatedAt();
    }

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .forEach(readStatus -> participantIds.add(readStatus.getUser().getId()));
      // .forEach()는 Iterable에서 제공하는 메서드라서 .stream() 안써도 된다.

    }
    ReadStatus readStatus = readStatusRepository.findById(channel.getId())
        .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다."));
    User user = userRepository.findById(readStatus.getUser().getId())
        .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다."));


//    Boolean online = userStatusRepository.findByUserId(user.getId())
//        .map(
//            userStatus -> userStatus.isOnline())
//        .orElse(null);
//
//    BinaryContentCreateRequest profile = new BinaryContentCreateRequest(
//        user.getProfile().getId(),
//        user.getProfile().getFileName(),
//        user.getProfile().getSize(),
//        user.getProfile().getContentType()
//    );
//
//    UserDto participants = new UserDto(
//        user.getId(),
//        user.getUsername(),
//        user.getEmail(),
//        profile,
//        online
//    );
    UserDto participants = userMapper.toDto(user); // 위 주석들이 이렇게 한줄로 간결화.

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        Collections.singletonList(participants),
        lastMessageAt 
    );
  }
}
