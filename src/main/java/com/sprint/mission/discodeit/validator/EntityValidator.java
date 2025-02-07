package com.sprint.mission.discodeit.validator;


import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class EntityValidator {

  private final Map<Class<?>, BaseRepository<?, ?>> baseRepositoryMap;

  @SuppressWarnings("unchecked")
  public <T> T findOrThrow(Class<T> entityType, String id, RuntimeException exception) {

    BaseRepository<T, String> repository = (BaseRepository<T, String>) baseRepositoryMap.get(entityType);

    if(repository == null) throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE + entityType);

    return repository.findById(id)
        .orElseThrow(() -> exception);
  }

/*
private Optional<?> findEntityById(Class<?> entityType, String id) {
    if (entityType.equals(BinaryContent.class)) {
      return binaryContentRepository.findById(id);
    } else if (entityType.equals(Channel.class)) {
      return channelRepository.findById(id);
    } else if (entityType.equals(Message.class)) {
      return messageRepository.findById(id);
    } else if (entityType.equals(ReadStatus.class)) {
      return readStatusRepository.findById(id);
    } else if (entityType.equals(User.class)) {
      return userRepository.findById(id);
    } else if (entityType.equals(UserStatus.class)) {
      return userStatusRepository.findById(id);
    }

    throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
  }
*/
}
