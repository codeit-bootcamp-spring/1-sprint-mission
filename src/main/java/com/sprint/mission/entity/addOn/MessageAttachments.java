package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.Message;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "message_attachments")
public class MessageAttachments {

    @Id @GeneratedValue(strategy = UUID)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    private BinaryContent binaryContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;
}
