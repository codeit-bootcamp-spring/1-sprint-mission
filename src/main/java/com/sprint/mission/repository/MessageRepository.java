package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Message;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

//    @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId")
//    Slice<Message> findAllByChannelId(@Param("channelId") UUID channelId, Pageable pageable);

    void deleteAllByChannel_Id(UUID channelId);

    @EntityGraph(attributePaths = {"channel", "messageAttachments", "author"})
    @NonNull
    Optional<Message> findById(@NonNull UUID id);
    // 가져올 것 : CHANNEL이랑 BINARY

    @EntityGraph(attributePaths = {"channel", "messageAttachments", "author", "author.status"})
    Page<Message> findPagingAllByChannel_Id(UUID channelId, Pageable pageable);

    @EntityGraph(attributePaths = {"channel", "messageAttachments", "author", "author.status"})
    Window<Message> findFirst50ByChannel_IdOrderByCreatedAtDesc(UUID channelId, KeysetScrollPosition position);

    Long countByChannel_Id(UUID channelId);

    // 테스트 용
    List<Message> findAllByChannel_Id(UUID channelId);

    Optional<Message> findTop1ByChannel_IdOrderByCreatedAtDesc(UUID channelId);

    // 테스트용
}
//Message save(Message message);
//
//Optional<Message> findById(UUID id);
//List<Message> findAll();
//void delete(UUID messageId);
//
//boolean existsById(UUID messageId);