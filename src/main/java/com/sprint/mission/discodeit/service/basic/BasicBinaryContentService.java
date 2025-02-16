package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(CreateBinaryContentDto content) {
        return binaryContentRepository.save(content);
    }

    @Override
    public BinaryContent findById(String contentId) {
        return binaryContentRepository.findById(contentId);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<String> contentIds) {
        return binaryContentRepository.findAll().stream().filter(binaryContent -> contentIds.contains(binaryContent.getId())).collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String contentId) {
        return binaryContentRepository.delete(contentId);
    }
}
