//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.exception.message.NullMessageTitleException;
//import com.sprint.mission.discodeit.factory.BaseEntityFactory;
//import com.sprint.mission.discodeit.factory.EntityFactory;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.validate.FileMessageServiceValidator;
//import com.sprint.mission.discodeit.service.validate.ServiceValidator;
//import com.sprint.mission.discodeit.service.validate.UserServiceValidator;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class FileMessageService implements MessageService, FileService<Message> {
//    private static final String MESSAGE_PATH = "message.ser";
//
//    private static final EntityFactory ef = BaseEntityFactory.getInstance();
//
//    private final ServiceValidator<Message> messageValidator = new FileMessageServiceValidator();
//
//    private final ServiceValidator<User> userValidator = new UserServiceValidator();
//
//    private Map<UUID, Message> messageList = new HashMap<>();
//
//
//    @Override
//    public Message createMessage(String title, String content, User sender, User receiver) {
//        if (messageValidator.isNullParam(title)) {
//            throw new NullMessageTitleException();
//        }
//
//        User sendUser = userValidator.entityValidate(sender);
//        User receiveUser = userValidator.entityValidate(receiver);
//
//        Map<UUID, Message> messages = messageValidator.entityValidate(load(MESSAGE_PATH, messageList));
//
//        Message message = messageValidator.entityValidate(ef.createMessage(title, content, sendUser, receiveUser));
//
//        Map<UUID, Message> allMessages = getAllMessages();
//        allMessages.put(message.getId(), message);
//        save(MESSAGE_PATH, allMessages);
//
//        return message;
//    }
//
//    @Override
//    public Message getMessageById(UUID messageId) {
//        Map<UUID, Message> messages = getAllMessages();
//
//        return messageValidator.entityValidate(messages.get(messageId));
//    }
//
//    @Override
//    public Map<UUID, Message> getAllMessages() {
//        return messageValidator.entityValidate(load(MESSAGE_PATH, messageList));
//    }
//
//    @Override
//    public Message updateMessage(UUID messageId, String newTitle, String newContent) {
//        if(messageValidator.isNullParam(newTitle,newContent)) {
//            throw new NullMessageTitleException();
//        }
//
//        Message findMessage = getMessageById(messageId);
//
//        findMessage.updateMessageTitle(newTitle);
//        findMessage.updateMessageContent(newContent);
//
//        save(MESSAGE_PATH, messageList);
//
//        return findMessage;
//    }
//
//    @Override
//    public void deleteMessage(UUID messageId) {
//        Map<UUID, Message> allMessages = getAllMessages();
//
//        Message findMessage = messageValidator.entityValidate(allMessages.get(messageId));
//
//        allMessages.remove(findMessage.getId());
//
//        save(MESSAGE_PATH, allMessages);
//    }
//}
