package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseChannel {
  private final String UUID;
  private final String ServerUUID;
  private final String CategoryUUID;
  private String channelName;
  private int maxNumberOfPeople;
  private String tag;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isPrivate;

  BaseChannel(BaseChannelBuilder<?> builder) {
    if (builder.serverUUID == null || builder.categoryUUID == null || builder.channelName == null) {
      throw new IllegalArgumentException("Required fields cannot be null");
    }
    this.UUID = java.util.UUID.randomUUID().toString();
    this.ServerUUID = builder.serverUUID;
    this.CategoryUUID = builder.categoryUUID;
    this.channelName = builder.channelName;
    this.maxNumberOfPeople = builder.maxNumberOfPeople;
    this.tag = builder.tag;
    this.isPrivate = builder.isPrivate;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public static abstract class BaseChannelBuilder<T extends BaseChannelBuilder<T>> {
    private String serverUUID;
    private String categoryUUID;
    private String channelName;
    private int maxNumberOfPeople = 50;
    private String tag = "default";
    private Boolean isPrivate = false;

    public T serverUUID(String serverUUID) {
      this.serverUUID = serverUUID;
      return self();
    }

    public T categoryUUID(String categoryUUID) {
      this.categoryUUID = categoryUUID;
      return self();
    }

    public T channelName(String channelName) {
      this.channelName = channelName;
      return self();
    }

    public T maxNumberOfPeople(int maxNumberOfPeople) {
      this.maxNumberOfPeople = maxNumberOfPeople;
      return self();
    }

    public T tag(String tag) {
      this.tag = tag;
      return self();
    }

    public T isPrivate(Boolean isPrivate) {
      this.isPrivate = isPrivate;
      return self();
    }

    protected abstract T self();

    public abstract BaseChannel build();

  }

  public void updatePrivate(Boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  ;

  @Override
  public String toString() {
    return "BaseChannel{" +
        "UUID='" + UUID + '\'' +
        ", ServerUUID='" + ServerUUID + '\'' +
        ", CategoryUUID='" + CategoryUUID + '\'' +
        ", channelName='" + channelName + '\'' +
        ", maxNumberOfPeople=" + maxNumberOfPeople +
        ", tag='" + tag + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", isPrivate=" + isPrivate +
        '}';
  }
}
