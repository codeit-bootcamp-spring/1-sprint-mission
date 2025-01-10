package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService{
    private final List<Message> data;

    public JCFMessageService(){
        data = new ArrayList<>();
    }

    @Override
    public void createMessage(Message message){
        data.add(message);
    }

    @Override
    public void updateMessage(Message message, String content, User writer){
        boolean messageCheck = data.stream()
                .anyMatch(check -> check.equals(message));
        boolean writerCheck = message.getWriter().equals(writer);

        if (!messageCheck){
            System.out.println("메시지가 없습니다.");
        }else if(!writerCheck) {
            System.out.println("작성자가 아닙니다.");
        }else {
            message.setContent(content);
        }
    }

    @Override
    public void deleteMessage(Message message, User writer){
        boolean removed = data.removeIf(removeMessage -> removeMessage.equals(message) && removeMessage.getWriter().equals(writer));
        if (removed) {

            System.out.println("메시지가 삭제되었습니다.");
        } else {
            System.out.println("메시지가 없거나 작성자만 삭제할 수 있습니다.");
        }
    }

    @Override
    public List<String> getAllMessage(){
            return data.stream()
                    .map(Message::getContent)
                    .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, String>> getChannelMessage(Channel channel){
         List<Map<String, String>> channelMessages = data.stream()
                .filter(channelCheck -> channelCheck.getChannel().equals(channel))
                .map(message->{
                    Map<String, String> channelMessage = new HashMap<>();
                    channelMessage.put("작성자", message.getWriter().getUserName());
                    channelMessage.put("메시지 내용", message.getContent());
                    return channelMessage;
        }).collect(Collectors.toList());

        if (channelMessages.isEmpty()) {
            System.out.println("해당 채널이 존재하지 않거나 메시지가 없습니다.");
        }
        return channelMessages;
    }

    @Override
    public List<Map<String, String>> getWriterMessage(User user){
        List<Map<String, String>> writerMessages = data.stream()
                .filter(writerCheck -> writerCheck.getWriter().equals(user))
                .map(message -> {
                    Map<String, String> writerMessage = new HashMap<>();
                    writerMessage.put("채널", message.getChannel().getChannelName());
                    writerMessage.put("메시지 내용", message.getContent());
                    return writerMessage;
                }).collect(Collectors.toList());

        if (writerMessages.isEmpty()) {
            System.out.println("사용자가 없거나, 메시지가 없습니다.");
        }
        return writerMessages;

    }

}
