package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;
  private String name;
  private String description;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "channel")
  private List<ReadStatus> statuses = new ArrayList<>();


  public enum ChannelType {
    PRIVATE, PUBLIC
  }

  public void updateChannelName(String channelName) {

    if (channelName.isBlank()) {
      return;
    }

    this.name = channelName;
  }

  public void updateDescription(String description) {
    if (description.isBlank()) {
      return;
    }

    this.description = description;
  }

  public void addReadStatus(ReadStatus readStatus) {
    if (statuses == null) {
      statuses = new ArrayList<>();
    }
    statuses.add(readStatus);
    readStatus.addChannel(this);
  }


  @Override
  public String toString() {
    return "Channel{" +
        "id='" + getId() + '\'' +
        ", channelType=" + type +
        ", channelName='" + name + '\'' +
        ", createdAt=" + getCreatedAt() +
        ", updatedAt=" + getUpdatedAt() +
        '}';
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

    return Objects.equals(getId(), channel.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
