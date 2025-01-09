package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String channelName; // 채널명
    private int peopleCount; // 채널에 있는 사람 수
    private User adminUser; // 채널의 관리자

    // 추가적으로 필요한 필드값들은 메소드 만들면서 추가하도록 하자.
    // 지금 넣어봤자 계속 바뀔 것 같다.

    public Channel(String channelName, User adminUser) {
        // 고유한 채널을 식별하기 위한 ID.
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.channelName = channelName;
        this.adminUser = adminUser;

    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void updateChannelName(String newName) {
        channelName = newName;
    }

    public void updateAdminUser(User newUser) {
        adminUser = newUser;
    }

    public void updateUpdatedAt(Long newUpdatedAt) {
        updatedAt = newUpdatedAt;
    }
}
