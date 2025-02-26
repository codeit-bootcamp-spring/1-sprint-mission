package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.binarycontent.UploadBinaryContent;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  List<UploadBinaryContent> create(BinaryContentRequest request) throws IOException;

  List<UploadBinaryContent> createFiles(List<MultipartFile> files, UUID messageId);

  BinaryContentResponse find(String id);

  BinaryContent findBinaryContentById(UUID id);

  Optional<BinaryContent> findBinaryContentByUserId(UUID userId);

  List<BinaryContentResponse> findAllById(UUID id);

  UUID delete(UUID id);

  List<BinaryContentResponse> findAll(List<String> binaryContentIds);
}
