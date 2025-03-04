package some_path._1sprintmission.discodeit.service;

import some_path._1sprintmission.discodeit.dto.MessageCreateRequestDTO;
import some_path._1sprintmission.discodeit.dto.MessageUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequestDTO request);
    Message update(MessageUpdateRequestDTO request);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);

    void delete(UUID messageId);
}