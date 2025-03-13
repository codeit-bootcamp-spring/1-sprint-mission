package com.sprint.mission.entity.main;

import com.sprint.mission.entity.addOn.BinaryContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
//@NoArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@EqualsAndHashCode(of = {"content", "author", "channel"}, callSuper = true)
@ToString(of = "content")
@Getter @Setter
@Schema(description = "메시지 엔티티")
@Table(name = "messages")
public class Message  extends BaseUpdatableEntity{
//    @ToString.Exclude
//    private static final long serialVersionUID = 1L;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    // 메시지는 user가 삭제되도 남기기?
    @ManyToOne(fetch = LAZY)
    private User author;

    @OneToMany(cascade = REMOVE, orphanRemoval = true)
    @JoinColumn(name = "message_id")
    private List<BinaryContent> attachments = new ArrayList<>();

    public Message(Channel channel, User user, String content) {
        this.content = content;
        this.channel = channel;
        this.author = user;
    }

    public void update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
        }
    }

    public void addAttachment(BinaryContent attachment) {
        attachments.add(attachment);
        //일단은 단방향이니
    }
}
