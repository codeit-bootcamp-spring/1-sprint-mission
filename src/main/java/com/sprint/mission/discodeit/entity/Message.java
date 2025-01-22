package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Message extends BaseEntity {
    private String content; //메시지 내용
    private final User author; //작성자
    private List<User> mentions; //멘션된 사용자 리스트
    private final Channel channel; //메시지가 속한 채널

    public Message(String content, User author, Channel channel) {
        super();
        this.content = content;
        this.author = author;
        this.channel = channel;
        this.mentions = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public List<User> getMentions() {
        return mentions;
    }

    public User getAuthor() {
        return author;
    }


    public Channel getChannel() {
        return channel;
    }

    public void update(String content) {
        if (content != null && !content.trim().isEmpty()) {
            this.content = content;
            updateTimeStamp();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("메시지 {").append("내용 = '").append(content).append('\'')
                .append(", 작성자 = ").append(author.getName())
                .append(", 채녈명 = ").append(channel.getName())
                .append(", 작성 시간 = ").append(getCreatedAt());

        if (!mentions.isEmpty()) {
            sb.append(", 맨션 = ").append(mentions.stream()
                    .map(User::getName).toList());
        }

        if (getCreatedAt() != getUpdatedAt()) {
            sb.append(", 수정 시간 = ").append(getUpdatedAt());
        }

        sb.append("}");
        return sb.toString();
    }
}

