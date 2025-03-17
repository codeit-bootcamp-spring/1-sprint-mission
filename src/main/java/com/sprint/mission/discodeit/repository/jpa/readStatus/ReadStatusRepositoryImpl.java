package com.sprint.mission.discodeit.repository.jpa.readStatus;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadStatusRepositoryImpl implements ReadStatusRepository {

  private final ReadStatusJpaRepository readStatusRepository;

  @Override
  public ReadStatus saveReadStatus(ReadStatus readStatus) {
    return readStatusRepository.save(readStatus);
  }

  @Override
  public Optional<ReadStatus> findReadStatusById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId);
  }

  @Override
  public List<ReadStatus> findAllReadStatusByUser(User user) {
    return readStatusRepository.findAllByUser(user);
  }

  @Override
  public List<ReadStatus> findAllReadStatusByChannel(Channel channel) {
    return readStatusRepository.findAllByChannel(channel);
  }

  @Override
  public void removeReadStatus(UUID readStatusId) {
    readStatusRepository.deleteById(readStatusId);
  }
}
