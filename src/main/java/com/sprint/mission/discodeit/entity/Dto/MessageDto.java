package com.sprint.mission.discodeit.entity.Dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record MessageDto(
        Instant createdAt,
        Instant updatedAt,
        UUID id,
        User fromUser,
        Channel channel,
        String content,
        List<UUID> binaryContents
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getId(),
                message.getAuthor(),
                message.getChannel(),
                message.getContent(),
                Optional.ofNullable(message.getBinaryContentsId()).orElse(Collections.emptyList())
        );
    }

    public record CreateMessageWithAtteachmentsRequest(String userName, String channelName, String content, List<String> attachmentsPaths) {}
    public record CreateMessage(String userName, String channelName, String content) {}
    public record updateAttachmentsRequest(MessageDto messageDto, List<String> attachmentsPaths) {}
    public record updateContent(MessageDto messageDto, String content) {}
    //todo Message는 userName처럼 각 메세지를 구별할수있는 필드가 uuid밖에없는데.. 유저에게서 Dto를 받아도 되나 고민해보고 수정 필요.


}
