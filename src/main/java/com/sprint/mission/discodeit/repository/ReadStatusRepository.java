package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

    ReadStatus saveReadStatus(ReadStatus readStatus);

    Optional<ReadStatus> findReadStatusById(UUID readStatusId);

    List<ReadStatus> findAllReadStatusByUser(User user);

    List<ReadStatus> findAllReadStatusByChannel(Channel channel);

    void removeReadStatus(UUID readStatusId);
}
