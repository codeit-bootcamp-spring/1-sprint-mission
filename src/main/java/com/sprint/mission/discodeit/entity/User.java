package com.sprint.mission.discodeit.entity;

// User 파일이 Gender 파일과 같은 패키지 안에 있으므로 따로 임포트하지 않아도 됨

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor // 롬복은 나중에 생성자 호출할 때 파라미터로 뭘 줘야하는지 헷갈림
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id = UUID.randomUUID();
  private final Instant createdAt = Instant.now();
  private Instant updatedAt = null;

  @NonNull
  private String name;
  @NonNull
  private int age; // 기본 타입은 null이 될 수가 없더라도 @NonNull로 명시를 해줘야 @RequiredArgsConstructor가 인식을 하는거 아닌가..?
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


