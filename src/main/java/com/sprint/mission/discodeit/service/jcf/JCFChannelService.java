//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.channel.Channel;
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.factory.BaseEntityFactory;
//import com.sprint.mission.discodeit.factory.EntityFactory;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.validate.ChannelServiceValidator;
//import com.sprint.mission.discodeit.service.validate.MessageServiceValidator;
//import com.sprint.mission.discodeit.service.validate.ServiceValidator;
//import com.sprint.mission.discodeit.service.validate.UserServiceValidator;
//
//import java.util.Map;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//public class JCFChannelService implements ChannelService {
//    private final ChannelRepository channelRepository;
//    private static EntityFactory entityFactory = BaseEntityFactory.getInstance();
//
//    private final ServiceValidator<Channel> validator = new ChannelServiceValidator();
//    private final ServiceValidator<User> userValidator = new UserServiceValidator();
//    private final ServiceValidator<Message> messageValidator = new MessageServiceValidator();
//
//    public JCFChannelService() {
//        this.channelRepository = new JCFChannelRepository();
//    }
//
//    public JCFChannelService(ChannelRepository channelRepository) {
//        this.channelRepository = channelRepository;
//    }
//
//    @Override
//    public Channel createChannel(String channelName, User owner, Map<UUID, User> userList) {
//        validator.isNullParam(channelName);
//        userValidator.entityValidate(owner);
//
//        Channel channel = validator.entityValidate(entityFactory.createChannel(channelName, owner, userList));
//
//        channelRepository.saveChannel(channel);
//        return channel;
//    }
//
//    @Override
//    public Channel findChannelById(UUID id) {
//        return validator.entityValidate(channelRepository.findChannelById(id));
//    }
//
//
//    @Override
//    public Map<UUID, Channel> getAllChannels() {
//        return validator.entityValidate(channelRepository.findAllChannel());
//    }
//
//    @Override
//    public Map<UUID, Channel> getChannelByName(String channelName) {
//        validator.isNullParam(channelName);
//
//        return getAllChannels().entrySet().stream()
//                .filter(entry -> entry.getValue().getChannelName().equals(channelName))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
//    @Override
//    public Channel updateChannel(UUID channelUUID, String channelName, User changeOwnerUser) {
//        validator.isNullParam(channelName);
//        userValidator.entityValidate(changeOwnerUser);
//
//        Channel findChannel = findChannelById(channelUUID);
//
//        findChannel.updateChannelName(channelName);
//        findChannel.updateOwnerUser(changeOwnerUser);
//
//        channelRepository.saveChannel(findChannel);
//
//        return findChannel;
//    }
//
//    @Override
//    public void removeChannelById(UUID removeChannelUUID) {
//        Channel channelById = findChannelById(removeChannelUUID);
//
//        channelRepository.removeChannelById(channelById.getId());
//    }
//
//    @Override
//    public void addUserChannel(UUID channelUUID, User addUser) {
//        userValidator.entityValidate(addUser);
//
//        Channel channelById = findChannelById(channelUUID);
//
//        channelById.addUser(addUser);
//        channelRepository.saveChannel(channelById);
//    }
//
//    @Override
//    public void kickUserChannel(UUID channelUUID, User kickUser) {
//        Channel findChannel = findChannelById(channelUUID);
//
//        User user = userValidator.entityValidate(findChannel.getChannelUsers().get(kickUser.getId()));
//
//        findChannel.removeUser(user);
//
//        if (findChannel.getChannelOwnerUser().equals(user)) {
//            findNextOwnerUser(findChannel);
//        }
//
//        channelRepository.saveChannel(findChannel);
//    }
//
//    private void findNextOwnerUser(Channel findChannel) {
//        Channel channel = findChannelById(findChannel.getId());
//
//
//        User nextOwnerUser = channel.getChannelUsers().entrySet().stream()
//                .filter(entry -> !entry.getKey().equals(channel.getChannelOwnerUser().getId()))
//                .findAny()
//                .map(Map.Entry::getValue)
//                .orElseThrow(() -> new UserNotFoundException("채널에 아무도 없습니다."));
//
//        channel.updateOwnerUser(nextOwnerUser);
//    }
//
//
//    @Override
//    public void addMessageInCh(UUID channelId, Message message) {
//        Message validMessage = messageValidator.entityValidate(message);
//        Channel findChannel = findChannelById(channelId);
//
//        findChannel.addMessageInChannel(validMessage);
//
//        channelRepository.saveChannel(findChannel);
//    }
//
//    @Override
//    public void removeMessageInCh(UUID channelId, Message removeMessage) {
//        Message message = messageValidator.entityValidate(removeMessage);
//        Channel findChannel = findChannelById(channelId);
//
//        findChannel.removeMessageInChannel(message.getId());
//
//        channelRepository.saveChannel(findChannel);
//    }
//
//    @Override
//    public Message findChannelMessageById(UUID channelId, UUID messageId) {
//        Channel findChannel = findChannelById(channelId);
//        return messageValidator.entityValidate(findChannel.getChannelMessages().get(messageId));
//    }
//
//    @Override
//    public Map<UUID, Message> findChannelInMessageAll(UUID channelId) {
//        Channel findChannel = findChannelById(channelId);
//
//        return messageValidator.entityValidate(findChannel.getChannelMessages());
//    }
//}
