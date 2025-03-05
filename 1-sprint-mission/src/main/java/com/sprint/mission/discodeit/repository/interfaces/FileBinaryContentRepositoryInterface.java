package com.sprint.mission.discodeit.repository.file.interfacepac;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public interface FileBinaryContentRepositoryInterface {
    public void saveUserContent(UUID id, Instant createdAt) throws IOException, ClassNotFoundException;
    public boolean containsFile(UUID id);
}
