package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;

//@Repository
@NoArgsConstructor
//@JCFRepositoryCondition
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> readStatusData = new HashMap<>();

  @Override
  public ReadStatus saveReadStatus(ReadStatus readStatus) {
    readStatusData.put(readStatus.getId(), readStatus);
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findReadStatusById(UUID readStatusId) {
    return Optional.ofNullable(readStatusData.get(readStatusId));
  }

  public Optional<ReadStatus> findReadStatusByUserId(UUID userId) {
    return readStatusData.values().stream()
        .filter(readStatus -> readStatus.getUser().getId().equals(userId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findAllReadStatusByUser(User user) {
    return readStatusData.values().stream()
        .filter(readStatus -> readStatus.getUser().equals(user))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllReadStatusByChannel(Channel channel) {
    return readStatusData.values().stream()
        .filter(readStatus -> readStatus.getChannel().equals(channel))
        .toList();
  }

  @Override
  public void removeReadStatus(UUID readStatusId) {
    readStatusData.remove(readStatusId);
  }
}
