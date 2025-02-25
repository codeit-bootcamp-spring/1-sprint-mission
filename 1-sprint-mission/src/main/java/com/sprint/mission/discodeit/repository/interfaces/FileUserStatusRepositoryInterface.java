package com.sprint.mission.discodeit.repository.interfaces;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public interface FileUserStatusRepositoryInterface {
    public void setUserStatus(UUID userId,Instant createdAt, Instant updatedAt) throws IOException, ClassNotFoundException;
    public void update(UUID id,Instant updatedAt) throws IOException, ClassNotFoundException;
    public void checkUserOnline(UUID userId) throws IOException, ClassNotFoundException;
}
