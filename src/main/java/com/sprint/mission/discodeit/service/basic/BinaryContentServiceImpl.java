package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final EntityValidator validator;

  @Override
  public BinaryContent create(CreateBinaryContentDto dto) {

    validator.findOrThrow(User.class, dto.userId(), new UserNotFoundException());

    BinaryContent binaryContent = dto.isProfile()
        ? new BinaryContent.BinaryContentBuilder(dto.userId(), dto.fileName(), dto.fileType(), dto.fileSize(), dto.data()).isProfilePicture().build()
        : new BinaryContent.BinaryContentBuilder(dto.userId(), dto.fileName(), dto.fileType(), dto.fileSize(), dto.data()).build();

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
  public Map<String, BinaryContent> mapUserToBinaryContent(Set<String> userIds) {

    if (userIds == null || userIds.isEmpty()) return Collections.emptyMap();

    List<BinaryContent> profiles = binaryContentRepository.findProfilesOf(userIds);

    return profiles.stream()
        .collect(Collectors.toMap(BinaryContent::getUserId, content -> content));
  }

  @Override
  public void delete(String id) {
    binaryContentRepository.deleteById(id);
  }
}
