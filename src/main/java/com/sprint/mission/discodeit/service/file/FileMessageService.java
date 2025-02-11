package com.sprint.mission.discodeit.service.file;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private final FileMessageRepository messageRepository;
    private final FileChannelRepository channelRepository;

    public FileMessageService(){
        this.messageRepository = new FileMessageRepository();
        this.channelRepository = FileChannelRepository.getInstance();
        messageRepository.init();
    }

    @Override
    public void createMessage(Message message) {
        try {
            List<Channel> channels = channelRepository.load();
            List<Message> messages = messageRepository.load();
            boolean channelExists = channels.stream()
                    .anyMatch(existingChannel -> existingChannel.getChannelName().equals(message.getChannel().getChannelName()));
            if (channelExists) {
                messages.add(message);
                messageRepository.save(messages);
                System.out.println("메시지가 등록되었습니다.");
            } else {
                throw new NoSuchElementException("해당 채널이 존재하지 않습니다.");
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void updateMessage(Message message, String content, User writer) {
        List<Message> messages = messageRepository.load();
        boolean hasMessage = messages.stream()
                .anyMatch(check -> check.equals(message));
        boolean isWriter = message.getWriter().equals(writer);

        try {
            if (!hasMessage) {
                throw new NoSuchElementException("메시지가 없습니다.");
            } else if (!isWriter) {
                throw new IllegalArgumentException("작성자만 변경할 수 있습니다.");
            } else {
                message.updateContent(content);
                messageRepository.save(messages);
                System.out.println("메시지가 수정되었습니다.");
            }
        }catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteMessage(Message message, User writer) {
        try {
            List<Message> messages = messageRepository.load();
            boolean isRemoved = messages.removeIf(removeMessage -> removeMessage.equals(message) && removeMessage.getWriter().equals(writer));
            if (isRemoved) {
                messageRepository.save(messages);
                System.out.println("메시지가 삭제되었습니다.");
            } else {
                throw new Exception("메시지가 없거나, 작성자가 아닙니다.");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteChannelMessage(Channel channel) {
        try {
            List<Message> messages = messageRepository.load();
            boolean isRemoved = messages.removeIf(removeMessage -> removeMessage.getChannel().equals(channel));
            if (isRemoved) {
                messageRepository.save(messages);
                System.out.println("해당 채널의 메시지가 삭제되었습니다.");
            } else {
                throw new NoSuchElementException("메시지가 삭제되지 않았습니다.");
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> getAllMessage() {
        try {
            List<Message> messages = messageRepository.load();
            if (messages.isEmpty()) {
                throw new NoSuchElementException("저장된 메시지가 없습니다.");
            }
            return messages.stream()
                    .map(Message::getContent)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return List.of();
        }
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
        try {
            if (channelMessages.isEmpty()) {
                throw new NoSuchElementException("해당 채널이 존재하지 않거나 메시지가 없습니다.");
            }
            return channelMessages;
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
            return List.of();
        }
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

        try {
            if (writerMessages.isEmpty()) {
                throw new NoSuchElementException("사용자가 없거나, 메시지가 없습니다.");
            }
            return writerMessages;

        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
