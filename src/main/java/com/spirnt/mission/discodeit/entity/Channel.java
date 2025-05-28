package com.spirnt.mission.discodeit.entity;

import com.spirnt.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "channels")
@NoArgsConstructor
@Getter
public class Channel extends BaseUpdatableEntity {

  String name;
  String description;
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  ChannelType type;

  public Channel(String name, String description, ChannelType type) {
    super();
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public void update(String name, String description) {
    if (name != null && !name.equals(this.name)) {
      this.name = name;
    }
    if (description != null && !description.equals(this.description)) {
      this.description = description;
    }
  }


  @Override
  public String toString() {
    return "Channel{" +
        "id='" + this.getId() + '\'' +
        ", username='" + this.name + '\'' +
        '}';
  }

  // 객체를 UUID로 비교하기 위해
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Channel channel = (Channel) obj;
    return Objects.equals(this.getId(), channel.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }
}
