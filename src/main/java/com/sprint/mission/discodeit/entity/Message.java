package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.error.execption.bianryContent.BinaryContentNotNullException;
import com.sprint.mission.discodeit.global.error.execption.channel.ChannelNotNullException;
import com.sprint.mission.discodeit.global.error.execption.user.UserNotNullException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {

    private final User sender;
    private final Channel channel;
    private String content;
    private final List<BinaryContent> binaryContentList = new ArrayList<>();

    private Message(User sender, Channel channel, String content) {
        super();
        this.sender = sender;
        this.channel = channel;
        this.content = content;
    }

    public static Message of(User sender, Channel channel, String content) {
        if (sender == null) {
            throw new UserNotNullException();
        }
        if (channel == null) {
            throw new ChannelNotNullException();
        }
        return new Message(sender, channel, content);
    }

    public void updateContent(String content) {
        this.content = content;
        this.updateUpdatedAt();
    }

    public boolean isNotOwner(UUID senderId) {
        return !(this.sender.getId().equals(senderId));
    }

    public void addBinaryContent(BinaryContent binaryContent) {
        if (binaryContent == null) {
            throw new BinaryContentNotNullException();
        }
        this.binaryContentList.add(binaryContent);
        this.updateUpdatedAt();
    }

    public void deleteBinaryContent(BinaryContent binaryContent) {
        if (binaryContent == null) {
            throw new BinaryContentNotNullException();
        }
        this.binaryContentList.remove(binaryContent);
        this.updateUpdatedAt();
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", channel=" + channel +
                ", content='" + content + '\'' +
                '}';
    }
}
