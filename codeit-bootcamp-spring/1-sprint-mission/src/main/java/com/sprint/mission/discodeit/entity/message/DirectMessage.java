package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.user.entity.User;

public class DirectMessage {
    // 보내는 방법을 추상화
    private final Sender<User, User> sender;

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
            System.out.println(format);
        };

        return new DirectMessage(sender, message, messageSender, messageReceiver);
    }

    public void sendMessage() {
        sender.sendMessage(messageSender, messageReceiver, message);
    }

}
/**
 *  메세지를 주고 받는 행위를 어떻게 처리해주어야 할까?
 *  1. 유저가 메시지를 발송한다.
 *  2. 애플리케이션에서 메시지를 저장한다.
 *  3. 애플리케이션에서 메시지 알림을 전송한다.
 *  4. 받은 유저가 알림을 확인하고, 메시지를 확인한다. 메시지를 확인할 때 기존의 주고받은 메시지도 모두 들고 와야함 => 서비스 레포에서 처리해야할 일 같음
 */

/**
 *  메시지가 저장할 데이터는 ?
 *  1. 메시지 원문
 *  2. 보낸 사람
 *  3. 받는 사람
 *  etc. 메시지 원문 말고, 사진(url 포함), 다양한 것을 보낼 수 있는데 저장할지 확실하지 않은 데이터를 필드로 가지고 있어야하는가?
 */