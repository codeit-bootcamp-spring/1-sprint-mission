package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.ChannelValidationException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public abstract class BaseChannel implements Serializable {
  private static final long serialVersionUID = 1L;

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

    public BaseChannelBuilder(String channelName) {
      if (channelName == null || channelName.isEmpty()) {
        throw new ChannelValidationException();
      }
      this.channelName = channelName;
    }

    public T serverUUID(String serverUUID) {
      this.serverUUID = serverUUID;
      return self();
    }

    public T categoryUUID(String categoryUUID) {
      this.categoryUUID = categoryUUID;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseChannel that = (BaseChannel) o;
    return Objects.equals(UUID, that.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }
}
