package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByChannel(Channel channel);

  void deleteByChannel(Channel channel);

  @EntityGraph(attributePaths = {"channel", "author"})
  Page<Message> findAllByChannel_Id(UUID channelId, Pageable pageable);

}
