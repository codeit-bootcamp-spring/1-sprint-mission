package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  // 검증을 위한 쿼리 메서드 추가 -> 모든 User 객체를 불러와 비교하는 방식은 비효율적이기 때문에
  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByUsername(String username);

  /**
   * BinaryContentService 작업 때 SSE 연결을 위해 넘기려고 했으나, Race Condition 에 대한 우려로 보류 (워커 스레드의 whenComplete
   * 콜백이 메인 스레드의 트랜잭션 보다 빠를 경우)
   **/
  User findByProfile(BinaryContent profile);
}