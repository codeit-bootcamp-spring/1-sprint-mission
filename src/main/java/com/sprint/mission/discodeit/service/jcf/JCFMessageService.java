package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final JCFMessageRepository messageRepository;
    private final JCFChannelRepository channelRepository;

    public JCFMessageService() {
        this.messageRepository = new JCFMessageRepository();
        this.channelRepository = JCFChannelRepository.getInstance();
    }

    @Override
    public void createMessage(Message message) {
        try {
            if (channelRepository.exists(message.getChannel())) {
                messageRepository.save(message);
                System.out.println("메시지가 등록되었습니다.");
            } else {
                throw new NoSuchElementException("해당 채널이 없습니다.");
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateMessage(Message message, String content, User writer) {
        try {
            List<Message> messages = messageRepository.load();
            boolean hasMessage = messages.stream().anyMatch(check -> check.equals(message));
            boolean isWriter = message.getWriter().equals(writer);

            if (!hasMessage) {
                throw new NoSuchElementException("메시지가 없습니다.");
            } else if (!isWriter) {
                throw new IllegalArgumentException("작성자만 변경할 수 있습니다.");
            } else {
                message.updateContent(content);
                messageRepository.save(message);
                System.out.println("메시지가 수정되었습니다.");
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteMessage(Message message, User writer) {
        try {
            Optional<Message> existingMessageOpt = messageRepository.findById(message.getId());
            if (existingMessageOpt.isPresent()) {
                Message existingMessage = existingMessageOpt.get();
                if (existingMessage.getWriter().equals(writer)) {
                    messageRepository.delete(existingMessage);
                    System.out.println("메시지가 삭제되었습니다.");
                } else {
                    throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
                }
            } else {
                throw new NoSuchElementException("메시지가 존재하지 않습니다.");
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteChannelMessage(Channel channel) {
        List<Message> messages = messageRepository.load();
        List<Message> channelMessages = messages.stream()
                .filter(message -> message.getChannel().equals(channel))
                .toList();

        if (!channelMessages.isEmpty()) {
            for (Message msg : channelMessages) {
                messageRepository.delete(msg);
            }
            System.out.println("해당 채널의 메시지가 삭제되었습니다.");
        } else {
            System.out.println("삭제할 메시지가 없습니다.");
        }
    }

    @Override
    public List<String> getAllMessage() {
        return messageRepository.load().stream()
                .map(Message::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, String>> getChannelMessage(Channel channel) {
        List<Message> messages = messageRepository.load();
        List<Map<String, String>> channelMessages = messages.stream()
                .filter(channelCheck -> channelCheck.getChannel().equals(channel))
                .map(message -> {
                    Map<String, String> channelMessage = new HashMap<>();
                    channelMessage.put("작성자", message.getWriter().getUserName());
                    channelMessage.put("메시지 내용", message.getContent());
                    return channelMessage;
                }).collect(Collectors.toList());

        if (channelMessages.isEmpty()) {
            System.out.println("해당 채널이 존재하지 않거나 메시지가 없습니다.");
        }
        return channelMessages;
    }

    @Override
    public List<Map<String, String>> getWriterMessage(User user) {
        List<Message> messages = messageRepository.load();
        List<Map<String, String>> writerMessages = messages.stream()
                .filter(writerCheck -> writerCheck.getWriter().equals(user))
                .map(message -> {
                    Map<String, String> writerMessage = new HashMap<>();
                    writerMessage.put("채널", message.getChannel().getChannelName());
                    writerMessage.put("메시지 내용", message.getContent());
                    return writerMessage;
                }).collect(Collectors.toList());

        if (writerMessages.isEmpty()) {
            System.out.println("사용자가 없거나, 메시지가 없습니다.");
        }
        return writerMessages;
    }
}
