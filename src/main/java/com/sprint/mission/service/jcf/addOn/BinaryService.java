package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.jcf.addOn.JCFBinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryService {

    private final JCFBinaryContentRepository binaryContentRepository;

    public BinaryContent create(BinaryContentDto request){
        BinaryContent binaryContent = BinaryContent.CreateBinaryContentByDTO(request);
        return binaryContentRepository.save(binaryContent);
    }

    public BinaryContent findById(UUID id){
        return binaryContentRepository.findById(id).orElseThrow();
    }

    public List<BinaryContent> findAllByIdList(List<UUID> idList) {
        return binaryContentRepository.findAllByIdIn(idList);
    }

    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) throw new RuntimeException();
        else binaryContentRepository.delete(id);
    }
}
