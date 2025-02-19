package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.exception.BinaryContentException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("binaryContentService")
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {
    private final BinaryContentRepository binaryRepository;

    @Override
    public BinaryContentResponse create(BinaryContentRequest request) {
        if (request == null) {
            throw new BinaryContentException("잘못된 request");
        }
        BinaryContent binaryContent = convertToBinaryContent(request);
        binaryRepository.save(binaryContent);
        return convertBinaryContentResponse(request);
    }

    @Override
    public BinaryContentResponse update(BinaryContentRequest request) {
        if (request == null) {
            throw new BinaryContentException();
        }
        BinaryContent updateBinaryContent = convertToBinaryContent(request);
        binaryRepository.save(updateBinaryContent);
        return convertBinaryContentResponse(request);
    }

    @Override
    public BinaryContentResponse find(UUID id) {
        BinaryContent findContent = binaryRepository.findById(id);

        return new BinaryContentResponse(findContent.getUserId(), findContent.getMessageId(), findContent.getFileName(), findContent.getFileType());
    }

    @Override
    public Map<UUID, BinaryContentResponse> findAllById(UUID id) {
        Map<UUID, BinaryContent> binaryContentMap = binaryRepository.findAll();

        return getBinaryContentResponseMap(id, binaryContentMap);
    }


    public Map<UUID, BinaryContentResponse> findByUserId(UUID userId) {
        Map<UUID, BinaryContent> byUserId = binaryRepository.findByUserId(userId);

        if (byUserId == null) {
            throw new BinaryContentException("userId에 대한 binary를 찾을 수 없음");
        }

        return getBinaryContentResponseMap(userId, byUserId);
    }
    @Override
    public void delete(UUID id) {
        BinaryContentResponse findContent = find(id);

        if (findContent == null) {
            throw new BinaryContentException("찾는 binaryContent가 없습니다.");
        }

        binaryRepository.remove(id);
    }

    private static Map<UUID, BinaryContentResponse> getBinaryContentResponseMap(UUID id, Map<UUID, BinaryContent> binaryContentMap) {
        return binaryContentMap.entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(id))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new BinaryContentResponse(
                                entry.getValue().getUserId(),
                                entry.getValue().getMessageId(),
                                entry.getValue().getFileName(),
                                entry.getValue().getFileType()
                        )
                ));
    }

    private static BinaryContent convertToBinaryContent(BinaryContentRequest request) {
        return new BinaryContent(request.userId(), request.messageId(), request.fileName(), request.fileType());
    }

    private static BinaryContentResponse convertBinaryContentResponse(BinaryContentRequest request) {
        return new BinaryContentResponse(request.userId(), request.messageId(), request.fileName(), request.fileType());
    }

}
