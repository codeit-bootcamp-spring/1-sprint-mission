package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.logger.service.ServiceLogger;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.UUID;

public class BasicMessageService {

    private static final ServiceLogger logger = ServiceLogger.getInstance();

    public static Message setupMessage(MessageService messageService, Message messageInfoToCreate) {
        printStartInfo("setupMessage(MessageService, Message)");

        Message message = messageService.registerMessage(messageInfoToCreate);

        printArgsAndMessageInfo(messageInfoToCreate.getId(), message, "Already exist!");

        return message;
    }

    public static Message updateMessage(MessageService messageService, UUID key,
        Message messageInfoToUpdate) {
        printStartInfo("updateMessage(MessageService, UUID, Message)");

        Message message = messageService.updateMessageById(key, messageInfoToUpdate);

        printArgsAndMessageInfo(key, message, "Not exist!");

        return message;
    }

    public static Message searchMessage(MessageService messageService, UUID key) {
        printStartInfo("searchMessage(MessageService, UUID)");

        Message message = messageService.searchMessageById(key);

        printArgsAndMessageInfo(key, message, "Not exist!");

        return message;
    }

    public static Message removeMessage(MessageService messageService, UUID key) {
        printStartInfo("removeMessage(MessageService, UUID)");

        Message message = messageService.deleteMessageById(key);

        printArgsAndMessageInfo(key, message, "Not exist!");

        return message;
    }

    private static void printStartInfo(String startInfo) {
        logger.info("---------------------------------");
        logger.info(startInfo);
    }

    private static void printArgsAndMessageInfo(UUID key, Message message,
        String messageWhenEmpty) {
        logger.info("pass UUID '" + key + "'! ");
        if (message == Message.EMPTY_MESSAGE) {
            logger.info(messageWhenEmpty);
        }
        logger.info("Message info: " + message);
    }
}
