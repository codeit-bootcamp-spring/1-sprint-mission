//package com.sprint.mission.discodeit.service.currentUse;
//
//
//import com.sprint.mission.discodeit.dto.MessageDto;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.service.interfaces.MessageServiceInterface;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//
//@RequiredArgsConstructor
//@Service
//public class MessageService implements MessageServiceInterface {
//
//    private FileMessageRepository messageRepository;
//    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
//
//    @Override
//    public void saveMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        Message message = messageRepository.createMessage(messageDto);
//
//        System.out.println("메세지 전송 성공!");
//    }
//
//    @Override
//    public void deleteMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        if(messageRepository.checkMessageContains(messageDto.getMessageId())){
//            messageRepository.deleteMessage(messageDto.getMessageId());
//            System.out.println("메세지 삭제 성공!");
//        }
//
//        System.out.println("메세지 삭제에 실패하였습니다");
//    }
//
//    @Override
//    public void modifyMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        if(messageRepository.checkMessageContains(messageDto.getMessageId())){
//            MessageDto dto = new MessageDto();
//            dto.setText(messageDto.getNewContent());
//            dto.setUserId(messageDto.getUserId());
//            dto.setMessageId(messageDto.getMessageId());
//            messageRepository.modifyMessage(dto);
//
//            System.out.println("메세지 수정 성공!");
//            return;
//        }
//
//        System.out.println("메세지 수정에 실패하였습니다");
//    }
//
//    @Override
//    public void showInfoMessage(MessageDto messagedto) throws IOException, ClassNotFoundException {
//        Message message = messageRepository.getMessage(messagedto);
//        System.out.println(message.getText() + " (" + message.getUserName() + ") [" +
//                format.format(message.getUpdatedAt()) + "]" + " - " + message.getChannelName());
//    }
//}