package discodeit.service.basic;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.repository.MessageRepository;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class BasicMessageService implements MessageService {
    private MessageRepository messageRepository;
    private UserService userService;
    private ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void setService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        try {
            Channel channel = channelService.findById(channelId);
            User user = userService.findById(userId);

            if (!channel.getUsers().contains(userId)) {
                System.out.println("메세지를 보낼 수 없습니다: " +
                        user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
                return null;
            }

            Message message = new Message(userId, channelId, content);
            messageRepository.save(message);
            System.out.println("메세지 전송 완료: " + message.getContent());
            return message.getId();
        } catch (NoSuchElementException e) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 않습니다. " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getMessageById(UUID messageId) {
        try {
            Message message = findById(messageId);
            Channel channel = channelService.findById(message.getChannelId());
            String formattedMessage = channel.getName() + " > ";
            try {
                User user = userService.findById(message.getSenderId());
                if (channelService.checkUserInChannel(user.getId(), channel.getId())) {
                    formattedMessage += user.getName() + ": ";
                } else {
                    formattedMessage += user.getName() + "(퇴장한 사용자): ";
                }
            } catch (NoSuchElementException e) {
                formattedMessage += "알 수 없는 사용자: ";
            }
            formattedMessage += message.messageInfoToString();
            return formattedMessage;
        } catch (NoSuchElementException e) {
            System.out.println("올바르지 않은 데이터입니다. " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getAllMessages() {
        Map<UUID, Message> data = messageRepository.findAll();
        if (data == null || data.size() == 0) return null;
        List<String> formattedMessages = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(message -> message.getUpdatedAt()))
                .forEach(message -> {
                    Channel channel = channelService.findById(message.getChannelId());
                    String formattedMessage = channel.getName() + " > ";
                    // 유저가 채널을 나간 경우 '퇴장한 사용자' 표시, 유저가 삭제된 경우 '알 수 없는 사용자' 표시
                    /*
                        유저 계정이 삭제되어도 메세지는 남아 있음.
                        단, 이름을 알기 위해서는 유저 객체를 호출해야 하는데 삭제된 경우
                        NoSuchElementExecption이 발생함.
                        이 경우를 처리하기 위해 catch문에서 "알 수 없는 사용자"로 표시함.
                    */
                    try {
                        User user = userService.findById(message.getSenderId());
                        if (channelService.checkUserInChannel(user.getId(), channel.getId())) {
                            formattedMessage += user.getName() + ": ";
                        } else {
                            formattedMessage += user.getName() + "(퇴장한 사용자): ";
                        }
                    } catch (NoSuchElementException e) {
                        formattedMessage += "알 수 없는 사용자: ";
                    }
                    formattedMessage += message.messageInfoToString();
                    formattedMessages.add(formattedMessage);
                });
        return formattedMessages;
    }


    @Override
    public void updateMessage(UUID userId, UUID messageId, String newContent) {
        try {
            Message message = findById(messageId);

            // 메시지 작성자인 경우에만 수정 가능
            if (!message.getSenderId().equals(userId)) {
                System.out.println("메세지 작성자만 수정 가능합니다.");
                return;
            }
            message.updateContent(newContent);
            messageRepository.save(message);
            System.out.println("메세지가 수정되었습니다.: " + newContent);
        } catch (NoSuchElementException e) {
            System.out.println("메세지 또는 사용자 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        try {
            messageRepository.delete(messageId);
            System.out.println("메세지가 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " + e.getMessage());
        }
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<String> getMessagesByChannelId(UUID channelId) {
        Map<UUID, Message> data = messageRepository.findAll();
        if (data == null || data.size() == 0) return null;
        // 특정 채널에 속하는 메세지 찾기
        List<Message> messagesInChannel = data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))    // 각 메세지가 가진 채널 ID로 판별
                .sorted(Comparator.comparing(message -> message.getUpdatedAt()))    // 메세지는 시간순으로 정렬
                .collect(Collectors.toList());
        List<String> formattedMessages = new ArrayList<>();
        // 형식) {작성자 이름}: {내용} {시간, 수정 여부}
        for (Message message : messagesInChannel) {
            String formattedMessage = "";
            try {
                User user = userService.findById(message.getSenderId());
                if (channelService.checkUserInChannel(user.getId(), channelId)) {
                    formattedMessage += user.getName() + ": ";
                } else {
                    formattedMessage += user.getName() + "(퇴장한 사용자): ";
                }
            } catch (NoSuchElementException e) {
                formattedMessage += "알 수 없는 사용자: ";
            }
            formattedMessage += message.messageInfoToString();
            formattedMessages.add(formattedMessage);
        }
        return formattedMessages;
    }

    @Override
    public void deleteMessagesInChannel(UUID channelId) {
        Map<UUID, Message> data = messageRepository.findAll();
        if (data == null) return;
        data.values().stream()
                .forEach(message -> {
                    // 해당 채널에 쓰여진 메세지 객체를 찾고
                    if (message.getChannelId().equals(channelId)) {
                        // 그 객체를 레포지토리에서 삭제
                        messageRepository.delete(message.getId());
                    }
                });
    }
}
