package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    public Message createMessage(CreateMessageRequest createMessageRequest,
                                 List<MultipartFile> multipartFileList);

    Message findMessageByIdOrThrow(UUID messageId);

    List<Message> findAllMessagesByChannelId(UUID channelId);

    Message updateMessage(UUID messageId, UpdateMessageRequest updateMessageRequest,
                          List<MultipartFile> multipartFileList);

    void deleteMessage(UUID messageId, DeleteMessageRequest deleteMessageRequest);
}
