package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    //채널 기준으로 메시지를 정리
    Map<UUID, Map<UUID, Message>> msgData = new HashMap<>();


    public void addChannelMsg(Message msg) {
        // 채널이 없으면 새로 추가
        msgData.putIfAbsent(msg.getDestinationChannel(), new HashMap<>());
        // 메시지 저장
        msgData.get(msg.getDestinationChannel()).put(msg.getMsguuId(), msg);
    }

    // 메시지 조회
    // 리턴을 매시지 객체로 하는게 좋은자.. 따로 필드값을 뽑아서 리스트로 뽑는게 좋을지 모르겠...
    public Message getMessage(UUID msgId) {
        for (Map<UUID, Message> channelMessages : msgData.values()) {
            if (channelMessages.containsKey(msgId)) {
                return channelMessages.get(msgId);
            }
        }
        return null;
    }

    public Map<UUID, Map<UUID, Message>> getAllMsg() {
        return new HashMap<>(msgData);
    }




    public void updateMsg(UUID msgId, String newContent) {
        // getMessage() 메서드를 활용하여 해당 메시지를 가져옴
        Message msg = getMessage(msgId);  // 메시지 ID로 메시지를 찾음

        if (msg != null) {
            msg.update(newContent);  // 메시지 내용 수정
            System.out.println("메시지 내용이 업데이트되었습니다 --> ("+ newContent + ")");
        } else {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
        }
    }



    public void deleteMsg(UUID msgId) {
        // getMessage 메시지 검색
        Message msg = getMessage(msgId);

        // 메시지가 있는지 확인
        if (msg == null) {
            System.out.println("해당 메시지를 찾을 수 없습니다.");
            return;  // 메시지가 없으면 바로 종료
        }
        UUID channeluuId = msg.getDestinationChannel();
        Map<UUID, Message> channelMessages = msgData.get(channeluuId);
        channelMessages.remove(msgId);

    }
}
