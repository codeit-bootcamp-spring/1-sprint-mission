package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.*;

public class Message {

    private final UUID msguuId;
    private final Channel destinationCh;
    private final Long createdAt;
    private Long updatedAt;
    private String content;
    private final User SendUser;


    public Message(User SendUser, Channel destinationCh, String content) {
        this.msguuId = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.content = content;
        this.SendUser = SendUser;
        this.destinationCh = destinationCh;
    }

    public UUID getMsguuId() {
        return msguuId;
    }
    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public User  SendUser() {
        return SendUser;
    }

    public Channel getDestinationChannel() {
        return destinationCh;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간을 갱신
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Message{\n" +
                "UUId=" + msguuId +
                ", \ndestinationChannel = " + destinationCh.getChannelName() +
                ", \ncreatedAt = " + sdf.format(new java.util.Date(createdAt)) +
                ", \nupdatedA t= " + sdf.format(new java.util.Date(updatedAt)) +
                ", \ncontent ='" + content + '\'' +
                ", \nsendUser = " + SendUser.getName() +
                "}";
    }

}
