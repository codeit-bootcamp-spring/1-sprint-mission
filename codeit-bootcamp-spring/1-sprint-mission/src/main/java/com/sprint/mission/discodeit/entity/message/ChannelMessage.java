package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.entity.User;
import lombok.Getter;

@Getter
public class ChannelMessage extends AbstractUUIDEntity {

    private final Sender<User, Channel> sender;

    private final String message;

    private final User messageSender;

    private final Channel receiverChannel;

    private ChannelMessage(Sender<User, Channel> sender, String message, User messageSender, Channel receiverChannel) {
        this.sender = sender;
        this.message = message;
        this.messageSender = messageSender;
        this.receiverChannel = receiverChannel;
    }

    public static ChannelMessage ofMessageAndSenderAndReceiverChannel(
            String message,
            User messageSender,
            Channel receiverChannel
    ) {

        Sender<User, Channel> channelSender = (sender, receiver, message1) -> {
            var format = String.format(
                    "보낸 사람 : %s , 수신 채널 : %s , 메시지 : %s",
                    sender.getName(),
                    receiver.getChannelName(),
                    message1
            );

            System.out.println(format);
        };

        return new ChannelMessage(channelSender, message, messageSender, receiverChannel);
    }

    public void sendMessage() {
        sender.sendMessage(messageSender, receiverChannel, message);
    }

}
