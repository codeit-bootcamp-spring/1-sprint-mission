package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.entity.BinaryContent;
import com.spirnt.mission.discodeit.exception.BinaryContent.BinaryContentNotFoundException;
import com.spirnt.mission.discodeit.mapper.BinaryContentMapper;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public BinaryContentDto create(BinaryContentCreateRequest binaryContentCreateRequest) {
        String fileName = binaryContentCreateRequest.fileName();
        byte[] bytes = binaryContentCreateRequest.bytes();
        String contentType = binaryContentCreateRequest.contentType();
        BinaryContent binaryContent = new BinaryContent(
            fileName,
            (long) bytes.length,
            contentType
        );
        binaryContentRepository.save(binaryContent);
        // 로컬 스토리지에 저장
        binaryContentStorage.put(binaryContent.getId(), bytes);
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    public BinaryContentDto find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("[Finding BinaryContent Failed: BinaryContent with id {} not found]", id);
                return new BinaryContentNotFoundException(Map.of("binaryContentId", id));
            });
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> uuids) {
        List<BinaryContentDto> list = new ArrayList<>();
        for (UUID id : uuids) {
            list.add(find(id));
        }
        return list;
    }


}
