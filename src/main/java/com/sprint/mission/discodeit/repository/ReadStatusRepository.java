package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    void deleteAllByChannelId(UUID id);

    boolean existsByUserIdAndChannelId(UUID userid, UUID channelId);

    @Query("SELECT count(r) > 0 FROM ReadStatus r WHERE r.id = :id AND r.id = :userId")
    boolean existsByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
