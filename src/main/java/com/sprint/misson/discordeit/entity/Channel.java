package com.sprint.misson.discordeit.entity;

import java.io.Serializable;
import java.util.*;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
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
    //채널 공개 여부
    private boolean isHidden;

    //채널에 속한 유저 목록
    private final HashMap<UUID, User> userList;

    //추가로 구현해볼만한 것
    //채널 그룹
    //채널 주제
    //접근 권한

    public Channel(String channelName, ChannelType channelType) {
        //id, createdAt, updateAt은 생성자에서 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.channelType = channelType;
        this.userList = new HashMap<>();
    }


    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    //채널 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현
    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    //채널 생성된 이후, 채널 종류를 변경할 수 없으므로 update 미구현
    //TODO - 고민
    //상속시켜서 음성채널, 보이스채널로 따로 구현해야?
    public ChannelType getChannelType() {
        return channelType;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public HashMap<UUID, User> getUserList() {
        return userList;
    }

    public String toShortString() {
        return "[Channel] id: " + id + " / channelName: " + channelName + " / channelType: " + channelType + " / total users: " + userList.size();
    }

    public String toFullString() {
        return toShortString() + " / isHidden: " + isHidden + " / createdAt: " + createdAt + " / updatedAt: " + updatedAt;
    }

    public void displayFullInfo() {
        System.out.println(toFullString());
    }

    public void displayShortInfo() {
        System.out.println(toShortString());
    }

}
