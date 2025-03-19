package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  // 데이터 삭제
  User save(User user);

  // 데이터 삭제
  void delete(User user);

  void deleteById(UUID id);

  // 객체 찾기
  Optional<User> findById(UUID userId);

  Optional<User> findByUsername(String name);

  // 저장된 모든 데이터 가져오기
  List<User> findAll();

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile")
  List<User> findAllFetchJoin();

  // 존재 검증
  @Transactional(readOnly = true)
  boolean existsById(UUID userId);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @Transactional
  @Modifying
  @Query("UPDATE User u SET u.username = :newUsername, u.email = :newEmail, u.password = :newPassword, u.profile.id = :newProfileId WHERE u.id = :id")
  int updateById(
      @Param("id") UUID id,
      @Param("newUsername") String newUsername,
      @Param("newEmail") String newEmail,
      @Param("newPassword") String newPassword,
      @Param("newProfileId") UUID newProfileId
  );

}
