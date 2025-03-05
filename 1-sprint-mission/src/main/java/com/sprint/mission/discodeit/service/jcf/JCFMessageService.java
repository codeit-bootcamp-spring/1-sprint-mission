//package com.sprint.mission.discodeit.service.jcf;
//
//
//import com.sprint.mission.discodeit.dto.MessageDto;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.service.jcf.interfaces.JCFMessageServiceInterface;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//
//
//public class JCFMessageService implements JCFMessageServiceInterface {
//
//    private JCFMessageRepository messageRepository;
//    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
//
//    @Autowired
//    public JCFMessageService(JCFMessageRepository messageRepository) {
//        this.messageRepository = messageRepository;
//    }
//
//    //    private static final JCFServiceJCF INSTANCE = new JCFServiceJCF();
////    private JCFServiceJCF(){}
////
////    public static JCFServiceJCF getInstance() {
////        return INSTANCE;
////    }
//    @Override
//    public Message saveMessage(String text, User user, Channel channel, boolean haveContent , int contents) {
//        MessageDto dto = new MessageDto();
//        dto.setText(text);
//        dto.setUserId(user.getId());
//        dto.setUserName(user.getName());
//        dto.setChannelId(channel.getChannelId());
//        dto.setChannelName(channel.getName());
//        dto.setHaveContent(haveContent);
//        if(haveContent) dto.setHowManyContent(contents);
//
//        Message message = messageRepository.createMessage(dto);
//
//        System.out.println("메세지 전송 성공!");
//        return message;
//    }
//
//    @Override
//    public void deleteMessage(Message message) {
//        if(messageRepository.checkMessageContains(message.getMessageId())){
//            messageRepository.deleteMessage(message.getMessageId());
//            System.out.println("메세지 삭제 성공!");
//        }
//
//        System.out.println("메세지 삭제에 실패하였습니다");
//    }
//
//    @Override
//    public void modifyMessage(Message message, String content) {
//        if(messageRepository.checkMessageContains(message.getMessageId())){
//            MessageDto dto = new MessageDto();
//            dto.setText(content);
//            dto.setUserId(message.getUserId());
//            dto.setMessageId(message.getMessageId());
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
//    public void showInfoMessage(Message message) {
//        System.out.println(message.getText() + " (" + message.getUserName() + ") [" +
//                format.format(message.getUpdatedAt()) + "]" + " - " + message.getChannelName());
//    }
//}