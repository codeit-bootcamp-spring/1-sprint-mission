package com.sprint.mission.discodeit.dto;

import java.util.Optional;

public class UserUpdateDto {
  private Optional<String> username = Optional.empty();
  private Optional<String> password = Optional.empty();
  private Optional<String> email = Optional.empty();
  private Optional<String> nickname = Optional.empty();
  private Optional<String> phoneNumber = Optional.empty();
  private Optional<String> profilePictureURL = Optional.empty();
  private Optional<String> description = Optional.empty();


  public UserUpdateDto(Optional<String> username, Optional<String> password, Optional<String> email, Optional<String> nickname, Optional<String> phoneNumber, Optional<String> profilePictureURL, Optional<String> description) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.nickname = nickname;
    this.phoneNumber = phoneNumber;
    this.profilePictureURL = profilePictureURL;
    this.description = description;
  }

  public Optional<String> getUsername() {
    return username;
  }

  public Optional<String> getPassword() {
    return password;
  }

  public Optional<String> getEmail() {
    return email;
  }

  public Optional<String> getNickname() {
    return nickname;
  }

  public Optional<String> getPhoneNumber() {
    return phoneNumber;
  }

  public Optional<String> getProfilePictureURL() {
    return profilePictureURL;
  }

  public Optional<String> getDescription() {
    return description;
  }
}
