package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

public class Channel {
    private String UUID;
    private String ServerUUID;
    private String CategoryUUID;
    private String channelName;
    private int maxNumberOfPeople;
    private String tag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPrivate;
    private ChannelBehavior behavior;

    public Channel(String serverUUID,
                   String categoryUUID,
                   String channelName,
                   int maxNumberOfPeople,
                   Boolean isPrivate,
                   String tag,
                   ChannelBehavior behavior
    ) {

        this.ServerUUID = serverUUID;
        this.CategoryUUID = categoryUUID;
        this.channelName = channelName;
        this.maxNumberOfPeople = maxNumberOfPeople;
        this.isPrivate = isPrivate;
        this.UUID = UuidGenerator.generateUUID();
        this.createdAt = LocalDateTime.now();
        this.tag = tag == null ? "일반" : tag;
        this.behavior = behavior;
    }

    public String getUUID() {
        return UUID;
    }

    public void updateUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getServerUUID() {
        return ServerUUID;
    }

    public void updateServerUUID(String serverUUID) {
        ServerUUID = serverUUID;
    }

    public String getCategoryUUID() {
        return CategoryUUID;
    }

    public void updateCategoryUUID(String categoryUUID) {
        CategoryUUID = categoryUUID;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getMaxNumberOfPeople() {
        return maxNumberOfPeople;
    }

    public void updateMaxNumberOfPeople(int maxNumberOfPeople) {
        this.maxNumberOfPeople = maxNumberOfPeople;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateUpdatedAt(LocalDateTime updateTime){
        this.updatedAt = updateTime;
    }


    public Boolean getPrivate() {
        return isPrivate;
    }

    public void updatePrivate(Boolean aPrivate, Channel channel) {
        if(aPrivate) behavior.setChannelPublic(channel);
        else behavior.setChannelPrivate(channel);
    }

    public ChannelBehavior getBehavior(){
        return behavior;
    }

    public String getTag() {
        return tag;
    }

    public void updateTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Channel{" +
            "UUID='" + UUID + '\'' +
            ", ServerUUID='" + ServerUUID + '\'' +
            ", CategoryUUID='" + CategoryUUID + '\'' +
            ", channelName='" + channelName + '\'' +
            ", maxNumberOfPeople=" + maxNumberOfPeople +
            ", tag='" + tag + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", isPrivate=" + isPrivate +
            ", behavior=" + behavior +
            '}';
    }
}
