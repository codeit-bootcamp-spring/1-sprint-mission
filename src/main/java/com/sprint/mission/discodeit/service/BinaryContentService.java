package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface BinaryContentService {
    BinaryContent findById(UUID id);
}
