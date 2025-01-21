package com.sprint.mission.discodeit.entity;

// 채팅 메시지 엔티티
public class Message extends BaseEntity {
    private String content; // 메시지 내용
    private final User user; // 메시지를 보낸 사용자
    private final Channel channel; // 메시지가 속한 채널

    // 메시지 내용, 사용자, 채널을 초기화하는 생성자
    public Message(String content, User user, Channel channel) {
        super();
        this.content = content;
        this.user = user;
        this.channel = channel;
    }

    // 메시지 내용을 반환
    public String getContent() {
        return content;
    }

    // 메시지 내용을 설정하고 수정 시간을 업데이트
    public void setContent(String content) {
        this.content = content;
        updateTimestamp();
    }

    // 메시지를 보낸 사용자를 반환
    public User getUser() {
        return user;
    }

    // 메시지가 속한 채널을 반환
    public Channel getChannel() {
        return channel;
    }
}