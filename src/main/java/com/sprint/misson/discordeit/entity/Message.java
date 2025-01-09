package com.sprint.misson.discordeit.entity;

import java.util.UUID;

public class Message {
    //객체 식별 id
    private final UUID id;
    //생성 날짜 - 유닉스 타임스탬프
    private final Long createdAt;
    //수정 시간
    private Long updatedAt;
    //메세지 작성자
    private final User sender;
    //메세지 내용
    private String content;
    //메세지가 생성된 채널
    private final Channel channel;

    public Message(User sender, String content, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.sender = sender;
        this.content = content;
        this.channel = channel;
    }

    public UUID getId() {
        return id;
    }
    
    //메세지가 생성된 이후, 보낸 사람을 변경할 수 없으므로 update 미구현
    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content=content;
    }

    //메세지가 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현
    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt=updatedAt;
    }

    //메세지가 생성된 이후, 메세지를 보낸 채널을 변경할 수 없으므로 update 미구현
    public Channel getChannel() {
        return channel;
    }


    //추후에 추가할 것
    //멘션, 답장(reply)

}
