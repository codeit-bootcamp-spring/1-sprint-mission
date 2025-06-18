package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.util.MultipartFileConverter;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.execption.bianryContent.BinaryContentNofFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final MultipartFileConverter multipartFileConverter;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContent createBinaryContent(MultipartFile multipartFile) {
        BinaryContent binaryContent = BinaryContent.of(multipartFile.getOriginalFilename(),
            multipartFile.getSize(), multipartFile.getContentType());

        BinaryContent savedContent = binaryContentRepository.saveBinaryContent(binaryContent);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    CompletableFuture<UUID> uuidCompletableFuture =
                        null;
                    try {
                        uuidCompletableFuture = binaryContentStorage.putAsync(binaryContent.getId(),
                            multipartFileConverter.toByteArray(multipartFile));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    uuidCompletableFuture.whenComplete((uuid, throwable) -> {
                        if (throwable != null) {
                            binaryContent.updateBinaryContentUploadStatus(
                                BinaryContentUploadStatus.FAILED);
                        } else {
                            binaryContent.updateBinaryContentUploadStatus(
                                BinaryContentUploadStatus.SUCCESS);
                        }
                        binaryContentRepository.saveBinaryContent(binaryContent);
                    });
                }
            }
        );

        return savedContent;
    }

    @Override
    public BinaryContent createBinaryContent(BinaryContent binaryContent) {
        return binaryContentRepository.saveBinaryContent(binaryContent);
    }

    @Override
    public BinaryContentDto findBinaryContentById(UUID binaryContentId) {
        BinaryContent binaryContent =
            binaryContentRepository.findBinaryContentById(binaryContentId)
                .orElseThrow(() ->
                    new BinaryContentNofFoundException(Map.of("binaryContentId", binaryContentId)));

        return binaryContentMapper.toBinaryContentDto(binaryContent);
    }

    @Override
    public List<BinaryContentDto> findAllBinaryContentsById(List<UUID> binaryContentIdList) {
        return binaryContentIdList.stream()
            .map(binaryContentId -> findBinaryContentById(binaryContentId))
            .toList();
    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        findBinaryContentById(binaryContentId);

        binaryContentRepository.removeBinaryContent(binaryContentId);
    }
}
