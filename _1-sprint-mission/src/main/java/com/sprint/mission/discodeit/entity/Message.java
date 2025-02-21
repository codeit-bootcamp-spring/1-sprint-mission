package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전
    @Id
    @GeneratedValue
    private UUID id;
    //필드
    private String content; //메세지내용

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user; // 메시지를 보낸 사용자

    @ManyToOne
    @JoinColumn(name = "channel_id",nullable = false)
    private Channel channel; // 메시지가 속한 채널

    @OneToMany
    @JoinColumn(name="attachment_id")
    private List<BinaryContent> attachments;

    //생성자
    public Message(String content, User user, Channel channel, List<BinaryContent> attachments) {
        super();
        // 검증
        if (content == null || content.trim().isEmpty() || content.length() > 500) {
            throw new IllegalArgumentException("메시지 내용은 1~500자 사이여야 합니다.");
        }
        if (channel.getId() == null) {
            throw new IllegalArgumentException("채널 ID는 필수입니다.");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("작성자 ID는 필수입니다.");
        }

        this.content = content;
        this.user = user;
        this.channel = channel;
        this.attachments = attachments;
    }

    // update 메소드
    public void updateContent(String newContent) {
        if (newContent != null && !newContent.trim().isEmpty() && !newContent.equals(this.content)) {
            this.content = newContent;
            updateTimestamp();
        }
    }

    //첨부파일 추가 메서드
    public void attachFile(BinaryContent file){
        this.attachments = file;
        updateTimestamp();
    }
}
