package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {
    final List<Message> messagedata;

    public JCFMessageService() {
        this.messagedata = new ArrayList<>();
    }

    @Override
    public void createMessage(Message message) {
        messagedata.add(message);
        message.getUser().addMessage(message);

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
        Message msg = readMessage(msgID);
        String oriMsg=msg.getContent();
        msg.updateContent(content);
        msg.updateUpdatedAt();
        System.out.println(msg.getUser().getUserName() + "님의 메시지 변경: \""+ oriMsg + "\" -> \"" +
                content+  "\"");
    }

    @Override
    public void deleteMessage(String msgID) {
        String uname= readMessage(msgID).getUser().getUserName();
        String name= readMessage(msgID).getContent();
        //boolean isDeleted = this.messagedata.removeIf(msg -> msg.getMsgId().equals(msgID)); //메시지삭제
        boolean isDeleted = this.messagedata.removeIf(msg -> {
            if (msg.getMsgId().equals(msgID)) {
                // 유저의 메시지리스트에서삭제
                msg.getUser().getMsgList().removeIf(m -> m.getMsgId().equals(msgID));
                return true; // 삭제 조건을 만족할 때 삭제
            }
            return false;
        });
        if(isDeleted) {

            System.out.println(uname + "님의 \"" + name + "\" 메시지가 삭제되었습니다.");

        }else{
            System.out.println(" 메시지 삭제 실패하였습니다.");
        }


    }
}
