package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Page<Message> findByChannel_Id(UUID channelId, Pageable pageable);

  @Override
  @NonNull
  @EntityGraph(attributePaths = {"channel", "author", "author.profile", "attachments",
      "author.status"})
  Optional<Message> findById(@NonNull UUID uuid);

  @Query("select m from Message m"
      + " join fetch m.channel c"
      + " join fetch m.author a"
      + " join fetch a.profile p"
      + " join fetch a.status st"
      + " left join fetch m.attachments at"
      + " where m.channel.id = :channelId"
      + " and m.createdAt > :cursor order by m.createdAt desc")
  Slice<Message> findAllByChannelIdWithCursor(@Param("channelId") UUID ChannelId,
      @Param("cursor") Instant cursor, Pageable slice);

  @EntityGraph(attributePaths = {"attachments"})
  @Override
  void deleteById(@NonNull UUID messageId);
}
