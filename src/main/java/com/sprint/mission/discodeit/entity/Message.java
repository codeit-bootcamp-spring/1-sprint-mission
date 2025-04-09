package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;


  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<MessageAttachment> attachments = new ArrayList<>();

  public void addContent(String content) {
    this.content = content;
  }

  public void addAttachment(BinaryContent attachment) {
    this.attachments.add(new MessageAttachment(this, attachment));
  }


  public void addChannel(Channel channel) {
    this.channel = channel;
  }

  public void addAuthor(User user) {
    this.author = user;
  }

  @Override
  public String toString() {
    return "Message{"
        + "id='" + getId() + '\''
        + ", userid='" + author.getId() + '\''
        + ", channelid='" + channel.getId() + '\''
        + ", content='" + content + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message = (Message) o;

    return Objects.equals(getId(), message.getId());

  }

  @Override
  public int hashCode() {

    return Objects.hash(getId());

  }
}
