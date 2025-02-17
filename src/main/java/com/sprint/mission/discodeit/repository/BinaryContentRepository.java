package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BinaryContentRepository extends BaseRepository<BinaryContent, String>{
  BinaryContent save(BinaryContent binaryContent);
  List<BinaryContent> saveMultipleBinaryContent(List<BinaryContent> binaryContents);
  Optional<BinaryContent> findById(String id);

  List<BinaryContent> findAll();
  List<BinaryContent> findByUserId(String userId);
  List<BinaryContent> findByChannel(String channelId);
  List<BinaryContent> findByMessageId(String messageId);
  List<BinaryContent> findProfilesOf(Set<String> users);
  BinaryContent findByUserIdAndIsProfilePictureTrue(String userId);
  void deleteByUserId(String userId);
  void deleteByMessageId(String messageId);
  void deleteById(String id);
  void clear();
}
