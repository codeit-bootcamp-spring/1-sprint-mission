package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final EntityValidator validator;

  @Override
  public BinaryContent create(BinaryContent content) {

    validator.findOrThrow(User.class, content.getUserId(), new UserNotFoundException());

    // 기존에 존재하던 프로필 삭제. 그냥 교채로 바꿀수도?
    if (content.isProfilePicture()) {
      BinaryContent originalProfile = binaryContentRepository.findByUserId(content.getUserId()).stream().filter(BinaryContent::isProfilePicture).findFirst().orElse(null);
      if (originalProfile != null) {
        binaryContentRepository.deleteById(originalProfile.getUUID());
      }
    }

    return binaryContentRepository.save(content);
  }

  @Override
  public BinaryContent find(String id) {
    return binaryContentRepository.findById(id).orElseThrow(
        () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
    );
  }

  @Override
  public List<BinaryContent> findByMessageId(String messageId) {
    return binaryContentRepository.findByMessageId(messageId);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<String> ids) {
    List<BinaryContent> contents = ids.stream().map(
        id -> binaryContentRepository.findById(id).orElseThrow(() -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE))
    ).toList();

    return contents;
  }

  @Override
  public Map<String, BinaryContent> mapUserToBinaryContent(Set<String> userIds) {

    if (userIds == null || userIds.isEmpty()) return Collections.emptyMap();

    List<BinaryContent> profiles = binaryContentRepository.findProfilesOf(userIds);

    return profiles.stream()
        .collect(Collectors.toMap(BinaryContent::getUserId, content -> content));
  }

  @Override
  public void delete(String id) {
    binaryContentRepository.deleteById(id);
  }

  @Override
  public void deleteByMessageId(String messageId) {
    binaryContentRepository.deleteByMessageId(messageId);
  }

  @Override
  public List<BinaryContentDto> saveBinaryContentsForMessage(CreateMessageDto messageDto, String messageId) {
    validator.findOrThrow(Message.class, messageId, new MessageNotFoundException());

    if (messageDto.binaryContent() == null || messageDto.binaryContent().isEmpty()) {
      return Collections.emptyList();
    }

    List<BinaryContentDto> binaryContentDtos = messageDto.binaryContent();
    List<BinaryContent> binaryContents = new ArrayList<>();
    for (BinaryContentDto binary : binaryContentDtos) {
      BinaryContent content = new BinaryContent.BinaryContentBuilder(
          messageDto.userId(),
          binary.fileName(),
          binary.fileType(),
          binary.fileSize(),
          binary.data()
      )
          .messageId(messageId)
          .channelId(messageDto.channelId())
          .build();

      binaryContents.add(content);
    }

    binaryContentRepository.saveMultipleBinaryContent(binaryContents);
    return binaryContentDtos;
  }

  @Override
  public List<BinaryContent> updateBinaryContentForMessage(Message message, String userId, List<BinaryContentDto> binaryContentDtos) {
    List<BinaryContent> originalBinaryContent = binaryContentRepository.findByMessageId(message.getUUID());

    // 기존 파일이 있는지 확인
    for (BinaryContentDto dto : binaryContentDtos) {
      BinaryContent existingContent = originalBinaryContent.stream()
          .filter(content -> content.getFileName().equals(dto.fileName()))
          .findFirst()
          .orElse(null);

      // 없다면 새 파일 추가
      if (existingContent == null) {
        BinaryContent newBinaryContent = new BinaryContent.BinaryContentBuilder(userId, dto.fileName(), dto.fileType(), dto.fileSize(), dto.data())
            .messageId(message.getUUID())
            .channelId(message.getChannelUUID())
            .build();

        originalBinaryContent.add(newBinaryContent);
      }
    }

    return binaryContentRepository.saveMultipleBinaryContent(originalBinaryContent);
  }

  /**
   * 채널 내 모든 BinaryContent를 조회하고, 메시지 ID 기준으로 그룹화
   *
   * @param channelId 채널 ID
   * @return messageId를 키로 하고, 해당하는 BinaryContent 목록을 값으로 갖는 Map
   */
  @Override
  public Map<String, List<BinaryContent>> getBinaryContentsFilteredByChannelAndGroupedByMessage(String channelId) {
    return binaryContentRepository.findByChannel(channelId).stream()
        .collect(Collectors.groupingBy(
            BinaryContent::getMessageId,
            Collectors.toList()
        ));
  }
}
