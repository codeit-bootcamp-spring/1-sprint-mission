package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;

import java.util.Map;

public class Message {
    private String UUID;
    private String userUUID;
    private String channelUUID;
    private String content;
    private Boolean isEdited;
    private Long createdAt;
    private Long updatedAt;
    private String contentImage;
    private String threadUUID;
    private Map<String, Reactions> messageReactions;

    public Message(
                   String userUUID,
                   String channelUUID,
                   String content,
                   String contentImage,
                   String threadUUID) {

        this.UUID = UuidGenerator.generateUUID();
        this.userUUID = userUUID;
        this.channelUUID = channelUUID;
        this.content = content;
        this.contentImage = contentImage == null ? "" : contentImage;
        this.threadUUID = threadUUID == null ? "" : threadUUID;
        this.createdAt = System.currentTimeMillis();
    }

    public String getUUID() {
        return UUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getChannelUUID() {
        return channelUUID;
    }

    public String getContent() {
        return content;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getContentImage() {
        return contentImage;
    }

    public String getThreadUUID() {
        return threadUUID;
    }

    public Map<String, Reactions> getMessageReactions() {
        return messageReactions;
    }
    public void setContent(String content) {
        this.content = content;
        this.isEdited = true;
        this.updatedAt = System.currentTimeMillis();
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage == null ? "" : contentImage;
    }

    public void setThreadUUID(String threadUUID) {
        this.threadUUID = threadUUID;
    }

    public void addReaction(String userUUID, Reactions reaction) {
        messageReactions.put(userUUID, reaction);
    }

    public void removeReaction(String userUUID) {
        messageReactions.remove(userUUID);
    }

    public void setIsEdited(){
        this.isEdited = true;
    }

    @Override
    public String toString() {
        return "Message{" +
            "UUID='" + UUID + '\'' +
            ", userUUID='" + userUUID + '\'' +
            ", channelUUID='" + channelUUID + '\'' +
            ", content='" + content + '\'' +
            ", contentImage='" + contentImage + '\'' +
            '}';
    }
}
