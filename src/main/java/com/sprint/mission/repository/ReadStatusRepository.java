package com.sprint.mission.repository;

import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    Optional<ReadStatus> findByUser(User user);

    void deleteAllByChannel(Channel channel);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    List<ReadStatus> findAllByUser(User user);

    @EntityGraph(attributePaths = {"user", "channel", "user.profile", "user.status"})
    List<ReadStatus> findAllByUser_Id(UUID userId);

    @EntityGraph(attributePaths = {"user", "channel"}) // 이건 나중에 요구사항 보고 수정
    List<ReadStatus> findAllByChannel_Id(UUID channelId);

//    @EntityGraph(attributePaths = {"user", "channel"})
//    List<ReadStatus> findPagingAllByUser_Id(UUID userId);



//    @EntityGraph(attributePaths = {"user", "channel"})
//    @Query("SELECT rs FROM ReadStatus rs WHERE rs.user.id = :userId")
//    Page<ReadStatus> findWithEGByUserId(@Param("userId") UUID userId, Pageable pageable);
}
//ReadStatus save(ReadStatus readStatus);
//
//Optional<ReadStatus> findById(UUID id);
//
//List<ReadStatus> findAllByUserId(UUID userId);
//
//List<ReadStatus> findAllByChannelId(UUID channelId);
//
//boolean existsById(UUID id);
//
//void deleteById(UUID id);
//
//void deleteAllByChannelId(UUID channelId);