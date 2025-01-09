package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.jcf.*;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // User
        userService.createUser("Hazel");
        userService.createUser("ALice");
        System.out.println("Users:");
        userService.getAllUsers().forEach(user ->
                System.out.println(user.getId() + " - " + user.getUsername()));

        // Channel
        channelService.createChannel("General");
        channelService.createChannel("Random");
        System.out.println("\nChannels:");
        channelService.getAllChannels().forEach(channel ->
                System.out.println(channel.getId() + " - " + channel.getChannelName()));


        UUID firstUserId = userService.getAllUsers().get(0).getId();
        UUID generalChannelId = channelService.getAllChannels().get(0).getId();

        // Message 1
        messageService.createMessage("This is my first message", firstUserId, generalChannelId);
        System.out.println("\nMessages:");
        messageService.getAllMessages().forEach(message ->
                System.out.println("User: " + message.getUserId() + ", Channel: " + message.getChannelId() + ", Content: " + message.getContent()));


        UUID secondUserId = userService.getAllUsers().get(1).getId();
        UUID randomChannelId = channelService.getAllChannels().get(1).getId();
        // Message2
        messageService.createMessage("This is my second message", secondUserId, randomChannelId);
        System.out.println("\nMessages:");
        messageService.getAllMessages().forEach(message ->
                System.out.println("User: " + message.getUserId() + ", Channel: " + message.getChannelId() + ", Content: " + message.getContent()));

        // Update user
        userService.updateUser(firstUserId, "AliceUpdated");
        System.out.println("\nUpdated Users:");
        userService.getAllUsers().forEach(user ->
                System.out.println(user.getId() + " - " + user.getUsername()));

        //Delete User
        userService.deleteUser(firstUserId);
        System.out.println("\nRemain Users: ");
        userService.getAllUsers().forEach(user ->
                System.out.println(user.getId() + " - " + user.getUsername()));


        // Delete channel
        channelService.deleteChannel(generalChannelId);
        System.out.println("\nRemaining Channels:");
        channelService.getAllChannels().forEach(channel ->
                System.out.println(channel.getId() + " - " + channel.getChannelName()));

        //Delete message
        messageService.deleteMessage(messageService.getAllMessages().get(0).getId());
        System.out.println("\nRemaining Message: ");
        messageService.getAllMessages().forEach(message ->
                System.out.println("User : " + message.getUserId() + " , Channel : " + message.getChannelId()
                        + "Message : " + message.getContent()));
    }
}
