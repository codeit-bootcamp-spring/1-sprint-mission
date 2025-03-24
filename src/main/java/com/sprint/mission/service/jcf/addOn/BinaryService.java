package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.BinaryContentStorage;
import com.sprint.mission.repository.BinarycontentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BinaryService {

    private final BinarycontentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;


    public BinaryContent create(BinaryContentDtoForCreate request){
        //binaryContentStorage.put(savedBinaryContent.getId(), request.bytes());
        BinaryContent createdBinaryContent = binaryContentMapper.toEntity(request);
        log.info("Create binary content: {}", createdBinaryContent);
        BinaryContent savedUser = binaryContentRepository.save(binaryContentMapper.toEntity(request));
        binaryContentStorage.put(savedUser.getId(), request.bytes());
        return savedUser;
    }

    @Transactional(readOnly = true)
    public BinaryContent findById(UUID id){
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_BINARY));
    }

    public void deleteById(UUID binaryId) {
        if (!binaryContentRepository.existsById(binaryId)) throw new CustomException(ErrorCode.NO_SUCH_BINARY);
        else {
            binaryContentRepository.delete(binaryId);
        }
    }

    @Transactional(readOnly = true)
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds);
    }
}
