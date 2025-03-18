package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User author;

    @BatchSize(size = 100)
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
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
