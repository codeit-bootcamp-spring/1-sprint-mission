package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContentResponseDto create(MultipartFile file, UUID userId) throws IOException;

  BinaryContentResponseDto find(UUID fileId);

  List<BinaryContentResponseDto> findAll();

  BinaryContentResponseDto delete(UUID fileId);

  ResponseEntity<?> download(UUID fileId);
}
