package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Table(name = "messages")
@Entity
@NoArgsConstructor(force = true)
public class Message extends BaseUpdatableEntity implements Serializable {                  // 메시지 (게시물)

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private final Channel channel;       // 메시지가 속해있는 채널

    @ManyToOne
    @JoinColumn(name = "author_id")
    private final User author;        // 작성자 id

    @Column(name = "content")
    private String content;             // 메시지 내용

    @OneToMany
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private List<BinaryContent> attachments;       // 첨부 이미지 id


    // 생성자
    public Message(Channel channel, User author, String content, List<BinaryContent> attachments) {
        this.channel = channel;
        validationAndSetContent(content);
        this.author = author;
        this.attachments = attachments;
    }


    // update 함수
    public void updateContent(String content) {
        validationAndSetContent(content);
    }

    public void updateAttachments(List<BinaryContent> attachments) {
        this.attachments = attachments;
    }


    // 메시지 내용 유효성 검사 및 세팅
    private void validationAndSetContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("메시지 내용을 입력해주세요.");
        }

        content = content.trim();

        this.content = content;
    }
}