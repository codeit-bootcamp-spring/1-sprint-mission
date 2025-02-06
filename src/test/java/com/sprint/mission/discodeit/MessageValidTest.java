//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//
//import java.util.UUID;
//
//public class MessageValidTest {
//    static void messageContentTest(MessageService messageService) {
//        // 메시지 예외 테스트
//        System.out.println("메시지가 null일 경우");
//        UUID channelId = UUID.randomUUID();
//        UUID authorId = UUID.randomUUID();
//        try {
//            Message message = messageService.create(null, channelId, authorId);
//            System.out.println("메시지 생성: " + message.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("메시지가 100자를 초과할 경우");
//        try {
//            Message message = messageService.create("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij" +
//                    "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija", channelId, authorId);
//            System.out.println("메시지 생성: " + message.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        MessageService messageService = new BasicMessageService(new FileMessageRepository());
//
//        messageContentTest(messageService);
//    }
//}
