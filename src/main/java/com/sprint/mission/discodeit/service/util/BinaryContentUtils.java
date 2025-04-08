package com.sprint.mission.discodeit.service.util;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class BinaryContentUtils {

  /**
   * 실제로 프로필 이미지 저장
   */
  public static void saveProfileImg(MultipartFile file, User savedMember) throws IOException {
    if (file != null && savedMember.getProfile() != null) {
      BinaryContentStorage binaryContentStorage = SpringContextUtils.getBean(
          BinaryContentStorage.class);

      binaryContentStorage.put(savedMember.getProfile().getId(), file.getBytes());
      log.info("profile image 저장: {}", file.getOriginalFilename());
    }
  }

  /**
   * 프로필 객체 생성
   */
  public static BinaryContent getProfile(MultipartFile file) {
    BinaryContent bin = null;
    if (file != null) {
      bin = BinaryContent.builder()
          .fileName(file.getOriginalFilename())
          .size(file.getSize())
          .contentType(file.getContentType())
          .build();

      bin.changeCreateAt(Instant.now());
    }

    return bin;
  }
}
