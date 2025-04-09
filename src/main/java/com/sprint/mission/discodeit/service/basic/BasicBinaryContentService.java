package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.util.MultipartFileConverter;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.execption.bianryContent.BinaryContentNofFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final MultipartFileConverter multipartFileConverter;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContent createBinaryContent(MultipartFile multipartFile) {
    BinaryContent binaryContent = BinaryContent.of(multipartFile.getOriginalFilename(),
        multipartFile.getSize(), multipartFile.getContentType());

    BinaryContent savedContent = binaryContentRepository.saveBinaryContent(binaryContent);

    binaryContentStorage.put(savedContent.getId(),
        multipartFileConverter.toByteArray(multipartFile));

    return savedContent;
  }

  @Override
  public BinaryContent createBinaryContent(BinaryContent binaryContent) {
    return binaryContentRepository.saveBinaryContent(binaryContent);
  }

  @Override
  public BinaryContentDto findBinaryContentById(UUID binaryContentId) {
    BinaryContent binaryContent =
        binaryContentRepository.findBinaryContentById(binaryContentId)
            .orElseThrow(() ->
                new BinaryContentNofFoundException(Map.of("binaryContentId", binaryContentId)));

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
