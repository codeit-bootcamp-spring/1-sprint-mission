package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class BasicMessageService {
    public static Message setupMessage(MessageService messageService, Message messageInfoToCreate) {
        printStartInfo("setupMessage(MessageService, Message)");

        Message message = messageService.createMessage(messageInfoToCreate);

        printArgsAndMessageInfo(messageInfoToCreate.getId(), message, "Already exist!");

        return message;
    }

    public static Message updateMessage(MessageService messageService, UUID key, Message messageInfoToUpdate) {
        printStartInfo("updateMessage(MessageService, UUID, Message)");

        Message message = messageService.updateMessageById(key, messageInfoToUpdate);

        printArgsAndMessageInfo(key, message, "Not exist!");

        return message;
    }

    public static Message searchMessage(MessageService messageService, UUID key) {
        printStartInfo("searchMessage(MessageService, UUID)");

        Message message = messageService.findMessageById(key);

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
        System.out.println("---------------------------------");
        System.out.println(startInfo);
    }

    private static void printArgsAndMessageInfo(UUID key, Message message, String messageWhenEmpty) {
        System.out.println("pass UUID '" + key + "'! ");
        if (message == Message.EMPTY_MESSAGE) {
            System.out.println(messageWhenEmpty);
        }
        System.out.println("Message info: " + message);
        System.out.println();
    }
}
