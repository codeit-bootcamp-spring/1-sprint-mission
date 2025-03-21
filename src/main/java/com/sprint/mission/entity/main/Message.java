package com.sprint.mission.entity.main;

import com.sprint.mission.entity.addOn.BinaryContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"content", "author", "channel"}, callSuper = true)
@ToString(of = "content")
@Getter
@Schema(description = "메시지 엔티티")
@Table(name = "messages")
public class Message extends BaseUpdatableEntity{
//    @ToString.Exclude
//    private static final long serialVersionUID = 1L;

    private String content;

    @ManyToOne(fetch = LAZY, cascade = REMOVE)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL) // profile이 삭제되면 user의 profile은 null로 변경
    private User author;

    // 설계도에서 OneToMany 관계를 JoinTable로 설계하도록 되어있어서...
    // 이런 구조에서 Message삭제 시 binaryContent자동 삭제는 구현 못했습니다(수동으로 메서드 만들어서 해야될까요)
    // MessageCascadeTest 파일에서 테스트 실패
    @OneToMany(fetch = LAZY, cascade = REMOVE, orphanRemoval = true)
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private List<BinaryContent> messageAttachments = new ArrayList<>();

    public Message(Channel channel, User user, String content) {
        this.content = content;
        this.channel = channel;
        this.author = user;
    }

    public Message update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
        }
        return this;
    }

//    public void addAttachment(BinaryContent attachment) {
//        attachments.add(attachment);
//        //일단은 단방향이니
//   }
}
