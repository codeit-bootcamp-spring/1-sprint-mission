package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;

public class JCFMessagelService implements MessageService {
    final List<Message> messagedata;

    public JCFMessagelService() {
        this.messagedata = new ArrayList<>();
    }

    @Override
    public void createMessage(Message message) {
        messagedata.add(message);

    }

    @Override
    public Message readMessage(String msgID) {
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
    public void modifyMessage(String msgID, String content) {
        readMessage(msgID).updateContent(content);
    }

    @Override
    public void deleteMessage(String msgID) {
        String name= readMessage(msgID).getMsgId();
        boolean isDeleted = this.messagedata.removeIf(msg -> msg.getMsgId().equals(msgID));

        if(isDeleted) {
            System.out.println(name + " 메시지가 삭제되었습니다.");
        }else{
            System.out.println(name + " 메시지 삭제 실패하였습니다.");
        }


    }
}
