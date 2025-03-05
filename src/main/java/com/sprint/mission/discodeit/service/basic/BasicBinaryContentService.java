package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.util.EntryUtils;
import com.sprint.mission.discodeit.util.FileConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    // 파일 변환 관련 상수
    public static final String CONTENT_DISPOSITION_TYPE = "attachment";
    public static final String ZIP_FILE_EXTENSION = ".zip";

    @Override
    public Long create(BinaryContentDTO.request binaryContentDto) {
        return binaryContentRepository.save(new BinaryContent(
                binaryContentDto.referenceId(), binaryContentDto.file(), binaryContentDto.contentType(), binaryContentDto.mimeType(), binaryContentDto.filename()
        ));
    }

    @Override
    public BinaryContentDTO.response find(Long id) {
        BinaryContent content = binaryContentRepository.load(id);
        return BinaryContentDTO.response.from(EntryUtils.of(id, content));
    }

    @Override
    public BinaryContentDTO.response find(UUID uuid) {
        Map.Entry<Long, BinaryContent> content = binaryContentRepository.findBinaryContentByUUID(uuid);
        return BinaryContentDTO.response.from(content);
    }

    @Override
    public List<BinaryContentDTO.response> findAll() {
        return binaryContentRepository.loadAll().entrySet().stream()
                .map(BinaryContentDTO.response::from)
                .collect(Collectors.toList());
    }

    @Override
    public BinaryContentDTO.response delete(Long id) {
        BinaryContent deleteContent = binaryContentRepository.load(id);
        binaryContentRepository.delete(id);
        return BinaryContentDTO.response.from(EntryUtils.of(id, deleteContent));
    }

    @Override
    public BinaryContentDTO.response delete(UUID uuid) {
        Map.Entry<Long, BinaryContent> deleteContent = binaryContentRepository.findBinaryContentByUUID(uuid);
        binaryContentRepository.delete(deleteContent.getKey());
        return BinaryContentDTO.response.from(deleteContent);
    }

    @Override
    public BinaryContentDTO.convert findProfileByReferenceId(UUID referenceId) {
        Map.Entry<Long, BinaryContent> content = binaryContentRepository.findProfileImageByMessageId(referenceId);
        return BinaryContentDTO.convert.from(content);
    }

    @Override
    public List<BinaryContentDTO.convert> findContentsByReferenceId(UUID referenceId) {
        Map<Long, BinaryContent> content = binaryContentRepository.findMessageImageByMessageId(referenceId);
        return content.entrySet().stream()
                .map(BinaryContentDTO.convert::from)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<byte[]> getProfileImageAsResponse(UUID userId) throws IOException {
        BinaryContent content = binaryContentRepository.findProfileImageByMessageId(userId).getValue();

        byte[] pngBytes = FileConverter.convertToFile(content.getData(), content.getFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(content.getMimeType() != null ? MediaType.parseMediaType(content.getMimeType()) : MediaType.IMAGE_PNG);
        headers.setContentLength(pngBytes.length);
        headers.setContentDispositionFormData(CONTENT_DISPOSITION_TYPE, URLEncoder.encode(content.getFilename(), StandardCharsets.UTF_8));

        return new ResponseEntity<>(pngBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getMessageImageAsResponse(UUID messageId) throws IOException {
        List<BinaryContent> contentList = new ArrayList<>(binaryContentRepository.findMessageImageByMessageId(messageId).values());
        byte[] zipBytes = FileConverter.zipFiles(contentList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(CONTENT_DISPOSITION_TYPE, messageId + ZIP_FILE_EXTENSION);
        headers.setContentLength(zipBytes.length);

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }
}
