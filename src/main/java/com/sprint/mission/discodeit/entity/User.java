package com.sprint.mission.discodeit.entity;

// User 파일이 Gender 파일과 같은 패키지 안에 있으므로 따로 임포트하지 않아도 됨
import java.io.Serializable;
import java.util.UUID;


public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id;
  private Long createdAt;
  private Long updatedAt;
  private String name;
  private int age;
  private Gender gender;

  public User(String name, int age, Gender gender) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.updatedAt = this.createdAt;
    this.name = name;
    this.age = age;
    this.gender = gender;
  }

  public UUID getId() {
    return id;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public Gender getGender() {
    return gender;
  }

  public void update(String name, int age, Gender gender) {
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.updatedAt = System.currentTimeMillis();
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


