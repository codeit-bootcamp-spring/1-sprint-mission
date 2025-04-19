package com.sprint.mission.repository;

import com.sprint.mission.entity.Message;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    void deleteAllByChannel_Id(UUID channelId);

    @EntityGraph(attributePaths = {"channel", "messageAttachments", "author"})
    @NonNull
    Optional<Message> findById(@NonNull UUID id);

    @EntityGraph(attributePaths = {"channel", "messageAttachments", "author", "author.status"})
    Page<Message> findPagingAllByChannel_Id(UUID channelId, Pageable pageable);

    @EntityGraph(attributePaths = {"channel", "messageAttachments", "author", "author.status"})
    Window<Message> findFirst50ByChannel_IdOrderByCreatedAtDesc(UUID channelId, KeysetScrollPosition position);

    Long countByChannel_Id(UUID channelId);
    Optional<Message> findTop1ByChannel_IdOrderByCreatedAtDesc(UUID channelId);
}
