package com.sprint.mission.discodeit.repository;

import java.util.Optional;

/**
 * 엔티티를 조회할 수 있는 기본적인 레포지토리 인터페이스
 *
 * {@link com.sprint.mission.discodeit.config.RepositoryMapConfig} 에서 BaseRepository<?, ?> 로 map 을 생성하여
 * {@link com.sprint.mission.discodeit.validator.EntityValidator} 에서 주입받아 사용
 * @param <T> 엔티티 타입
 * @param <ID> 엔티티 식별자 타입
 */
public interface BaseRepository<T, ID> {
  Optional<T> findById(ID id);
}
