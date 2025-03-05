package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.error.execption.bianryContent.BinaryContentNofFoundException;
import com.sprint.mission.discodeit.global.util.MultipartFileConverter;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final MultipartFileConverter multipartFileConverter;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public BinaryContent createBinaryContent(MultipartFile multipartFile) {
    byte[] converterByteArray = multipartFileConverter.toByteArray(multipartFile);
    BinaryContent binaryContent = BinaryContent.of(multipartFile.getOriginalFilename(),
        multipartFile.getContentType(), converterByteArray);

    return binaryContentRepository.saveBinaryContent(binaryContent);
  }

  @Override
  public BinaryContent createBinaryContent(BinaryContent binaryContent) {
    return binaryContentRepository.saveBinaryContent(binaryContent);
  }

  @Override
  public BinaryContentDto findBinaryContentById(UUID binaryContentId) {
    BinaryContent binaryContent =
        Optional.ofNullable(binaryContentRepository.findBinaryContentById(binaryContentId))
            .orElseThrow(() -> new BinaryContentNofFoundException("id: " + binaryContentId));

    return binaryContentMapper.toBinaryContentDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllBinaryContentsById(List<UUID> binaryContentIdList) {
    return binaryContentIdList.stream()
        .map(binaryContentId -> findBinaryContentById(binaryContentId))
        .toList();
  }

  @Override
  public void deleteBinaryContent(UUID binaryContentId) {
    findBinaryContentById(binaryContentId);

    binaryContentRepository.removeBinaryContent(binaryContentId);
  }
}
