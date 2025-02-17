package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.sprint.mission.discodeit.constant.BinaryContentConstant.DEFAULT_PROFILE_PICTURE_NAME;
import static com.sprint.mission.discodeit.constant.BinaryContentConstant.DEFAULT_PROFILE_PICTURE_UUID;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf",  matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository{

  private final Map<String, BinaryContent> data = new ConcurrentHashMap<>();

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    data.put(binaryContent.getUUID(), binaryContent);
    return binaryContent;
  }


  @Override
  public List<BinaryContent> saveMultipleBinaryContent(List<BinaryContent> binaryContents) {
    binaryContents.stream()
        .forEach(binaryContent -> data.put(binaryContent.getUUID(), binaryContent));
    return binaryContents;
  }

  @Override
  public Optional<BinaryContent> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<BinaryContent> findAll() {
    return data.values().stream().toList();
  }

  @Override
  public List<BinaryContent> findByUserId(String userId) {
    return data.values().stream()
        .filter(content -> content.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<BinaryContent> findByChannel(String channelId) {
    return data.values().stream()
        .filter(content -> Objects.equals(content.getChannelId(), channelId))
        .toList();
  }

  @Override
  public List<BinaryContent> findByMessageId(String messageId) {
    return data.values().stream()
        .filter(content -> Objects.equals(content.getMessageId(), messageId))
        .toList();
  }

  @Override
  public List<BinaryContent> findProfilesOf(Set<String> users) {
    return data.values().stream()
        .filter(content -> content.isProfilePicture() && users.contains(content.getUserId())).toList();
  }

  @Override
  public BinaryContent findByUserIdAndIsProfilePictureTrue(String userId) {
    return data.values().stream()
        .filter(content -> Objects.equals(userId, content.getUserId()) && content.isProfilePicture())
        .findFirst().orElse(null);
  }

  @Override
  public void deleteByUserId(String userId) {
    data.values().removeIf(content -> content.getUserId().equals(userId));
  }

  @Override
  public void deleteByMessageId(String messageId) {
    data.values().removeIf(content -> Objects.equals(content.getMessageId(), messageId));
  }

  @Override
  public void deleteById(String id) {
    data.remove(id);
  }

  @Override
  public void clear() {
    data.clear();
  }
}
