package com.sprint.mission.discodeit.entity;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@Setter
@Table(name = "messages")
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

  // entity는 지연로딩 때문에 final 키워드를 가지고 갈 수 없다!
  //메세지 작성자

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;
  //메세지 내용
  @Column(columnDefinition = "text", nullable = false)
  private String content;
  //메세지가 생성된 채널
  @ManyToOne(optional = false)
  @JoinColumn(name = "channel_id")
  private Channel channel;
  //첨부 이미지 목록
  //다대다 -> 다:1 1:다 로 중간 테이블을 놓아서 풀어사용해야한다.
  //일대다 단방향은 사용하지 않는 편이 좋고, 일대다 양방향으로 사용하거나, 다대일 단방향으로 사용하자!!!
  @OneToMany(mappedBy = "message", fetch = FetchType.LAZY,
      orphanRemoval = true, cascade = CascadeType.ALL)
  private List<MessageAttachments> messageAttachments = new ArrayList<>();
  //기존에 JPA가 자동으로 중간테이블을 관리하도록 하는 방식은 단순 매핑만 함.
  // 따라서 id 컬럼이 자동으로 생성되지 않는 문제가 발생.
  // -> 중간 테이블을 명시적 엔티티로 관리하여, BaseEntity의 @Id가 정상 작동하도록 함

  public Message(User author, String content, Channel channel) {
    this.author = author;
    this.content = content;
    this.channel = channel;
  }

  public void addFile(BinaryContent binaryContent) {
    if (binaryContent != null) {
      MessageAttachments attachment = new MessageAttachments();
      attachment.setMessage(this);
      attachment.setContent(binaryContent);
      messageAttachments.add(attachment);
    }
  }

  public List<BinaryContent> getAttachments() {
    return messageAttachments.stream()
        .map(MessageAttachments::getContent)
        .toList();
  }
  //추후에 추가할 것
  //멘션, 답장(reply)

}
