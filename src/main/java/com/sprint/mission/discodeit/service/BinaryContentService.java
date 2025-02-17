package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BinaryContentService {
  BinaryContent create(BinaryContent content);
  BinaryContent find(String id);
  List<BinaryContent> findByMessageId(String messageId);
  List<BinaryContent> findAllByIdIn(List<String> ids);
  Map<String, BinaryContent> mapUserToBinaryContent(Set<String> userIds);
  void delete(String id);
  void deleteByMessageId(String messageId);
  List<BinaryContent> saveBinaryContentsForMessage(String messageId, List<BinaryContent> contents);
  List<BinaryContent> updateBinaryContentForMessage(Message message, String userId, List<MultipartFile> binaryContentDtos);
  BinaryContent updateProfile(String userId, BinaryContent profileImage);
  Map<String, List<BinaryContent>> getBinaryContentsFilteredByChannelAndGroupedByMessage(String channelId);

  List<BinaryContent> findAll();
}
