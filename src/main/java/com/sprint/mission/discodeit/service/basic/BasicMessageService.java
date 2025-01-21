package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.VoiceChannel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageServiceV2;

import java.util.List;
import java.util.Optional;

public class BasicMessageService implements MessageServiceV2<ChatChannel> {

  private static volatile BasicMessageService instance;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  private BasicMessageService(MessageRepository messageRepository
      , UserRepository userRepository
      , ChannelRepository channelRepository) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
    this.channelRepository = channelRepository;
  }

  public static BasicMessageService getInstance(MessageRepository messageRepository
      , UserRepository userRepository
      , ChannelRepository channelRepository) {
    if (instance == null) {
      synchronized (BasicMessageService.class) {
        if (instance == null) {
          instance = new BasicMessageService(messageRepository, userRepository, channelRepository);
        }
      }
    }
    return instance;
  }

  private void isChannelChat(String channelId){
    Optional<BaseChannel> baseChannel = channelRepository.findById(channelId);
    if(baseChannel.isPresent()){
      if(baseChannel.get() instanceof VoiceChannel) throw new IllegalArgumentException("음성 채널은 메시지를 보낼 수 없습니다.");
    }else{
      throw new ChannelNotFoundException();
    }
  }
  @Override
  public Message createMessage(String userId, Message message, ChatChannel channel) throws MessageValidationException {
    if (userRepository.findById(userId).isEmpty()) throw new MessageValidationException();
    if (channelRepository.findById(channel.getUUID()).isEmpty())throw new MessageValidationException();
    isChannelChat(channel.getUUID());
    messageRepository.create(message);
    return message;
  }

  @Override
  public Optional<Message> getMessageById(String messageId, ChatChannel channel) {
    isChannelChat(channel.getUUID());
    return messageRepository.findById(messageId);
  }

  @Override
  public List<Message> getMessagesByChannel(ChatChannel channel) {
    isChannelChat(channel.getUUID());
    return messageRepository.findByChannel(channel.getUUID());
  }

  @Override
  public Message updateMessage(ChatChannel channel, String messageId, MessageUpdateDto updatedMessage) {
    isChannelChat(channel.getUUID());
    Optional<Message> message = messageRepository.findById(messageId);
    if(message.isEmpty()) throw new IllegalArgumentException("메시지가 존재하지 않습니다.");
    Message originalMessage = message.get();
    updatedMessage.getContent().ifPresent(originalMessage::setContent);
    updatedMessage.getContent().ifPresent(originalMessage::setContentImage);
    messageRepository.update(originalMessage);
    return originalMessage;
  }

  @Override
  public boolean deleteMessage(String messageId, ChatChannel channel) {
    isChannelChat(channel.getUUID());
    messageRepository.delete(messageId);
    return true;
  }
}
