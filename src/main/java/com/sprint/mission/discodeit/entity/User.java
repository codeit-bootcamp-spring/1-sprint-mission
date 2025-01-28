package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Long createdAt;
  private Long updatedAt;
  //삭제예정
  private List<UUID> userMessageIdList;
  private String name;
  private Map<UUID, List<UUID>> userMessageIdList2;

  @JsonCreator
  public User(
      @JsonProperty("id") UUID id,
      @JsonProperty("createdAt") Long createdAt
  ) {
    this.id = id != null ? id : UUID.randomUUID(); // id가 없으면 새로 생성
    this.createdAt = createdAt != null ? createdAt : System.currentTimeMillis(); // createdAt이 없으면 현재 시간
  }
//userMessageIdList map<id, userMessageIdList>
  public User(String name) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.name = name;
    this.updatedAt = createdAt;
    //삭제 예정
    this.userMessageIdList = new ArrayList<>();
    this.userMessageIdList2 = new HashMap<>();

  }

  public User(User user) {
    this.id = user.id;
    this.createdAt = user.createdAt;
    this.name = user.name;
    this.updatedAt = user.updatedAt;
    //삭제 예정
    this.userMessageIdList = user.userMessageIdList;
    this.userMessageIdList2 = user.userMessageIdList2;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<UUID> getUserMessageIdList() {
    return userMessageIdList;
  }

  public Map<UUID, List<UUID>> getUserMessageIdList2() {
    return userMessageIdList2;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }
  //삭제 예정
  public void addMessage(UUID messageId) {
    userMessageIdList.add(messageId);
  }



  public void setUpdatedAt (Long updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void updateName(String name) {
    this.name = name;
    this.updatedAt = System.currentTimeMillis();
  }

  //삭제예정
  public void setMessage(List<UUID> MessageIdList){
    this.userMessageIdList = MessageIdList;
  }

  public void setMessage2(Map<UUID, List<UUID>> MessageIdList){
    this.userMessageIdList2 = MessageIdList;
  }



  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (obj instanceof User) {
      User user = (User) obj;
      return Objects.equals(this.id, user.id);
    }

    if (obj instanceof String) {
      return Objects.equals(this.id.toString(), obj);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }


  public String toString() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return name + "/ createdAt = " + simpleDateFormat.format(createdAt) + "/ updatedAt="
        + updatedAt + id + userMessageIdList;

  }
}
