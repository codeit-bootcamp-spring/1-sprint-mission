package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * # Message
 * - 텍스트 채팅 객체
 * - 단체 대화만 한다고 가정 (언급 기능 등은 일단 고려하지 않음)
 * - 파일 첨부 기능 등이 아닌 단순 문자 채팅만 한다고 고려
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

    @Column(name = "content", nullable = false)
    private String content; // 메시지 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "fk_channel"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;    // 메시지 대상 서버

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_author"), nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User author;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageAttachment> attachments = new ArrayList<>();

    // 생성자
    public Message(String content, Channel channel, User author) {
        this.content = content;
        this.channel = channel;
        this.author = author;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateAuthor(User author) {
        this.author = author;
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
    }

    public void clearAttachments() {
        this.attachments.clear();
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}
