/*
package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity */
/*implements Serializable*//*
 {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  @Column(nullable = false)
  private String name;

  @Column
  private String description;

  // 채널의 자식 엔티티들을 위한 컬렉션
  @OneToMany(mappedBy = "channel", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<Message> messages = new ArrayList<>();

  @OneToMany(mappedBy = "channel", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  protected Channel() {
    super();
  }

  public Channel(ChannelType type, String name, String description) {
    // super(); // 부모의 생성자 호출하여 ID 생성 없어도 호출이 가능하나 명시적으로 작성
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }

  // 자식 엔티티들을 관리하기 위한 헬퍼 메서드
  public void addMessage(Message message) {
    if (!this.messages.contains(message)) {
      this.messages.add(message);
    }
  }

  public void addReadStatus(ReadStatus readStatus) {
    if (!this.readStatuses.contains(readStatus)) {
      this.readStatuses.add(readStatus);
    }
  }
}
*/
package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;
  @Column(length = 100)
  private String name;
  @Column(length = 500)
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }
}
