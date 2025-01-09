package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.jcf.JCFMessagelService;
import com.sprint.mission.discodeit.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class JCFMessagelService {
      //final List<Message> messagedata;

//    public JCFMessagelService() { //data필드 생성자초기화
//        this.messagedata= new ArrayList<>();
//    }
//
//
//    @Override
//    public void createMessage(Message Message) {
//        messagedata.add(Message);
//        System.out.println(Utils.transTime(Message.getCreatedAt())  + " 메시지 내용: " + Message.getContent() );
//    }
//
//    @Override
//    public Message readMessage(String id) { //
//
//        return
//                this.messagedata.stream()
//                        .filter(ch -> ch.getMsgId().equals(id))
//                        .findFirst()
//                        .orElse(null);
//
//
//    }
//
//    @Override
//    public List<Message> readAllMessage() {
//        return messagedata;
//    }
//
//    //메시지 내용 및 시간 업데이트
//    @Override
//    public void modifyMessage(String id, String name) {
//        Message target = readMessage(id);
//        target.updateName(name);
//        target.updateUpdatedAt(); //시간 업데이트
//
//    }
//
//    @Override
//    public void deleteMessage(String id) {
//        String name= readMessage(id).getChName();
//        boolean isDeleted = this.Messagedata.removeIf(ch -> ch.getChId().equals(id));
//
//        if(isDeleted) {
//            System.out.println(name + " 채널이 삭제되었습니다.");
//        }else{
//            System.out.println(name + " 채널 삭제 실패하였습니다.");
//        }
//    }

    
}
