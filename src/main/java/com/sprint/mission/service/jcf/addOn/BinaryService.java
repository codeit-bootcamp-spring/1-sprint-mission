package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.BinarycontentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryService {

    private final BinarycontentRepository binaryContentRepository;

    public BinaryContent create(BinaryContentDto request){
        return binaryContentRepository.save(request.toEntity());
    }

    public BinaryContent findById(UUID id){
        return binaryContentRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_BINARY));
    }

//    public List<BinaryContent> findAllByIdList(List<UUID> idList) {
//        return binaryContentRepository.findAllById(idList);
//    }

    public void deleteById(UUID binaryId) {
        if (!binaryContentRepository.existsById(binaryId)) throw new CustomException(ErrorCode.NO_SUCH_BINARY);
        else binaryContentRepository.deleteById(binaryId);
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds);
    }
}
