package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContent.CreateDTO;
import com.sprint.mission.discodeit.dto.user.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.data.BinaryContent;
import com.sprint.mission.discodeit.entity.data.ContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent created(CreateDTO data){
        BinaryContent binaryContent =
                new BinaryContent(data.contentType(), data.filename(),data.fileType(), data.data());
        binaryContentRepository.save(binaryContent, data.contentType());
        return binaryContent;
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + id + " not found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn() {
        return binaryContentRepository.findAll();
    }

    @Override
    public void delete(UUID id, ContentType contentType) {
        binaryContentRepository.deleteById(id, contentType);
    }

}
