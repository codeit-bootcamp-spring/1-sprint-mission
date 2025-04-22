package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, UUID> {
}
