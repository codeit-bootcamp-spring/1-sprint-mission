package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JCFMessage implements MessageService {
    private final List<Message> data;

    public JCFMessage() {
        data = new ArrayList<>();
    }

    // 메세지 생성
    @Override
    public Message createMessage(Channel channel, User writer, String content) {
        Message newMessage = new Message(channel, writer, content);
        data.add(newMessage);
        return newMessage;
    }

    // 메세지 모두 조회
    @Override
    public List<Message> getAllMessageList() {
        return data;
    }

    // 채널명으로 메세지 조회
    @Override
    public List<Message> searchByChannel(Channel channel) {
        List<Message> channelMsg = data.stream()
                .filter(msg -> msg.getChannel() == channel)
                .collect(Collectors.toList());
        return channelMsg;
    }

    // 메세지 삭제
    @Override
    public void deleteMessage(Message message) {
        data.remove(message);
    }

    // 메세지 정보 출력
    @Override
    public void displayInfo(Message message) {
        System.out.println(message.displayInfoMessage());
    }

    // 메세지 수정
    @Override
    public void updateMessage(Message message, String content) {
        message.updateContent(content);
    }

}
