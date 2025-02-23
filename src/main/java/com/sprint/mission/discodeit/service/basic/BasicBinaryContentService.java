package com.sprint.mission.discodeit.service.basic;

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
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent createUserProfileFile(MultipartFile file, UUID userId) {
        BinaryContent newFile = BinaryContent.createBinaryContent(
                file.getName(), file.getContentType(), convertToBytes(file),
                BinaryContent.ParentType.USER, userId
        );
        log.info("Create User Profile : {}", newFile);
        return binaryContentRepository.save(newFile);
    }

    @Override
    public BinaryContent createMessageFile(MultipartFile file, UUID messageId) {
        BinaryContent newFile = BinaryContent.createBinaryContent(
                file.getName(), file.getContentType(), convertToBytes(file),
                BinaryContent.ParentType.MESSAGE, messageId
        );
        log.info("Create Message Files : {}", newFile);
        return binaryContentRepository.save(newFile);
    }

    @Override
    public BinaryContent updateUserProfileFile(MultipartFile file, UUID userId) {
        binaryContentRepository.deleteByUserId(userId);
        return createUserProfileFile(file, userId);
    }

    @Override
    public BinaryContent findByIdOrThrow(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.BINARY_CONTENT_NOT_FOUND, "id :" + id));
    }

    @Override
    public BinaryContent findByUserIdOrThrow(UUID userId) {
        return binaryContentRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(ErrorCode.BINARY_CONTENT_NOT_FOUND, "userId :" + userId));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream().map(this::findByIdOrThrow).collect(Collectors.toList());
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
