package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    Optional<BinaryContent> findByUser(User user);
    void deleteByUser(User user);
    BinaryContent save(BinaryContent binaryContent);
    List<BinaryContent> findByMessage(Message message);
    void deleteByMessageId(Message message);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
