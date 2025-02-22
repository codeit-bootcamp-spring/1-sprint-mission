package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.jcf.addOn.JCFBinaryContentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryService {

    private final JCFBinaryContentRepository binaryContentRepository;

    public BinaryContent create(BinaryContentDto request){
        return binaryContentRepository.save(request.toEntity());
    }

    public BinaryContent findById(UUID id){
        return binaryContentRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_BINARY));
    }

    public List<BinaryContent> findAllByIdList(List<UUID> idList) {
        return binaryContentRepository.findAllByIdIn(idList);
    }

    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) throw new CustomException(ErrorCode.NO_SUCH_BINARY);
        else binaryContentRepository.delete(id);
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds);
    }
}
