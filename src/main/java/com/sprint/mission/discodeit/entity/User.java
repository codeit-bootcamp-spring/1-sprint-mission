package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Long createdAt;
  private Long updatedAt;
  private List<UUID> userMessageIdList;
  private String name;

  @JsonCreator
  public User(
      @JsonProperty("id") UUID id,
      @JsonProperty("createdAt") Long createdAt
  ) {
    this.id = id != null ? id : UUID.randomUUID(); // id가 없으면 새로 생성
    this.createdAt = createdAt != null ? createdAt : System.currentTimeMillis(); // createdAt이 없으면 현재 시간
  }

  public User(String name) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.name = name;
    this.updatedAt = createdAt;
    this.userMessageIdList = new ArrayList<>();
  }

  public User(User user) {
    this.id = user.id;
    this.createdAt = user.createdAt;
    this.name = user.name;
    this.updatedAt = user.updatedAt;
    this.userMessageIdList = user.userMessageIdList;
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

  public Long getCreatedAt() {
    return createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

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

  public void setMessage(List<UUID> MessageIdList){
    this.userMessageIdList = MessageIdList;
  }

//  public void setId(UUID userId) {
//    this.id = userId;
//  }
//
//  public void setCreatedAt(Long createdAt) {
//    this.createdAt = createdAt;
//  }



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
        + updatedAt;

  }
}
