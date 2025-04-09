package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.execption.channel.ChannelNotNullException;
import com.sprint.mission.discodeit.execption.user.UserNotNullException;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY) // on delete set null을 기능적으로 지원하지 않는 듯 하다.
  @JoinColumn(name = "author_id")
  private User sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @Column(columnDefinition = "TEXT")
  private String content;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachmentsList = new ArrayList<>();

  private Message(User sender, Channel channel, String content) {
    super();
    this.sender = sender;
    this.channel = channel;
    this.content = content;
  }

  public static Message of(User sender, Channel channel, String content) {
    if (sender == null) {
      throw new UserNotNullException();
    }
    if (channel == null) {
      throw new ChannelNotNullException();
    }
    return new Message(sender, channel, content);
  }

  public void updateContent(String content) {
    this.content = content;
    this.updateUpdatedAt();
  }

  public boolean isNotOwner(UUID senderId) {
    return !(this.sender.getId().equals(senderId));
  }

  public void addAttachment(BinaryContent attachment) {
    if (attachment == null) {
      throw new IllegalArgumentException(); // custom exception
    }
    this.attachmentsList.add(attachment);
    this.updateUpdatedAt();
  }

  public void deleteAttachment(BinaryContent attachment) {
    if (attachment == null) {
      throw new IllegalArgumentException();
    }
    this.attachmentsList.remove(attachment);
    this.updateUpdatedAt();
  }
}
