package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  //생성시 persist, 삭제는 ddl - on delete cascade
  @OneToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
      name = "message_attachments", // ERD에 있는 중간 테이블 이름
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments = new ArrayList<>();

  public Message(String content, User author, Channel channel) {
    this.content = content;
    this.author = author;
    this.channel = channel;
  }

  public void addAttachments(BinaryContent attachment) {
    this.attachments.add(attachment);
  }


  public void setMessage(String content) {
    if (content != null && !content.equals(this.content)) {
      this.content = content;
    } else {
      throw new IllegalArgumentException("입력한 메시지: " + content + "가 기존 값과 같습니다.");
    }
  }


}