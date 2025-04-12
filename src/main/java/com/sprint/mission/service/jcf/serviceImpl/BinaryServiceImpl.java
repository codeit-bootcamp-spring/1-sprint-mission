package com.sprint.mission.service.jcf.serviceImpl;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.BinaryContentStorage;
import com.sprint.mission.repository.BinaryContentRepository;
import com.sprint.mission.service.BinaryService;
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
public class BinaryServiceImpl implements BinaryService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;


    @Override
    public BinaryContent create(BinaryContentDtoForCreate request){
        BinaryContent savedUser = binaryContentRepository.save(binaryContentMapper.toEntity(request));
        binaryContentStorage.put(savedUser.getId(), request.bytes());
        return savedUser;
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContent findById(UUID id){
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_BINARY));
    }

    @Override
    public void deleteById(UUID binaryId) {
        if (!binaryContentRepository.existsById(binaryId)) throw new CustomException(ErrorCode.NO_SUCH_BINARY);
        else {
            binaryContentRepository.deleteById(binaryId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds);
    }
}
