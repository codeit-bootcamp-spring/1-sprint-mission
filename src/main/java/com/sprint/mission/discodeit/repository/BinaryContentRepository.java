package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;

public interface BinaryContentRepository {
  BinaryContent save(BinaryContent binaryContent);
  List<BinaryContent> saveMultipleBinaryContent(List<BinaryContent> binaryContents);
  Optional<BinaryContent> findById(String id);

  List<BinaryContent> findAll();
  List<BinaryContent> findByUserId(String userId);
  List<BinaryContent> findByChannel(String channelId);
  List<BinaryContent> findByMessageId(String messageId);
  void deleteByUserId(String userId);
  void deleteByMessageId(String messageId);
  void deleteById(String id);
  void clear();
}
