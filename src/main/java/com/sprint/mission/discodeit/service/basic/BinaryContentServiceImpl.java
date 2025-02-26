package com.sprint.mission.discodeit.service.basic;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
  public List<BinaryContent> saveBinaryContentsForMessage(String messageId, List<BinaryContent> contents) {

    validator.findOrThrow(Message.class, messageId, new MessageNotFoundException());

    if (contents == null || contents.isEmpty()) {
      return Collections.emptyList();
    }

    return binaryContentRepository.saveMultipleBinaryContent(contents);
  }

  // TODO : 메시지 업데이트시 이미지는 없을 수도
  @Override
  public List<BinaryContent> updateBinaryContentForMessage(Message message, String userId, List<BinaryContent> newFiles) {

    List<BinaryContent> originalFiles = binaryContentRepository.findByMessageId(message.getUUID());

    Set<String> newFileNames = newFiles.stream()
        .map(BinaryContent::getFileName)
        .collect(Collectors.toSet());

    List<BinaryContent> filesToDelete = originalFiles.stream()
        .filter(file -> !newFileNames.contains(file.getFileName()))
        .toList();

    // 기존 파일 삭제
    filesToDelete.forEach(file -> binaryContentRepository.deleteById(file.getUUID()));

    return binaryContentRepository.saveMultipleBinaryContent(newFiles);
  }

  @Override
  public BinaryContent updateProfile(String userId, BinaryContent profileImage) {
    BinaryContent originalProfile = binaryContentRepository.findByUserIdAndIsProfilePictureTrue(userId);

    return Optional.ofNullable(originalProfile)
        .map(profile -> {
          binaryContentRepository.deleteById(profile.getUUID());
          return binaryContentRepository.save(profileImage);
        })
        .orElseGet(() -> binaryContentRepository.save(profileImage));
  }

  /**
   * 채널 내 모든 BinaryContent를 조회하고, 메시지 ID 기준으로 그룹화
   *
   * @param channelId 채널 ID
   * @return messageId를 키로 하고, 해당하는 BinaryContent 목록을 값으로 갖는 Map
   */
  // TODO : null 처리
  @Override
  public Map<String, List<BinaryContent>> getBinaryContentsFilteredByChannelAndGroupedByMessage(String channelId) {
    return binaryContentRepository.findByChannel(channelId).stream()
        .collect(Collectors.groupingBy( content ->
            content.getMessageId() != null ? content.getMessageId() : "",
            Collectors.toList()
        ));
  }

  @Override
  public List<BinaryContent> findAll(){
    return binaryContentRepository.findAll();
  }
}
