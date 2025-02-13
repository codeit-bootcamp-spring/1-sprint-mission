package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    //객체 식별 id
    private final String id;
    //생성 날짜 - 유닉스 타임스탬프
    private final Instant createdAt;
    //수정 시간
    private Instant updatedAt;
    //메세지 작성자
    private final String senderId;
    //메세지 내용
    private String content;
    //메세지가 생성된 채널
    private final String channelId;
    //첨부 이미지 목록
    private final Set<String> attachmentImageIds = new HashSet<>();

    public Message(User sender, String content, String channelId) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.senderId = sender.getId();
        this.content = content;
        this.channelId = channelId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //메세지가 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    //메세지가 생성된 이후, 메세지를 보낸 채널을 변경할 수 없으므로 update 미구현

    public void addImages(String imageId) {
        this.attachmentImageIds.add(imageId);
    }

    //추후에 추가할 것
    //멘션, 답장(reply)

    public String toShortString() {
        return "[Message] id: " + id + " / sender: " + senderId + " / content: " + content + " / channelId: " + channelId;
    }

    public String toFullString() {
        return toShortString() + " / createdAt: " + createdAt + " / updatedAt: " + updatedAt;
    }

    public void displayFullInfo() {
        System.out.println(toFullString());
    }

    public void displayShortInfo() {
        System.out.println(toShortString());
    }

}
