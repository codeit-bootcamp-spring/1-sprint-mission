package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.BaseEntity;
import lombok.Getter;

import java.io.Serial;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Getter
public class Message extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 14L;
    private final UUID authorId;
    private final UUID channelId;
    private String content; //메시지 내용
    private Set<UUID> mentionedIds; //멘션된 사용자 리스트

    private List<UUID> attachmentIds;

    public Message(String content, UUID authorId, UUID channelId) {
        super();
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.mentionedIds = new HashSet<>();
    }

    public void addMentions(Set<UUID> mentionedIds) {
        this.mentionedIds.addAll(mentionedIds);
    }

    public void update(String content) {
        if (content != null && !content.trim().isEmpty()) {
            this.content = content;
            updateTimeStamp();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "메시지 {내용='%s', 작성자ID=%s, 채널ID=%s, 작성시간=%s%s%s}",
                content,
                authorId,
                channelId,
                getCreatedAt(),
                mentionedIds.isEmpty() ? "" : ", 맨션=" + mentionedIds,
                getCreatedAt().equals(getUpdatedAt()) ? "" : ", 수정시간=" + getUpdatedAt()
        );
    }
}

