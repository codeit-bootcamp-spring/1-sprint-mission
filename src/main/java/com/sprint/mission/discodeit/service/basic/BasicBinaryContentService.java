package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Long create(BinaryContentDTO.request binaryContentDto) {
        return binaryContentRepository.save(new BinaryContent(binaryContentDto));
    }

    @Override
    public BinaryContentDTO.response find(Long id) {
        BinaryContent content = binaryContentRepository.load(id);
        return BinaryContentDTO.response.builder()
                .file(content.getData())
                .id(id)
                .uuid(content.getId())
                .referenceId(content.getReferenceId())
                .contentType(content.getContentType())
                .build();
    }

    @Override
    public BinaryContentDTO.response find(UUID uuid) {
        Map.Entry<Long, BinaryContent> content = binaryContentRepository.findBinaryContentByUUID(uuid);

        return BinaryContentDTO.response.builder()
                .file(content.getValue().getData())
                .id(content.getKey())
                .uuid(content.getValue().getId())
                .referenceId(content.getValue().getReferenceId())
                .contentType(content.getValue().getContentType())
                .build();
    }

    @Override
    public List<BinaryContentDTO.response> findAll() {
        return binaryContentRepository.loadAll().entrySet().stream()
                .map(entry -> BinaryContentDTO.response.builder()
                        .id(entry.getKey())
                        .file(entry.getValue().getData())
                        .uuid(entry.getValue().getId())
                        .referenceId(entry.getValue().getReferenceId())
                        .contentType(entry.getValue().getContentType())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public BinaryContentDTO.response delete(Long id) {
        BinaryContent deleteContent = binaryContentRepository.load(id);
        binaryContentRepository.delete(id);
        return BinaryContentDTO.response.builder()
                .file(deleteContent.getData())
                .id(id)
                .uuid(deleteContent.getId())
                .referenceId(deleteContent.getReferenceId())
                .contentType(deleteContent.getContentType())
                .build();
    }

    @Override
    public BinaryContentDTO.response delete(UUID uuid) {
        Map.Entry<Long, BinaryContent> deleteContent = binaryContentRepository.findBinaryContentByUUID(uuid);
        binaryContentRepository.delete(deleteContent.getKey());
        return BinaryContentDTO.response.builder()
                .file(deleteContent.getValue().getData())
                .id(deleteContent.getKey())
                .uuid(deleteContent.getValue().getId())
                .referenceId(deleteContent.getValue().getReferenceId())
                .contentType(deleteContent.getValue().getContentType())
                .build();
    }

    @Override
    public BinaryContentDTO.convert findProfileByReferenceId(UUID referenceId) {
        Map.Entry<Long, BinaryContent> content = binaryContentRepository.findProfileImageByMessageId(referenceId);
        return BinaryContentDTO.convert.builder()
                .id(content.getKey())
                .uuid(content.getValue().getId())
                .file(content.getValue().getData())
                .filename(content.getValue().getFilename())
                .mimeType(content.getValue().getMimeType())
                .build();
    }

    @Override
    public List<BinaryContentDTO.convert> findContentsByReferenceId(UUID referenceId) {
        Map<Long, BinaryContent> content = binaryContentRepository.findMessageImageByMessageId(referenceId);

        return content.entrySet().stream()
                .map(binaryContent -> BinaryContentDTO.convert.builder()
                        .id(binaryContent.getKey())
                        .uuid(binaryContent.getValue().getId())
                        .file(binaryContent.getValue().getData())
                        .filename(binaryContent.getValue().getFilename())
                        .mimeType(binaryContent.getValue().getMimeType())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
