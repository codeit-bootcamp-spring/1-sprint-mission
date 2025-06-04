package com.sprint.mission.discodeit.storage;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

public interface BinaryContentStorage {

	CompletableFuture<UUID> put(UUID binaryContentId, byte[] bytes);

	InputStream get(UUID binaryContentId);

	ResponseEntity<?> download(BinaryContentDto metaData);
}
