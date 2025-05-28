package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "messages")
@Getter
@AllArgsConstructor // @Builder가 모든 필드를 받는 생성자를 필요로 한다
@Builder
public class Message extends BaseUpdatableEntity {

  @Column
  private String content;

  @ManyToOne
  @JoinColumn(nullable = false, name = "channel_id")
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JoinTable( // 불확실
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments;

  protected Message() {

  }

  public void updateMessageText(String content) {
    if (content == null) {
      throw new IllegalArgumentException("channelName 은 null 일 수 없습니다.");
    }
    this.content = content;
    this.refreshUpdateAt();
  }
}
