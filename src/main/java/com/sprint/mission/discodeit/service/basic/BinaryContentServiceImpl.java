package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final UserValidator userValidator;


  @Override
  public BinaryContent create(CreateBinaryContentDto dto) {

    userValidator.throwIfNoSuchUserId(dto.userId());

    BinaryContent binaryContent = new BinaryContent.BinaryContentBuilder(dto.userId(), dto.fileName(), dto.fileType(), dto.fileSize(), dto.data()).build();

    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent find(String id) {
    return binaryContentRepository.findById(id).orElseThrow(
        () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
    );
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<String> ids) {
    List<BinaryContent> contents = ids.stream().map(
        id -> binaryContentRepository.findById(id).orElseThrow(() -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE))
    ).toList();

    return contents;
  }

  @Override
  public void delete(String id) {
    binaryContentRepository.deleteById(id);
  }
}
