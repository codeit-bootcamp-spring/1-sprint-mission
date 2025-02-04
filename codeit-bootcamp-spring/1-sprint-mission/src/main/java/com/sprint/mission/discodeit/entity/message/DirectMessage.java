package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.entity.User;
import lombok.Getter;

@Getter
public class DirectMessage extends AbstractUUIDEntity {
    // 보내는 방법을 추상화
    private final transient Sender<User, User> sender;

    private final String message;

    private final User messageSender;

    private final User messageReceiver;

    private DirectMessage(Sender<User, User> sender, String message, User messageSender, User messageReceiver) {
        this.sender = sender;
        this.message = message;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
    }

    public static DirectMessage ofMessageAndSenderReceiver(String message, User messageSender, User messageReceiver) {
        Sender<User, User> sender = (sender1, receiver1, message1) -> {
            var format = String.format(
                    "보낸 사람 : %s , 보낸 사람 : %s, 메시지 : %s",
                    sender1.getName(),
                    receiver1.getName(),
                    message
            );
        };

        return new DirectMessage(sender, message, messageSender, messageReceiver);
    }

    public void sendMessage() {
        sender.sendMessage(messageSender, messageReceiver, message);
    }

}