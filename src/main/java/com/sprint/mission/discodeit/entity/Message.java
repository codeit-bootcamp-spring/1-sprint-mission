package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinTable(
        name = "message_attachments",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
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

    public boolean isSameChannelById(UUID channelId) {
        return this.channel.getId().equals(channelId);
    }

    public boolean isSameAuthorById(UUID authorId) {
        return this.author.getId().equals(authorId);
    }
}
