package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageQuerydslRepository {

  Slice<Message> cursorBasedPaging(UUID channelId, Instant cursor, Pageable slice);

  Long totalCount(UUID channelId, Instant cursor, Pageable slice);
}
