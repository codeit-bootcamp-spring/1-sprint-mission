package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "authorId")
  private User user;
  // 외래 키 컬럼 이름 바꾸려면 @JoinColumn(name="userId") 이런 식으로.

  @JoinColumn(name = "channelId", nullable = false)
  @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private Channel channel;

  @ManyToMany(cascade = CascadeType.REMOVE)
  // N:M 관계에서는 중간테이블 필요(JoinTable) name:중간테이블 이름, joinColumns nmae:현재 엔티티의 FK이름, inverseJoinColumns name: 반대 엔티티의 FK 이름
  @JoinTable(name = "message_attachment", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "attachment_id"))
  private List<BinaryContent> attachments;


  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}