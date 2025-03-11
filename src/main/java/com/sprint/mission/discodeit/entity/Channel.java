package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Channel extends BaseUpdateEntity {

  private String channelName;
  private boolean isPrivate;
  private Instant lastMessageTime;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "channel_users",
      joinColumns = @JoinColumn(name = "channel_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> users = new ArrayList<>();

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public Channel(String channelName, boolean isPrivate) {
    this.channelName = channelName;
    this.isPrivate = isPrivate;
    this.lastMessageTime = Instant.now();
  }

  public Channel(boolean isPrivate) {
    this.isPrivate = isPrivate;
    this.lastMessageTime = Instant.now();
  }

  public void updateChannelName(String channelName) {
    this.channelName = channelName;
  }

  public void addMessageToChannel(Message message) {
    if (!messages.contains(message)) {
      messages.add(message);
    }
  }

  public void addUserToChannel(User user) {
    if (!users.contains(user)) {
      users.add(user);  // 중복을 방지하고 유저를 추가
    }
  }

  public void setReadStatus(ReadStatus readStatus) {
    if (!readStatuses.contains(readStatus)) {
      readStatuses.add(readStatus);
    }
  }

  public void setReadStatuses(List<ReadStatus> readStatus) {
    for (ReadStatus status : readStatus) {
      setReadStatus(status);
    }
  }

  @Override
  public String toString() {
    return "\nuuid: " + id + " channelName: " + channelName;
  }
}
