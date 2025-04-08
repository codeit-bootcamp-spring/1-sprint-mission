package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID>, MessageQuerydslRepository {

  Page<Message> findByChannel_Id(UUID channelId, Pageable pageable);

  @Override
  @NonNull
  @EntityGraph(attributePaths = {"channel", "author", "author.profile", "attachments",
      "author.status"})
  Optional<Message> findById(@NonNull UUID uuid);

  @EntityGraph(attributePaths = {"attachments"})
  @Override
  void deleteById(@NonNull UUID messageId);
}
