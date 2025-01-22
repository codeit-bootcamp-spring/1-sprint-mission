package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService(userService);
        MessageService messageService = new JCFMessageService(channelService, userService);

        // Create Users
        User alex = userService.createUser("alex00");
        User bob = userService.createUser("bob99");
        User chris = userService.createUser("00chris");
        User david = userService.createUser("0david0");

        System.out.println("Users created:");
        System.out.println("User " + alex.getId() + ", " + "username: " + alex.getUsername() + ", createdAt: " + alex.getCreatedAt());
        System.out.println("User " + bob.getId() + ", " + "username: " + bob.getUsername() + ", createdAt: " + bob.getCreatedAt());
        System.out.println("User " + chris.getId() + ", " + "username: " + chris.getUsername() + ", createdAt: " + chris.getCreatedAt());
        System.out.println("User " + david.getId() + ", " + "username: " + david.getUsername() + ", createdAt: " + david.getCreatedAt());

        // Create Channels
        Channel alexChannel = channelService.createChannel("Alex and friends", alex.getId());
        Channel chrisChannel = channelService.createChannel("Chris and friends", chris.getId());

        System.out.println("\nChannel created:");
        System.out.println("  Channel 1 Name: " + alexChannel.getName());
        System.out.println("  Owner: " + userService.getUserById(alexChannel.getOwnerId()).getUsername());
        System.out.println("  Channel 2 Name: " + chrisChannel.getName());
        System.out.println("  Owner: " + userService.getUserById(chrisChannel.getOwnerId()).getUsername());

        // Add Bob to alex's channel
        channelService.addUser(alexChannel.getId(), bob.getId());
        System.out.println("\nBob added to alex's channel.");

        // Add David to chris's channel
        channelService.addUser(chrisChannel.getId(), david.getId());
        System.out.println("\nDavid added to chris's channel.");

        // Send Messages in alex's channel
        Message message1 = messageService.createMessage(alexChannel.getId(), alex.getId(), "Hello, everyone!");
        Message message2 = messageService.createMessage(alexChannel.getId(), bob.getId(), "Hi Alice!");

        System.out.println("\nMessages created:");
        System.out.println("  Message 1: " + message1.getContent());
        System.out.println("  Message 2: " + message2.getContent());

        // Display All Messages in alex's channel
        System.out.println("\nMessages in Alex's channel:");
        List<Message> messages = messageService.getChannelMessages(alexChannel.getId());
        for (Message message : messages) {
            String authorName = userService.getUserById(message.getAuthorId()).getUsername();
            System.out.println("  [" + authorName + "]: " + message.getContent());
        }

        // Update a Message
        System.out.println("\nUpdating Alex's message.");
        messageService.updateMessage(alexChannel.getId(), message1.getId(), "Hello, everyone! Edited.");
        System.out.println("Messages in Alex's channel:");
        messages = messageService.getChannelMessages(alexChannel.getId());
        for (Message message : messages) {
            String authorName = userService.getUserById(message.getAuthorId()).getUsername();
            System.out.println("  [" + authorName + "]: " + message.getContent());
        }

        // Delete a Message
        messageService.deleteMessage(alexChannel.getId(), message2.getId());
        System.out.println("\nMessage 2 deleted.");
        messages = messageService.getChannelMessages(alexChannel.getId());
        for (Message message : messages) {
            String authorName = userService.getUserById(message.getAuthorId()).getUsername();
            System.out.println("  [" + authorName + "]: " + message.getContent());
        }
    }
}
