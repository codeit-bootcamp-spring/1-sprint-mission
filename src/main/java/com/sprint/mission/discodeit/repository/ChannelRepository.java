package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    boolean existsById(UUID id);


    @EntityGraph(attributePaths = {"readStatuses", "messages", "readStatuses.user",
            "readStatuses.user.profile"})
    @Query("""
            select c from Channel c
            where c.type = 'PUBLIC'
            or c.id in (
                select rs.channel.id from ReadStatus rs where rs.user.id = :userId
            )
            """)
    List<Channel> findAllByUserId(UUID userId);
}
