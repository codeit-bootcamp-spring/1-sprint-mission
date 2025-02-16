package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
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
    public ResponseBinaryContentDto create(CreateBinaryContentDto content) {
        BinaryContent binaryContent = binaryContentRepository.save(content);
        return ResponseBinaryContentDto.from(binaryContent);
    }

    @Override
    public ResponseBinaryContentDto findById(String contentId) {
        return ResponseBinaryContentDto.from(binaryContentRepository.findById(contentId));
    }

    @Override
    public List<ResponseBinaryContentDto> findAllByIdIn(List<String> contentIds) {
        List<BinaryContent> list = binaryContentRepository.findAll().stream().filter(binaryContent -> contentIds.contains(binaryContent.getId())).toList();
        List<ResponseBinaryContentDto> result = list.stream().map(ResponseBinaryContentDto::from).toList();
        return result;
    }

    @Override
    public boolean deleteById(String contentId) {
        return binaryContentRepository.delete(contentId);
    }
}
