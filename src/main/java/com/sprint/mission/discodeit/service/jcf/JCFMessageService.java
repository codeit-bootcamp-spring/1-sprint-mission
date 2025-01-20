package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    final List<Message> messagedata;

    public JCFMessageService() {
        this.messagedata = new ArrayList<>();
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        Message message = new Message(userId, channelId, content);
        messagedata.add(message);

        return message;


    }

    @Override
    public Message readMessage(UUID msgID) {
        //id가 있는지 검증


        return
                this.messagedata.stream()
                        .filter(msg -> msg.getMsgId().equals(msgID))
                        .findFirst()
                        .orElse(null); //없으면 null

    }

    @Override
    public List<Message> readAllMessage() {
        return messagedata;
    }

    @Override
    public Message modifyMessage(UUID msgID, String content) {
        Message msg = readMessage(msgID);
        String oriMsg=msg.getContent();
        msg.updateContent(content);
        msg.updateUpdatedAt();
        System.out.println(msg.getUserID() + "님의 메시지 변경: \""+ oriMsg + "\" -> \"" +
                content+  "\"");

        return msg;
    }

    @Override
    public void deleteMessage(UUID msgID) {
        String name= readMessage(msgID).getContent();
        boolean isDeleted = this.messagedata.removeIf(msg -> msg.getMsgId().equals(msgID)); //메시지삭제

        if(isDeleted) {
            System.out.println(name + "님의 메시지가 삭제되었습니다.");

        }else{
            System.out.println(" 메시지 삭제 실패하였습니다.");
        }


    }
}
