package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponse createUserProfileFile(MultipartFile file, UUID userId) {
        BinaryContent newFile = BinaryContent.createBinaryContent(
                file.getName(), file.getContentType(), convertToBytes(file),
                BinaryContent.ParentType.USER, userId
        );
        log.info("Create User Profile : {}", newFile);
        BinaryContent newBinaryContent = binaryContentRepository.save(newFile);
        return BinaryContentResponse.entityToDto(newBinaryContent);
    }

    @Override
    public BinaryContentResponse createMessageFile(MultipartFile file, UUID messageId) {
        BinaryContent newFile = BinaryContent.createBinaryContent(
                file.getName(), file.getContentType(), convertToBytes(file),
                BinaryContent.ParentType.MESSAGE, messageId
        );
        log.info("Create Message Files : {}", newFile);
        BinaryContent newBinaryContent = binaryContentRepository.save(newFile);
        return BinaryContentResponse.entityToDto(newBinaryContent);
    }

    @Override
    public BinaryContentResponse updateUserProfileFile(MultipartFile file, UUID userId) {
        binaryContentRepository.deleteByUserId(userId);
        return createUserProfileFile(file, userId);
    }

    @Override
    public BinaryContentResponse findByIdOrThrow(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.BINARY_CONTENT_NOT_FOUND, "id :" + id));
        return BinaryContentResponse.entityToDto(binaryContent);
    }

    @Override
    public BinaryContentResponse findByUserId(UUID userId) {
        BinaryContent binaryContent = binaryContentRepository.findByUserId(userId).orElse(null);
        if (binaryContent != null) {
            return BinaryContentResponse.entityToDto(binaryContent);
        }
        return null;
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(this::findByIdOrThrow)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        binaryContentRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteAllByMessageId(UUID messageId) {
        binaryContentRepository.deleteAllByMessageId(messageId);
    }

    private byte[] convertToBytes(MultipartFile imageFile) {
        try {
            return imageFile.getBytes();
        } catch (IOException e) {
            throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR, "");
        }
    }
}
