package com.sprint.misson.discordeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    //객체 식별 id
    private final UUID id;
    //채널명
    private String channelName;
    //생성 날짜 - 유닉스 타임스탬프
    private final Long createdAt;
    //수정 시간
    private Long updatedAt;

    //채널 종류 - 음성, 텍스트
    private final ChannelType channelType;

    //채널에 있는 메세지 목록
    private final List<Message> messages;
    // List<> 필드를 final 로 할 경우,
    // List 인스턴스는 다른 인스턴스로 못 바꾸지만
    // List 인스턴스가 담고 있는 내용은 변경 가능하다.

    //추가로 구현해볼만한 것
    //채널 그룹
    //채널 주제
    //접근 권한
    //비공개

    public Channel(String channelName, ChannelType channelType) {
        //id, createdAt, updateAt은 생성자에서 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.channelType = channelType;
        this.messages = new ArrayList<>();
    }


    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName=channelName;
    }

    //채널 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현
    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt=updatedAt;
    }

    //채널 생성된 이후, 채널 종류를 변경할 수 없으므로 update 미구현
    //TODO - 고민
    //상속시켜서 음성채널, 보이스채널로 따로 구현해야?
    public ChannelType getChannelType() {
        return channelType;
    }

    //채널 생성된 이후, 메세지 리스트 인스턴스를 변경할 수 없으므로 update 미구현
    public List<Message> getMessages() {
        return messages;
    }

}
