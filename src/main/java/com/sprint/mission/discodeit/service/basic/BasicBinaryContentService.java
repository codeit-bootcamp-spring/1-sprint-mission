package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(UUID contentId, BinaryContentCreateRequest request){
        if(!binaryContentRepository.existsById(contentId)){
            throw new NoSuchElementException("파일이 첨부되지 않았습니다. 파일을 첨부해주세요");
        }
        String fileName = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();
        BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID contentId){
        return binaryContentRepository.findById(contentId)
                .orElseThrow(() -> new NoSuchElementException("해당 파일이 존재하지 않습니다."));
    }


    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> contentIds){
        return binaryContentRepository.findAllByIdIn(contentIds);
    }

    @Override
    public void delete(UUID contentId){
        if (!binaryContentRepository.existsById(contentId)) {
            throw new NoSuchElementException("해당 파일이 존재하지 않습니다.");
        }
        binaryContentRepository.deleteById(contentId);
    }
}
