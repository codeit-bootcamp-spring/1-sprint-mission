package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class MessageMapper {

  protected UserRepository userRepository;
  protected BinaryContentRepository binaryContentRepository;
  protected BinaryContentStorage binaryContentStorage;
  private BinaryContentMapper binaryContentMapper;
  private UserMapper userMapper;

  protected MessageDto toDto(Message message) {
    // TODO 갖고있는게 userId 뿐이니까 레포지토리 이용해서 User 객체 겟
    User user = userRepository.findById(message.getUser().getId())
        .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다."));

// TODO Mapper 사용으로 간결화된 부분
//
//    Boolean online = userStatusRepository.findByUserId(message.getAuthorId())
//        .map(userStatus -> userStatus.isOnline())
//        .orElse(null);
//    BinaryContentDto profile = new BinaryContentDto(
//        user.getProfile().getId(),
//        user.getProfile().getFileName(),
//        user.getProfile().getSize(),
//        user.getProfile().getContentType(),
//        user.getProfile().getBytes()
//    );

    UserDto author = userMapper.toDto(user);

    List<BinaryContentDto> attachments = new ArrayList<>();
    List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(
        message.getAttachments().stream().map((bc) -> bc.getId()).toList());
    for (BinaryContent content : binaryContents) {
      BinaryContentDto newDto = binaryContentMapper.toDto(content);
      attachments.add(newDto);
    }

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        author,
        attachments
    );
  }
}
