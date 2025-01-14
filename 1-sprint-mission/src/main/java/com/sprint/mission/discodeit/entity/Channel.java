package com.sprint.mission.discodeit.entity;

public class Channel extends Data {

    private String channelTitle;
    private String channelId;
    private Long createdAt;
    private Long updatedAt;

    public Channel(String channelTitle) {
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.createdAt = System.currentTimeMillis(); // 생성 시간
        this.updatedAt = this.createdAt;           // 초기 생성 시간과 동일
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }
}
