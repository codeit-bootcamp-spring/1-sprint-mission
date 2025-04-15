package com.sprint.mission.repository;

import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    void deleteAllByChannel(Channel channel);

    @EntityGraph(attributePaths = {"user", "channel", "user.profile", "user.status"})
    List<ReadStatus> findAllByUser_Id(UUID userId);

    @EntityGraph(attributePaths = {"user", "channel"}) // 이건 나중에 요구사항 보고 수정
    List<ReadStatus> findAllByChannel_Id(UUID channelId);

    @EntityGraph(attributePaths = {"user", "channel"})
    @NonNull
    Optional<ReadStatus> findById(@NonNull UUID id);
}