package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasicMessageService implements MessageService {


    MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {

        this.messageRepository = messageRepository;
    }

    @Override
    public void craete(Message message) {

        messageRepository.save(message);
    }

    @Override
    public Message read(UUID id) {

        messageIsExist(id);

        return messageRepository.load().get(id);
    }

    @Override
    public List<Message> readAll() {

        return changeList(messageRepository.load());
    }

    @Override
    public List<Message> readAll(UUID channelID, ChannelService channelService) {

        channelService.channelIsExist(channelID);

        return readAll().stream()
                .filter(message -> message.getChannelId().equals(channelID))
                .toList();
    }

    @Override
    public void updateContext(UUID id, String updateContext) {

        read(id).updateContext(updateContext);
    }

    @Override
    public void delete(UUID id) {

        messageIsExist(id);

        messageRepository.delete(id);
    }

    // 메시지 존재 여부 확인
    @Override
    public void messageIsExist(UUID id) {

        Map<UUID, Message> messages = messageRepository.load();

        if (!messages.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.");
        }
    }

    // 불러온 데이터 List로 변환
    private List<Message> changeList(Map<UUID, Message> map) {

        return map.values().stream().toList();
    }
}
