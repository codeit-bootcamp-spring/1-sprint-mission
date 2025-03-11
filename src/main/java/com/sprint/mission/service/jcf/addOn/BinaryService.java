package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.BinaryContentStorage;
import com.sprint.mission.repository.BinarycontentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryService {

    private final BinarycontentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;


    public BinaryContent create(BinaryContentDtoForCreate request){
        //binaryContentStorage.put(savedBinaryContent.getId(), request.bytes());
        return binaryContentRepository.save(request.toEntity());
    }

    public BinaryContent findById(UUID id){
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_BINARY));
    }

//    public List<BinaryContent> findAllByIdList(List<UUID> idList) {
//        return binaryContentRepository.findAllById(idList);
//    }
//    public ResponseEntity<Resource> download(UUID binaryId) {
//        BinaryContent downlodingBinaryContent = binaryContentRepository.findById(binaryId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_BINARY));
//
//    }

    public void deleteById(UUID binaryId) {
        if (!binaryContentRepository.existsById(binaryId)) throw new CustomException(ErrorCode.NO_SUCH_BINARY);
        else binaryContentRepository.deleteById(binaryId);
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds);
    }
}
