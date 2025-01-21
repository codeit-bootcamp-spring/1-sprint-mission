package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.factory.ApplicationFileFactory;
import com.sprint.mission.discodeit.entity.channel.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.entity.message.dto.DirectMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.SendDirectMessageRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import java.io.IOException;
import java.util.UUID;

public class JavaApplication {
    private static final ApplicationFileFactory app = ApplicationFileFactory.getInstance();

    public static void main(String[] args) throws IOException {
        UserInfoResponse user = registerUser();
        ChannelResponse channelResponse = registerChannel(user.uuid(), "스프링부트_1기");
        UserInfoResponse receiveMessageUserTemp = registerUser();
        DirectMessageInfoResponse sendDirectMessage = sendDirectMessage(user.uuid(), receiveMessageUserTemp.uuid(), "안녕하세요");
    }

    private static UserInfoResponse registerUser() {
        var userService = app.getUserService();
        var registerRequest = new RegisterUserRequest("홍길동");
        return userService.register(registerRequest);
    }

    private static ChannelResponse registerChannel(UUID userId, String channelName) {
        var channelService = app.getChannelService();
        var channelCreateRequest = new CreateNewChannelRequest(userId, channelName);
        return channelService.createChannelOrThrow(channelCreateRequest);
    }

    private static DirectMessageInfoResponse sendDirectMessage(UUID sendUserId, UUID receiveUserId, String message) {
        var directMessageService = app.getDirectMessageService();
        var sendDirectMessageRequest = new SendDirectMessageRequest(sendUserId, receiveUserId, message);
        return directMessageService.sendMessage(sendDirectMessageRequest);
    }
}
