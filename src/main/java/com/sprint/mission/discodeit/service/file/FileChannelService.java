package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.validate.ChannelServiceValidator;
import com.sprint.mission.discodeit.service.validate.MessageServiceValidator;
import com.sprint.mission.discodeit.service.validate.ServiceValidator;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService, FileService<Channel> {
    private static final String CHANNEL_PATH = "channel.ser";

    private static final EntityFactory ef = BaseEntityFactory.getInstance();
    private Map<UUID, Channel> channelList = new HashMap<>();

    private final ServiceValidator<Channel> channelValidator = new ChannelServiceValidator();
    private final ServiceValidator<User> userValidator = new UserServiceValidator();
    private final ServiceValidator<Message> messageValidator = new MessageServiceValidator();

    @Override
    public Channel createChannel(String channelName, User owner, Map<UUID, User> userList) {
        if (channelValidator.isNullParam(channelName)) {
            throw new IllegalChannelException();
        } else if (owner == null) {
            throw new UserNotFoundException();
        }

        Channel channel = Optional.ofNullable(ef.createChannel(channelName, owner, userList))
                .orElseThrow(IllegalChannelException::new);

        channelList.put(channel.getChannelId(), channel);

        save(CHANNEL_PATH, channelList);

        return channel;
    }

    @Override
    public Map<UUID, Channel> getChannelByName(String channelName) {
        Map<UUID, Channel> findChannel = channelList.entrySet().stream()
                .filter(entry -> entry.getValue().getChannelName().equals(channelName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (findChannel.isEmpty()) {
            throw new ChannelNotFoundException();
        }

        return findChannel;
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channelValidator.entityValidate(channelList.get(channelId));
    }

    @Override
    public Map<UUID, Channel> getAllChannels() {
        return Optional.ofNullable(load(CHANNEL_PATH, channelList))
                .orElseThrow(ChannelNotFoundException::new);

        //return load(CHANNEL_PATH, channelList);
    }

    @Override
    public Channel updateChannel(UUID channelUUID, String channelName, User changeUser) {
        if (channelValidator.isNullParam(channelName)) {
            throw new IllegalChannelException();
        }

        User newOwner = userValidator.entityValidate(changeUser);

        Channel findChannel = findChannelById(channelUUID);

        findChannel.updateChannelName(channelName);
        findChannel.updateOwnerUser(newOwner);

        save(CHANNEL_PATH, channelList);

        return findChannel;
    }

    @Override
    public void removeChannelById(UUID channelUUID) {
        Channel findChannel = findChannelById(channelUUID);

        channelList.remove(findChannel.getChannelId());

        save(CHANNEL_PATH, channelList);
    }

    @Override
    public void addUserChannel(UUID channelUUID, User addUser) {
        User user = userValidator.entityValidate(addUser);

        Channel findChannel = findChannelById(channelUUID);

        findChannel.addUser(user);

        save(CHANNEL_PATH, channelList);
    }

    @Override
    public void kickUserChannel(UUID channelUUID, User kickUser) {
        User user = userValidator.entityValidate(kickUser);

        Channel findChannel = findChannelById(channelUUID);

        if (findChannel.getChannelOwnerUser().equals(user)) {
            findNextOwnerUser(findChannel);
            return;
        }

        findChannel.removeUser(user);

        save(CHANNEL_PATH, channelList);
    }

    private void findNextOwnerUser(Channel findChannel) {
        Channel channel = channelValidator.entityValidate(findChannel);

        User nextOwnerUser = channel.getChannelUsers().entrySet().stream()
                .filter(entry -> !entry.getKey().equals(channel.getChannelOwnerUser().getUserId()))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(UserNotFoundException::new);

        channel.updateOwnerUser(nextOwnerUser);

        channel.removeUser(nextOwnerUser);
    }


    @Override
    public void addMessageInCh(UUID channelId, Message message) {
        Message addMessage = messageValidator.entityValidate(message);

        Channel findChannel = findChannelById(channelId);

        findChannel.addMessageInChannel(addMessage);

        save(CHANNEL_PATH, channelList);
    }

    @Override
    public void removeMessageInCh(UUID channelId, Message removeMessage) {
        Message message = messageValidator.entityValidate(removeMessage);

        Channel findChannel = findChannelById(channelId);

        findChannel.removeMessageInChannel(message.getMessageId());

        save(CHANNEL_PATH, channelList);
    }

    @Override
    public Message findChannelMessageById(UUID channelId, UUID messageId) {
        Channel findChannel = findChannelById(channelId);

        return Optional.ofNullable(findChannel.getChannelMessages().get(messageId))
                .orElseThrow(MessageNotFoundException::new);
    }

    @Override
    public Map<UUID, Message> findChannelInMessageAll(UUID channelId) {
        Channel findChannel = findChannelById(channelId);

        return Optional.ofNullable(findChannel.getChannelMessages())
                .orElse(new HashMap<>());
    }
}
