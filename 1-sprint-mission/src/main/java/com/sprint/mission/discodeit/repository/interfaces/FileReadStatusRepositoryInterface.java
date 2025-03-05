package com.sprint.mission.discodeit.repository.file.interfacepac;

import com.sprint.mission.discodeit.entity.User;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface FileReadStatusRepositoryInterface {
    void setStatus(Instant createdAt, Instant updatedAt, List<UUID> list, UUID userId) throws IOException, ClassNotFoundException;

    void update(UUID id ,Instant updatedAt) throws IOException, ClassNotFoundException;
    List<UUID> setUserForRead(List<UUID> list);
}
