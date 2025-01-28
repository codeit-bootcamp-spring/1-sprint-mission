package com.sprint.misson.discordeit.entity;

import com.sprint.misson.discordeit.dto.ChannelDTO;

import java.io.Serializable;
import java.util.*;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    //객체 식별 id
    private final String id;
    //채널명
    private String channelName;
    //생성 날짜 - 유닉스 타임스탬프
    private final Long createdAt;
    //수정 시간
    private Long updatedAt;

    //채널 종류 - 음성, 텍스트
    private final ChannelSort channelSort;

    //채널 공개 여부
    private ChannelType channelType;

    private String description;

    //채널에 속한 유저 목록
    private final HashMap<String, User> userList;

    //추가로 구현해볼만한 것
    //채널 그룹
    //채널 주제
    //접근 권한

    public Channel(String channelName, ChannelType channelType, String description) {
        //id, createdAt, updateAt은 생성자에서 초기화
        this.id = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.channelSort = ChannelSort.TEXT;
        this.channelType = channelType;
        this.description = description;
        this.userList = new HashMap<>();
    }


    public String getId() {
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

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    //채널 생성된 이후, 채널 종류를 변경할 수 없으므로 update 미구현
    //TODO - 고민
    //상속시켜서 음성채널, 보이스채널로 따로 구현해야?
    public ChannelSort getChannelSort() {
        return channelSort;
    }

    public ChannelType getChannelType(){ return channelType; }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, User> getUserList() {
        return userList;
    }

    public String toShortString() {
        return "[Channel] id: " + id + " / channelName: " + channelName + " / channelType: " + channelType + " / total users: " + userList.size();
    }

    public String toFullString() {
        return toShortString() + " / channelSort: " + channelSort + " / description: " + description + " / createdAt: " + createdAt + " / updatedAt: " + updatedAt;
    }

    public void displayFullInfo() {
        System.out.println(toFullString());
    }

    public void displayShortInfo() {
        System.out.println(toShortString());
    }

    public boolean isUpdated(ChannelDTO channelDTO) {
        //변경 여부 체크
        boolean isUpdated = false;

        String newChannelName = channelDTO.getChannelName();
        if (newChannelName != null && !newChannelName.isEmpty() && !newChannelName.equals(channelName)) {
            channelName = newChannelName;
            isUpdated = true;
        }

        ChannelType newChannelType = channelDTO.getChannelType();
        if (newChannelType != null && channelType != newChannelType) {
            channelType = newChannelType;
            isUpdated = true;
        }

        String newDescription = channelDTO.getDescription();
        if (newDescription != null && !newDescription.isEmpty() && !newDescription.equals(description)) {
            description = newDescription;
            isUpdated = true;
        }

        return isUpdated;
    }

}
