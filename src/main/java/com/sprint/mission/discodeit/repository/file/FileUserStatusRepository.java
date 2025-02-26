package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

  private HashMap<UUID, UserStatus> data;
  private SerializationUtil<UUID, UserStatus> util;

  public FileUserStatusRepository(SerializationUtil<UUID, UserStatus> util) {
    this.util = util;
    this.data = util.loadData();
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    this.data.put(userStatus.getId(), userStatus);
    util.saveData(data);
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(UUID userStatusId) {
    return Optional.ofNullable(this.data.get(userStatusId));
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return this.findAll().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return this.data.values().stream().toList();
  }

  @Override
  public List<UserStatus> findAllByUserId(UUID userId) {
    List<UserStatus> userStatusList = this.data.values().stream().toList();
    // data.values().stream().toList() 는 불변리스트임. data를 final로 선언했으니까
    // List<UserStatus> userStatusList = new ArrayList<>(this.data.values()); 했으면 add나 remove 가능한 변경 가능한 리스트 됐을 것 (여기선 불변이든 가변이든 뭐 상관 없음)
    List<UserStatus> returnList = new ArrayList<>();
    for (UserStatus userStatus : userStatusList) {
      if (userStatus.getUserId().equals(userId)) {
        returnList.add(userStatus);
      }
    }
    return returnList;
  }

  @Override
  public boolean existsById(UUID userStatusId) {
    return this.data.containsKey(userStatusId);
  }

  @Override
  public void deleteById(UUID userStatusId) {
    this.data.remove(userStatusId);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    this.findByUserId(userId)
        .ifPresent(userStatus -> this.deleteByUserId(userStatus.getId()));
    util.saveData(data);
  }
}
