package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.binarycontent.dto.BinaryContentResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  BinaryContentResponse find(UUID binaryContentId);

  List<BinaryContentResponse> findAll(List<String> binaryContentIds);

  ResponseEntity<?> downloadBinaryContent(UUID binaryContentId);

//  List<UploadBinaryContent> create(BinaryContentRequest request) throws IOException;
//
//  List<UploadBinaryContent> createFiles(List<MultipartFile> files, UUID messageId);
//
//  BinaryContentResponse find(String id);
//
//  BinaryContent findBinaryContentById(UUID id);
//
//  Optional<BinaryContent> findBinaryContentByUserId(UUID userId);
//
//  List<BinaryContentResponse> findAllById(UUID id);
//
//  UUID delete(UUID id);
//
//  List<BinaryContentResponse> findAll(List<String> binaryContentIds);
}
