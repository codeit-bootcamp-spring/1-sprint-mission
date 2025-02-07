package com.sprint.mission.discodeit.service.binaryContent;

import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Qualifier("BasicBInaryContentService")
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequestDTO request) {
        BinaryContent binaryContent = request.from();

        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("BinaryContent not found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> id) {
        return binaryContentRepository.findAllByIdIn(id);
    }

    @Override
    public void delete(UUID id) {
        if(!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException("BinaryContent not found");
        }

        binaryContentRepository.deleteById(id);
    }





}
