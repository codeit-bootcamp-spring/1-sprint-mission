package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "message_attachments")
@Getter @Setter
//@Builder
public class MessageAttachments extends BaseEntity{

    @Column(name = "message_id")
    private UUID messageId;

    @Column(name = "attachment_id")
    private UUID attachmentId;

    protected MessageAttachments() { }
}
