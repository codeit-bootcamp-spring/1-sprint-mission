package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseUpdatableEntity {
    // TODO: authorId, channelId, attachmentsIds 변수 및 관련 로직 제거
    private final UUID authorId;
    private final UUID channelId;
    private List<UUID> attachmentsIds;

    private String content;

    private Channel channel;
    private User author;
    private List<BinaryContent> attachments;

    public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
        this.content = content;

        this.channel = channel;
        this.author = author;
        this.attachments = attachments;
    }

    public void updateContent(String content) {
        if (!this.content.equals(content)) {
            this.content = content;
        }
    }

    public boolean isSameChannelId(UUID channelId) {
        return this.channelId.equals(channelId);
    }

    public boolean isSameAuthorId(UUID authorId) {
        return this.authorId.equals(authorId);
    }
}
