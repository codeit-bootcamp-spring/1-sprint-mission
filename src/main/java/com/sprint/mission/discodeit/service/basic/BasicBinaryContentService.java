package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
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
        log.info("Create User Profile : {}", newFile.getId());
        return binaryContentRepository.save(newFile);
    }

    @Override
    public BinaryContent createMessageFile(MultipartFile file, UUID messageId) {
        BinaryContent newFile = BinaryContent.createBinaryContent(
                file.getName(), file.getContentType(), convertToBytes(file),
                BinaryContent.ParentType.MESSAGE, messageId
        );
        log.info("Create Message file : {}", newFile.getId());
        return binaryContentRepository.save(newFile);
    }

    @Override
    public BinaryContent updateUserProfileFile(MultipartFile file, UUID userId) {
        binaryContentRepository.deleteByUserId(userId);
        return createUserProfileFile(file, userId);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("File does not exists"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public void delete(UUID id) {
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
            throw new RuntimeException("Fail ImageFile Convert To file");
        }
    }
}
