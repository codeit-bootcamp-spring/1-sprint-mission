package com.sprint.mission.discodeit.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "message_attachments")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_attachment_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id")
    private BinaryContent binaryContent;

}
