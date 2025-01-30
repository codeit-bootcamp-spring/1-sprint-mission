package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static java.util.Collections.max;

public class FileMessageService implements MessageService {
    FileIOHandler fileIOHandlerInstance = FileIOHandler.getInstance();
    FileMessageRepository FileMessageRepositoryInstance = FileMessageRepository.getInstance();
    FileUserRepository FileUserRepositoryInstance = FileUserRepository.getInstance();
    FileChannelRepository FileChannelRepositoryInstance = FileChannelRepository.getInstance();

    //생성자 접근 불가능하도록 함.
    private FileMessageService() {
    }
    //LazyHolder 싱글톤
    private static class JCFMessageServiceHolder {
        private static final FileMessageService INSTANCE = new FileMessageService();
    }
    //외부에서 접근 가능한 인스턴스
    public static FileMessageService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return FileMessageService.JCFMessageServiceHolder.INSTANCE;
    }




    //모든 메세지 관리 맵 'MessagesMap' 반환
    @Override
    public HashMap<UUID, Message> getMessagesMap() {
        return FileMessageRepositoryInstance.getMessagesMap();
    }

    //해당 메세지 객체 리턴
    @Override
    public Message getMessageById(UUID messageId) {
        if (messageId == null || FileMessageRepositoryInstance.isMessageExist(messageId)==false){
            System.out.println("메세지 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return FileMessageRepositoryInstance.getMessage(messageId);
    }

    //메세지 생성. 'MessagesMap'에 uuid-메세지객체 주소 넣어줌.
    @Override
    public UUID createMessage(UUID fromUser, UUID channel, String content) {
        if (fromUser == null || channel == null || content == null || FileUserRepositoryInstance.isUserExist(fromUser) || FileChannelRepositoryInstance.isChannelExist(channel)) {
            System.out.println("메세지 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        Message newMessage = new Message(FileUserRepositoryInstance.getUser(fromUser), FileChannelRepositoryInstance.getChannel(channel), content);
        FileMessageRepositoryInstance.addMessage(newMessage);
        System.out.println("메세지 생성 성공!");
        return newMessage.getId();
    }

    //UUID를 통해 메세지 객체를 찾아 삭제
    @Override
    public boolean deleteMessage(UUID MessageId) {
        if (MessageId == null || FileMessageRepositoryInstance.isMessageExist(MessageId) == false) {
            System.out.println("메세지 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileMessageRepositoryInstance.deleteMessage(MessageId);
        System.out.println("메세지 삭제 성공!");
        return true;
    }

    //메세지 내용 변경
    @Override
    public boolean reviseMessageContent(UUID MessageId, String newContent) {
        if (MessageId == null || newContent == null || FileMessageRepositoryInstance.isMessageExist(MessageId) == false) {
            System.out.println("메세지 수정 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileMessageRepositoryInstance.getMessage(MessageId).setContent(newContent);
        System.out.println("메세지 수정 성공!");
        return true;
    }

    //메세지 존재여부 확인
    @Override
    public boolean isMessageExist(UUID messageId) {
        if (messageId == null) {
            return false;
        }
        return FileMessageRepositoryInstance.isMessageExist(messageId);
    }

    //메세지 객체 상세정보 출력(보낸이/채널/내용/생성시간과 수정시간 중 최근시간)
    @Override
    public boolean printMessageDetails(UUID messageId) {
        if (messageId == null
                || FileMessageRepositoryInstance.isMessageExist(messageId) == false
                || FileMessageRepositoryInstance.getMessage(messageId) == null
                || FileMessageRepositoryInstance.getMessage(messageId).getFromUser() == null
                || FileMessageRepositoryInstance.getMessage(messageId).getChannel() == null)
        {
            System.out.println("메세지 내용 및 상세정보 확인 실패. 입력값을 확인해주세요.");
            return false;
        }



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        if (FileMessageRepositoryInstance.getMessage(messageId).getUpdatedAt() != 0){
            date.setTime(FileMessageRepositoryInstance.getMessage(messageId).getUpdatedAt());
        } else {
            date.setTime(FileMessageRepositoryInstance.getMessage(messageId).getCreatedAt());
        }

        String userName = FileMessageRepositoryInstance.getMessage(messageId).getFromUser().getUserName();
        String channelName = FileMessageRepositoryInstance.getMessage(messageId).getChannel().getChannelName();
        String messageContent = FileMessageRepositoryInstance.getMessage(messageId).getContent();
        String messageLatestEditTime = simpleDateFormat.format(date);

        System.out.println("[보낸이] " + userName);
        System.out.println("[채널] " + channelName);
        System.out.println("[내용] " + messageContent);
        System.out.println("[최근 수정시간] " + messageLatestEditTime);
        return true;
    }

    @Override
    public String getMessageContent(UUID messageId) {
        if (messageId == null) {
            System.out.println("메세지 내용 확인 실패. 입력값을 확인해주세요.");
            return null;
        }
        return FileMessageRepositoryInstance.getMessage(messageId).getContent();
    }

    //현재 메세지맵에 있는 객체정보 직렬화
    public boolean exportMessageMap(String fileName) {
        if (fileName == null) {
            System.out.println("메세지 역직렬화 실패. 입력값을 확인해주세요.");
            return false;
        }
        fileIOHandlerInstance.serializeHashMap(FileMessageRepositoryInstance.getMessagesMap(), fileName);
        System.out.println("메세지 직렬화 성공!");
        return true;
    }

    //역직렬화로 불러온 메세지맵을 검증하고 기존의 메세지맵에 저장
    //다른 타입의 value가 역직렬화되어 들어왔을 경우를 고려하여 검증코드 추가 예정
    public boolean importMessageMap(String fileName) {
        if (fileName == null) {
            System.out.println("채널 불러오기 실패. 입력값을 확인해주세요.");
            return false;
        }
        HashMap<UUID, Message> importedMessageMap = (HashMap<UUID, Message>) fileIOHandlerInstance.deserializeHashMap(fileName);
        if (importedMessageMap == null || importedMessageMap.isEmpty()) {
            System.out.println("메세지 불러오기 실패. 추가할 메세지가 존재하지 않습니다.");
            return false;
        }
        FileMessageRepositoryInstance.addMessages(importedMessageMap);
        System.out.println("메세지 불러오기 성공!");
        return true;
    }

}
