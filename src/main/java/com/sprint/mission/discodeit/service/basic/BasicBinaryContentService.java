package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContent.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.global.exception.binarycontent.FileConversionException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContent save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        BinaryContent newFile = binaryContentRepository.save(BinaryContent.createBinaryContent(
            file.getOriginalFilename(),
            file.getSize(),
            file.getContentType()));

        binaryContentStorage.put(newFile.getId(), convertToBytes(file))
            .handle((result, ex) -> {
                if (ex == null) {
                    newFile.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
                    log.info("File upload success: {}", newFile.getId());
                } else {
                    newFile.updateUploadStatus(BinaryContentUploadStatus.FAILED);
                    log.error("File upload failed: {}", newFile.getId());
                }
                binaryContentRepository.save(newFile);
                return null;
            });
        return newFile;
    }

    @Override
    public BinaryContentResponse findById(UUID id) {
        BinaryContent binaryContent = findByIdOrThrow(id);
        return binaryContentMapper.entityToDto(binaryContent);
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
            .map(binaryContentMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        findByIdOrThrow(id);
        binaryContentRepository.deleteById(id);
    }

    private byte[] convertToBytes(MultipartFile imageFile) {
        try {
            return imageFile.getBytes();
        } catch (IOException e) {
            throw new FileConversionException(ErrorCode.INTERNAL_SERVER_ERROR,
                Map.of("fileName", imageFile.getOriginalFilename()));
        }
    }

    private BinaryContent findByIdOrThrow(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(() ->
            new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND,
                Map.of("id", id)));
    }
}
