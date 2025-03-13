package com.sprint.mission.dto.mappedDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UserDto author,
        List<BinaryContentDto> attachments) {

}

//public class Message  extends BaseUpdatableEntity{
//
/// /    @ToString.Exclude
/// /    private static final long serialVersionUID = 1L;
//
//    private String content;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "channel_id")
//    private Channel channel;
//
//    // 메시지는 user가 삭제되도 남기기?
//    @ManyToOne(fetch = LAZY)
//    private User author;
//
//    @OneToMany(cascade = REMOVE, orphanRemoval = true)
//    @JoinColumn(name = "message_id")
//    private List<BinaryContent> attachments = new ArrayList<>();