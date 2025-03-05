package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public ResponseBinaryContentDto create(MultipartFile file) throws RuntimeException {
    try {
      BinaryContent binaryContent = new BinaryContent(
          file.getName(),
          file.getBytes(),
          file.getContentType(),
          file.getSize()
      );
      BinaryContent savedContent = binaryContentRepository.save(binaryContent);
      return ResponseBinaryContentDto.from(savedContent);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResponseBinaryContentDto findById(String contentId) {
    return ResponseBinaryContentDto.from(binaryContentRepository.findById(contentId));
  }

  @Override
  public List<ResponseBinaryContentDto> findAllByIdIn(List<String> contentIds) {
    List<BinaryContent> list = binaryContentRepository.findAll().stream()
        .filter(binaryContent -> contentIds.contains(binaryContent.getId())).toList();
    return list.stream().map(ResponseBinaryContentDto::from).toList();
  }

  @Override
  public boolean deleteById(String contentId) {
    return binaryContentRepository.delete(contentId);
  }
}
