package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentUtils {

  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final BinaryContentStorage binaryContentStorage;

  //binaryProfile nullable 생성하기
  public BinaryContent makeNullableProfile(
      Optional<BinaryContentRequest> optionalProfileCreateRequest) {
    return optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("Profile created with ID : {} ", binaryContent.getId());
          return binaryContent;
        })
        .orElse(null);
  }


  //DB와 로컬 파일을 삭제
  public void deleteBinaryContent(UUID profileId) {
    binaryContentRepository.deleteById(profileId);
    binaryContentStorage.delete(profileId);
  }

  //유저-프로필 이미지 삭제
  public void deleteBinaryContentByUserId(UUID userId) {

    User user = userRepository.findById(userId).orElse(null);
    UUID profileId = Objects.requireNonNull(user).getProfile().getId();
    deleteBinaryContent(profileId);
  }

  //메시지-이미지 삭제
  public void deleteBinaryContentByMessageId(UUID messageId) {
    messageRepository.findById(messageId).ifPresent(message -> {
      List<BinaryContent> attachments = message.getAttachments();
      if (attachments != null) {
        attachments.forEach(attachment ->
            deleteBinaryContent(attachment.getId())
        );
      }
    });
  }


}
