package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final MessageAttachmentRepository messageAttachmentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public ResponseEntity<Resource> download(String id) {
    ResponseEntity<?> response = null;

    BinaryContent targetContent = binaryContentRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new DiscodeitException(ErrorCode.IMAGE_NOT_FOUND));

    BinaryContentDto dto = binaryContentMapper.toDto(targetContent);

    try {
      log.info("[DOWNLOAD START] : [ID : {}]", id);
      response = binaryContentStorage.download(dto);
    } catch (IOException e) {
      throw new FileException(ErrorCode.ERROR_WHILE_DOWNLOADING, Map.of("fileId", id));
    }

    if (response.getStatusCode().is3xxRedirection()) {
      log.info("[REDICRECTION] : {}", response.getHeaders().getLocation());
      return ResponseEntity.status(response.getStatusCode())
          .headers(response.getHeaders())
          .build();
    }

    if (response.getBody() instanceof Resource resource) {
      log.debug("[SUCCESSFULLY FETCHED RESOURCE] : [ID : {}]", id);
      return ResponseEntity.status(response.getStatusCode())
          .headers(response.getHeaders())
          .body(resource);
    }

    log.warn("[FAILED TO RETURN RESOURCE] : [ID : {}]", id);
    throw new FileException(ErrorCode.FILE_ERROR, Map.of("fildId", id));

  }

  @Override
  public BinaryContent save(BinaryContent content, byte[] bytes) {
    content.changeUploadStatus(UploadStatus.WAITING);
    BinaryContent savedContent = binaryContentRepository.save(content);
    //binaryContentRepository.flush();

    log.debug("[SAVED BINARY METADATA] : [ID: {}]", savedContent.getId());
//    binaryContentStorage.put(savedContent.getId(), bytes);
//    log.debug("[SAVED IMAGE TO STORAGE] : [ID: {}]", savedContent.getId());

    return savedContent;
  }

  @Override
  public List<BinaryContent> saveBinaryContents(List<BinaryContent> contents,
      List<MultipartFile> files) {

    if (contents == null || contents.isEmpty()) {
      return Collections.emptyList();
    }

    List<BinaryContent> savedContents = binaryContentRepository.saveAll(contents);

    //binaryContentRepository.flush();

    log.debug("[SAVED METADATA FOR FILES]");
//    log.debug("[WRITING FILE...]");
//    for (MultipartFile file : files) {
//      try {
//        BinaryContent content = contents.stream()
//            .filter(c -> Objects.equals(c.getFileName(), file.getOriginalFilename())).findFirst()
//            .orElseThrow();
//        binaryContentStorage.put(content.getId(), file.getBytes());
//      } catch (IOException e) {
//        log.warn("[ERROR WHILE WRITING FILE] : [FILE_NAME : {}]", file.getOriginalFilename());
//        throw new FileException(ErrorCode.FILE_ERROR,
//            Map.of("fileName", file.getOriginalFilename()));
//      }
//    }
    return savedContents;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public BinaryContent update(BinaryContent content) {
    return binaryContentRepository.save(content);
  }


  @Override
  public BinaryContent find(String id) {
    return binaryContentRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new DiscodeitException(ErrorCode.IMAGE_NOT_FOUND, Map.of("fileId", id)));

  }

  @Override
  public List<BinaryContent> findByMessageId(String messageId) {

    List<MessageAttachment> attachments = messageAttachmentRepository.findByMessageIdWithAttachments(
        UUID.fromString(messageId));
    List<BinaryContent> contents = attachments.stream()
        .map(attachment -> attachment.getAttachment()).collect(Collectors.toList());

    return (contents == null || contents.isEmpty())
        ? Collections.emptyList()
        : Collections.unmodifiableList(contents);

  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<String> ids) {
    List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
    return Collections.unmodifiableList(binaryContentRepository.findAllById(uuids));
  }

  @Override
  public void delete(String id) {
    binaryContentRepository.deleteById(UUID.fromString(id));
    log.debug("[DELETED BINARY CONTENT METADATA]: [ID: {}]", id);
  }

  @Override
  public void deleteByMessageId(String messageId) {

    List<UUID> contents = messageAttachmentRepository.findByMessageId(UUID.fromString(messageId));
    binaryContentRepository.deleteAllById(contents);
  }

//  @Override
//  public Map<String, List<BinaryContent>> getBinaryContentsFilteredByChannelAndGroupedByMessage(String channelId) {
//    return binaryContentRepository.findByChannel(channelId).stream()
//        .collect(Collectors.groupingBy( content ->
//            content.getMessageId() != null ? content.getMessageId() : "",
//            Collectors.toList()
//        ));
//  }

  @Override
  public List<BinaryContent> findAll() {
    return binaryContentRepository.findAll();
  }
}
