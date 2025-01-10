//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.ChatBehavior;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
//import com.sprint.mission.discodeit.service.jcf.JCFUserService;
//
//import java.util.List;
//
//static void messageSimulation(){
//    User user = userService.readAllUsers().get(0);
//    Channel chatChannel = new Channel(
//        "server-uuid-2",
//        "category-uuid-2",
//        "exampleChannelName",
//        100,
//        false,
//        "General",
//        new ChatBehavior(JCFUserService.getInstance(), JCFMessageService.getInstance())
//    );
//
//    Channel chatChannel2 = new Channel(
//        "server-uuid-1",
//        "category-uuid-1",
//        "exampleChannelName2",
//        100,
//        false,
//        "General",
//        new ChatBehavior(JCFUserService.getInstance(),JCFMessageService.getInstance())
//    );
//
//
//
//    ChatBehavior chatBehavior = (ChatBehavior) chatChannel.getBehavior();
//    ChatBehavior chatBehavior2 = (ChatBehavior) chatChannel2.getBehavior();
//
//    channelService.createChannel(chatChannel);
//    channelService.createChannel(chatChannel2);
//
//    Message message = new Message(
//        user.getUUID(),
//        chatChannel.getUUID(),
//        "this is first chat!",
//        null,
//        null
//    );
//
//    messageService.createMessage(message, chatBehavior);
//
//    messageService.createMessage(new Message(
//        user.getUUID(),
//        chatChannel.getUUID(),
//        "this is example message",
//        null,
//        null
//    ), chatBehavior2 );
//
//
//    messageService.createMessage(new Message(
//        user.getUUID(),
//        chatChannel.getUUID(),
//        "this is example message2",
//        null,
//        null
//    ), chatBehavior2 );
//
//
//    messageService.createMessage(new Message(
//        user.getUUID(),
//        chatChannel.getUUID(),
//        "this is example message3",
//        null,
//        null
//    ), chatBehavior2 );
//
//    List<Channel> channels = channelService.getAllChannels();
//
//    // 채널별 메시지 조회
//    for(Channel c : channels){
//        if(c.getBehavior() instanceof ChatBehavior){
//            ChatBehavior currentChat = (ChatBehavior) c.getBehavior();
//            List<Message> currentChatHistory = messageService.getChatHistory(currentChat);
//            System.out.println(c.getChannelName() + " Chat History");
//            for(Message m : currentChatHistory){
//                System.out.println(m.getContent());
//            }
//        }
//    }
//
//    // channel1 메시지 조회
//    System.out.println("=== channel 1 message history === ");
//    for(Message m : messageService.getChatHistory(chatBehavior)){
//        System.out.println(m);
//    }
//
//    // channel1 메시지 수정
//
//    System.out.println("=== edit message ===");
//    String messageId = messageService.getChatHistory(chatBehavior).get(0).getUUID();
//    messageService.modifyMessage(messageId, "this is edited message", chatBehavior);
//
//    System.out.println(messageService.getMessageById(messageId, chatBehavior).get().getContent());
//    // channel1 메시지 삭제
//
//
//    messageService.deleteMessage(messageId, chatBehavior);
//    //삭제 후 조회
//
//    System.out.println("=== channel 1 message history === ");
//    for(Message m : messageService.getChatHistory(chatBehavior)){
//        System.out.println(m);
//    }
//
