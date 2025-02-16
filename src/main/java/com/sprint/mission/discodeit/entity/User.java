package com.sprint.mission.discodeit.entity;

// User 파일이 Gender 파일과 같은 패키지 안에 있으므로 따로 임포트하지 않아도 됨

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id = UUID.randomUUID();
  private final Instant createdAt = Instant.now();
  private Instant updatedAt = null;

  @NonNull
  private String name;
  @NonNull
  private int age; // 기본 타입은 null이 될 수가 없더라도 @NonNull로 명시를 해줘야 @RequiredArgsConstructor가 인식을 함
  @NonNull
  private Gender gender;


  public void update(String name, int age, Gender gender) {
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", age=" + age +
      ", gender=" + gender +
      "}";
  }
}


