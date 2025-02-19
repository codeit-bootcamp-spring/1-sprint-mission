package com.sprint.mission.discodeit.repository.message;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.repository.message.interfaces.MessageRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    @Override
    public Message save(Message message) {
        return null;
    }

    @Override
    public Optional<Message> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void deleteByChannel(Channel channel) {

    }

    @Override
    public LocalDateTime getLastMessageTimeByChannelId(UUID channelId) {
        return null;
    }
}
