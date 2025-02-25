package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

/**
 * 엔티티의 유효성을 id 기준 검증 및 조회 기능을 제공하는 Validator 클래스
 * {@link com.sprint.mission.discodeit.config.RepositoryMapConfig} 에서 주입된 Map 을 사용하여 동적으로 엔티티를 조회
 */
@Component
@RequiredArgsConstructor
public class EntityValidator {

  private final Map<Class<?>, BaseRepository<?, ?>> baseRepositoryMap;

  /**
   * @param entityType 조회할 엔티티의 클래스
   * @param id 조회할 엔티티의 식별자
   * @param exception 엔티티가 존재하지 않을 경오 발생시킬 예외
   * @return 조회된 엔티티 객체
   * @param <T> 조회할 엔티티으 ㅣ타입
   */
  @SuppressWarnings("unchecked")
  public <T> T findOrThrow(Class<T> entityType, String id, RuntimeException exception) {

    BaseRepository<T, String> repository = (BaseRepository<T, String>) baseRepositoryMap.get(entityType);

    if(repository == null) throw new CustomException(ErrorCode.DEFAULT_ERROR_MESSAGE);

    return repository.findById(id)
        .orElseThrow(() -> exception);
  }

}
