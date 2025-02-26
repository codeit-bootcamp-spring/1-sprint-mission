package com.sprint.mission.discodeit.entity.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.BaseObject;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel extends BaseObject implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotEmpty
  @JsonProperty("name")
  private String channelName;

  @JsonProperty("description")
  private String description;

  @Setter
  @NotEmpty
  @JsonProperty("type")
  private String channelType;


  @JsonProperty("channelOwnerUser")
  private User channelOwnerUser;

  @JsonProperty("channelUsers")
  private Map<UUID, User> channelUsers = new HashMap<>();

  @Setter
  @JsonProperty("channelMessages")
  private Map<UUID, Message> channelMessages = new HashMap<>();

  @Getter
  private List<UUID> participantIds = new ArrayList<>();

  @Builder
  public Channel(String channelName, String description, String channelType) {
    super(UUID.randomUUID(), Instant.now(), Instant.now());
    this.channelName = channelName;
    this.description = description;
    this.channelType = channelType;
  }

  public boolean isThereUserHere(User user) {
    return channelUsers.containsValue(user);
  }

  public void modifyChannel(String name, String description) {
    this.channelName = name;
    this.description = description;
  }

  public Channel() {
  }

  public Channel(UUID id) {
    super(id);
  }

  public Channel(String channelName, User channelOwnerUser, String channelType,
      Map<UUID, User> channelUsers) {
    super();
    setChannelName(channelName);
    setChannelOwnerUser(channelOwnerUser);

    this.channelUsers = new HashMap<>();
    this.channelMessages = new HashMap<>();
    this.channelType = channelType;

    if (channelUsers != null) {
      this.channelUsers.putAll(channelUsers);
    }
  }

  public Channel(String channelName, User channelOwnerUser) {
    super();
    setChannelName(channelName);
    setChannelOwnerUser(channelOwnerUser);
    this.channelUsers = new HashMap<>();
  }

  public Channel(UUID channelId, String channelName, User channelOwnerUser, String channelType) {
    super(channelId);
    setChannelName(channelName);
    setChannelOwnerUser(channelOwnerUser);
    this.channelType = channelType;
    this.channelUsers = new HashMap<>();
  }

  public Channel(User channelOwnerUser, String channelType, Map<UUID, User> channelUsers) {
    setChannelOwnerUser(channelOwnerUser);
    this.channelType = channelType;
    this.channelUsers = channelUsers;
  }


  public UUID addUser(User user) {
    if (user == null) {
      throw new UserNotFoundException();
    }

    // 채널에 해당하는 유저 있으면 안됨
    if (channelUsers.containsValue(user)) {
      throw new IllegalUserException("채널에 유저가 이미 존재합니다.");
    }

    channelUsers.put(user.getId(), user);

    setUpdatedAt();

    return user.getId();
  }


  public Message addMessageInChannel(Message addMessage) {
    checkMessage(addMessage);

    channelMessages.put(addMessage.getId(), addMessage);

    Message putMessage = channelMessages.get(addMessage.getId());

    setUpdatedAt();

    return putMessage;
  }

  public void removeUser(User user) {
    channelUsers.entrySet().stream()
        .filter(entry -> entry.getKey().equals(user.getId()))
        .findFirst()
        .orElseThrow(UserNotFoundException::new);

    channelUsers.remove(user.getId());
    setUpdatedAt();
  }


  public void removeMessageInChannel(UUID messageId) {
    if (!channelMessages.containsKey(messageId)) {
      throw new MessageNotFoundException();
    }

    channelMessages.remove(messageId);
  }


  private void checkChannelName(String channelName) {
    if (channelName == null || channelName.isEmpty()) {
      throw new IllegalArgumentException("채널 이름을 작성해주세요.");
    }
  }

  private void checkChannelOwnerUser(User channelOwnerUser) {
    if (channelOwnerUser == null) {
      throw new UserNotFoundException();
    }
  }

  private void checkMessage(Message addMessage) {
    if (addMessage == null) {
      throw new MessageNotFoundException();
    }

    UUID sender = addMessage.getMessageSendUser().getId();
    UUID receiver = addMessage.getMessageReceiveUser().getId();

    if (channelOwnerUser.getId().equals(sender) || channelOwnerUser.getId().equals(receiver)) {
      return;
    }

    if (!channelUsers.containsKey(sender) || !channelUsers.containsKey(receiver)) {
      throw new UserNotFoundException();
    }
  }


  public String updateChannelName(String updateChannelName) {
    setChannelName(updateChannelName);
    return this.channelName;
  }


  public User updateOwnerUser(User updateOwnerUser) {
    setChannelOwnerUser(updateOwnerUser);
    return this.channelOwnerUser;
  }


  public Map<UUID, User> updateChannelUsers(Map<UUID, User> updateChannelUsers) {
    if (updateChannelUsers == null) {
      this.channelUsers = new HashMap<>();
    } else {
      this.channelUsers = new HashMap<>(updateChannelUsers);
    }
    setUpdatedAt();
    return this.channelUsers;
  }


  private void setChannelName(String channelName) {
    checkChannelName(channelName);
    this.channelName = channelName;
    setUpdatedAt();
  }


  private void setChannelOwnerUser(User channelOwnerUser) {
    checkChannelOwnerUser(channelOwnerUser);
    this.channelOwnerUser = channelOwnerUser;
    setUpdatedAt();
  }

  @Override
  public String toString() {
    return "Channel{" +
        "channelName='" + channelName + '\'' +
        ", channelOwnerUser=" + channelOwnerUser +
        ", channelUsers=" + channelUsers +
        ", channelMessages=" + channelMessages +
        '}' + "\n";
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Channel channel = (Channel) o;
    return Objects.equals(getId(), channel.getId()) &&
        Objects.equals(channelName, channel.channelName) &&
        Objects.equals(channelOwnerUser, channel.channelOwnerUser) &&
        Objects.equals(channelMessages, channel.channelMessages) &&
        Objects.equals(channelUsers, channel.channelUsers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), channelName, channelOwnerUser, channelUsers, channelMessages);
  }
}
