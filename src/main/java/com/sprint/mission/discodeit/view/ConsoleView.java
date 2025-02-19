package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ConsoleView {
    public void displayUser(UserResponse user) {
        if (user != null) {
            System.out.println(user.username() + "의 조회 내역");
            System.out.println(user.username() + "'s UUID : " + user.id() + "\n" +
                    ", Email : " + user.email() +"\n" +
                    ", Currently Logged In : " + user.isCurrentlyLoggedIn() +"\n" +
                    ", CreatedAt : " + user.createdAt() +"\n");
        } else {
            System.out.println("해당 ID의 유저를 찾을 수 없습니다.");
        }
    }

    public void displayUsers(List<UserResponse> users) {
        System.out.println("=== 전체 유저 조회 ===");
        if (!users.isEmpty()) {
            for (UserResponse user : users) {
                displayUser(user);
            }
        } else {
            System.out.println("현재 등록된 유저가 없습니다.");
        }
    }

    public void displayChannel(Channel channel, UserResponse user) {
        if (channel != null) {
            if (user != null) {
                System.out.println("채널이름: " + channel.getChannelName() +"\n" +
                        ", 유저이름: " + user.username() +"\n" +
                        ", 유저 ID: " + user.id() +"\n" +
                        ", 채널 UUID: " + channel.getId() +"\n" +
                        ", CreatedAt: " + channel.getCreatedAt() +"\n" +
                        ", UpdatedAt: " + channel.getUpdatedAt()+"\n");
            } else {
                System.out.println("채널이름: " + channel.getChannelName() +"\n" +
                        ", 채널 UUID: " + channel.getId() +"\n" +
                        ", CreatedAt: " + channel.getCreatedAt() +"\n" +
                        ", UpdatedAt: " + channel.getUpdatedAt()+"\n");
            }
        } else {
            System.out.println("채널 정보를 찾을 수 없습니다.");
        }
    }

    public void displayChannels(List<Channel> channels) {
        System.out.println("=== 전체 채널 조회 ===");

        if (!channels.isEmpty()) {
            for (Channel channel : channels) {
                displayChannel(channel, null);
            }
        } else {
            System.out.println("현재 등록된 채널이 없습니다.");
        }
    }

    public void displayMessage(Message message, String channelName, UserResponse user) {
        if (message != null) {
            System.out.println("User: " + (user != null ? user.username() : "Unknown") + "\n" +
                    ", Message Content: " + message.getContent() + "\n" +
                    ", Channel Name: " + (channelName != null ? channelName : "Unknown") + "\n" +
                    ", Channel UUID: " + message.getChannelId() + "\n" +
                    ", Message UUID: " + message.getId() + "\n" +
                    ", Created At: " + message.getCreatedAt() + "\n" +
                    ", Updated At: " + message.getUpdatedAt() + "\n");
        } else {
            System.out.println("해당 ID의 메시지를 찾을 수 없습니다.");
        }
    }



    public void displayMessages(List<Message> messages, List<Channel> channels, UserService userService) {
        System.out.println("=== 전체 메시지 조회 ===");

        if (!messages.isEmpty()) {
            for (Message message : messages) {
                String channelName = findChannelName(message.getChannelId(), channels);
                UserResponse user = userService.getUserById(message.getSenderId());
                displayMessage(message, channelName, user);
            }
        } else {
            System.out.println("현재 등록된 메시지가 없습니다.");
        }
    }

    public void displayAddUserToChannelSuccess(Channel channel, UserResponse user) {
        System.out.println("Success: 유저를 채널에 추가했습니다. " +"\n" +
                "채널이름: " + channel.getChannelName() +"\n" +
                ", 유저이름: " + user.username() +"\n" +
                ", 유저 ID: " + user.id()+"\n");
    }

    public void displaySimpleUser(UserResponse user) {
        if (user != null) {
            System.out.println("사용자 이름: " + user.username());
        }
    }

    public void displayExistingData(List<UserResponse> users, List<Channel> channels) {
        System.out.println("\n=== 기존의 내역 ===");
        System.out.println("# 사용자 목록");
        for (UserResponse user : users) {
            displaySimpleUser(user);
        }

        System.out.println("\n# 채널 목록");
        for (Channel channel : channels) {
            displaySimpleChannel(channel);
        }
        System.out.println("");
    }

    private String findChannelName(UUID channelId, List<Channel> channels) {
        for (Channel channel : channels) {
            if (channel.getId().equals(channelId)) {
                return channel.getChannelName();
            }
        }
        throw new IllegalArgumentException("Channel not found with id: " + channelId);
    }

    public void displayError(String message) {
        System.out.println("Error: " + message);
    }

    public void displaySuccess(String message) {
        System.out.println("Success: " + message);
    }

    public void displaySimpleChannel(Channel channel) {
        if (channel != null) {
            System.out.println("채널 이름: " + channel.getChannelName());
        }
    }


}