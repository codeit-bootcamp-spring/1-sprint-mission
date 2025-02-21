package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    // MultipartFile을 BinaryContent로 변환 및 저장
    public BinaryContent saveFile(MultipartFile file) {
        try{
            BinaryContent binaryContent = new BinaryContent(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            return binaryContentRepository.save(binaryContent);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public BinaryContent create(BinaryContentDto dto) {
        if(dto.data() == null || dto.contentType() == null){
            throw new NoSuchElementException("BinaryContent is null");
        }

        BinaryContent binaryContent = new BinaryContent(dto.fileName(),dto.contentType(),dto.data());
        return binaryContentRepository.save(binaryContent);
    }

    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(()->new NoSuchElementException("BinaryContent not found"));
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }

    public void deleteById(UUID id){
        if(!binaryContentRepository.existsById(id)){
            throw new NoSuchElementException("BinaryContent not found");
        }
        binaryContentRepository.deleteById(id);
    }
}
