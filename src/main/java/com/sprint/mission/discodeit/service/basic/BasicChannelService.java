package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.validate.ChannelServiceValidator;
import com.sprint.mission.discodeit.service.validate.MessageServiceValidator;
import com.sprint.mission.discodeit.service.validate.ServiceValidator;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * TODO: 각 서비스별 검증 로직 추가
 */

public class BasicChannelService implements ChannelService {
    private static final EntityFactory entityFactory = BaseEntityFactory.getInstance();
    private final ServiceValidator<Channel> channelValidator = new ChannelServiceValidator();
    private final ServiceValidator<User> userValidator = new UserServiceValidator();
    private final ServiceValidator<Message> messageValidator = new MessageServiceValidator();
    private final ChannelRepository channelRepository;
    private final MessageService messageService;

    public BasicChannelService(ChannelRepository channelRepository, MessageService messageService) {
        this.channelRepository = channelRepository;
        this.messageService = messageService;
    }

    @Override
    public Channel createChannel(String channelName, User owner, Map<UUID, User> userList) {
        if (channelValidator.isNullParam(channelName) || owner == null) {
            throw new ChannelNotFoundException();
        }

        Channel channel = channelValidator.entityValidate(entityFactory.createChannel(channelName, owner, userList));

        channelRepository.saveChannel(channel);

        return channel;
    }

    @Override
    public Map<UUID, Channel> getChannelByName(String channelName) {
        Map<UUID, Channel> allChannels = getAllChannels();

        if (allChannels.isEmpty()) {
            throw new ChannelNotFoundException();
        }

        return allChannels.entrySet().stream()
                .filter(entry -> entry.getValue().getChannelName().equals(channelName))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channelValidator.entityValidate(channelRepository.findChannelById(channelId));
    }

    /**
     * @return Map<UUID, Channel>
     * @Description: channelRepository에서 모든 채널을 가져오는데 없으면 빈 hashMap 반환
     */
    @Override
    public Map<UUID, Channel> getAllChannels() {
        return Optional.ofNullable(channelRepository.findAllChannel())
                .orElse(new HashMap<>());
    }

    @Override
    public Channel updateChannel(UUID channelUUID, String channelName, User changeUser) {
        Channel findChannel = findChannelById(channelUUID);

        findChannel.updateChannelName(channelName);
        findChannel.updateOwnerUser(changeUser);

        return channelRepository.saveChannel(findChannel);
    }

    @Override
    public void removeChannelById(UUID channelUUID) {
        Channel findChannel = findChannelById(channelUUID);

        channelRepository.removeChannelById(findChannel.getChannelId());
    }

    @Override
    public void addUserChannel(UUID channelUUID, User addUser) {
        if (addUser == null) {
            throw new UserNotFoundException();
        }

        Channel findChannel = findChannelById(channelUUID);

        findChannel.addUser(addUser);

        channelRepository.saveChannel(findChannel);
    }

    @Override
    public void kickUserChannel(UUID channelUUID, User kickUser) {
        User wantKickUser = userValidator.entityValidate(kickUser);

        Channel findChannel = findChannelById(channelUUID);

        if (findChannel.getChannelOwnerUser().equals(wantKickUser)) {
            findNextOwnerUser(findChannel);
        }

        findChannel.removeUser(wantKickUser);

        channelRepository.saveChannel(findChannel);
    }

    private void findNextOwnerUser(Channel findChannel) {
        Channel channel = channelValidator.entityValidate(findChannel);

        User nextOwnerUser = channel.getChannelUsers().entrySet().stream()
                .filter(entry -> !entry.getValue().equals(findChannel.getChannelOwnerUser()))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(UserNotFoundException::new);

        channel.updateOwnerUser(nextOwnerUser);
        channel.removeUser(nextOwnerUser);
    }

    // 채널은 메세지를 가지고 있음. 메세지를 추가
    @Override
    public void addMessageInCh(UUID channelId, Message message) {
        Channel findChannel = findChannelById(channelId);
        Message addMessage = findChannel.addMessageInChannel(message);

        // 메세지 저장
        messageService.createMessage(addMessage.getMessageTitle(),
                addMessage.getMessageContent(),
                addMessage.getMessageSendUser(),
                addMessage.getMessageReceiveUser());

        // 채널 저장
        channelRepository.saveChannel(findChannel);
    }

    /**
     * @param channelId
     * @param removeMessage
     * @Description: 메세지 서비스에서 가져온 메세지를 삭제, getMessageById에서 검증할 것임
     */
    @Override
    public void removeMessageInCh(UUID channelId, Message removeMessage) {
        Channel findChannel = findChannelById(channelId);

        Message message = messageValidator.entityValidate(removeMessage);

        messageService.deleteMessage(message.getMessageId()); // 먼저 메시지 삭제

        findChannel.getChannelMessages().remove(message.getMessageId());

        channelRepository.saveChannel(findChannel); // 그 다음 채널 저장
    }

    /**
     * @param channelId
     * @param messageId
     * @return Message
     * @Description: 채널에서 특정 메세지를 ID로 찾기
     */
    @Override
    public Message findChannelMessageById(UUID channelId, UUID messageId) {
        Channel findChannel = findChannelById(channelId);

        return findChannel.getChannelMessages().entrySet().stream()
                .filter(entry -> entry.getKey().equals(messageId))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(MessageNotFoundException::new);

    }

    @Override
    public Map<UUID, Message> findChannelInMessageAll(UUID channelId) {
        Channel findChannel = findChannelById(channelId);

        return findChannel.getChannelMessages();
    }
}
