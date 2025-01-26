//package com.sprint.mission.discodeit.jcf;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.service.MessageService;
//
//import java.util.*;
//
//public class JCFMessageService implements MessageService {
//    private final Map<UUID, Message> data;
//    private static final String DATA_NULL_BLANK = "";
//
//    public JCFMessageService(){
//        this.data = new HashMap<>();
//    }
//
//    @Override
//    public Message create(Message message) {
//        data.put(message.getId(), message);
//        return message;
//    }
//
//    @Override
//    public Message readOne(UUID id) {
//        return data.get(id);
//    }
//
//    @Override
//    public List<Message> readAll() {
//        data.values().forEach(message -> {
//
//            System.out.println();
//        });
//        return new ArrayList<>(data.values());
//    }
//
//    @Override
//    public List<Message> readAll(UUID channelId){
//        List<Message> returnData = new ArrayList<>();
//
//        data.values().stream()
//                .filter(message ->
//                        message.getChannel() != null && message.getChannel().getId().equals(channelId))
//                .forEach(message -> {
//                    String sender = message.getSender().getUsername();
//                    String recipient = message.getRecipient() != null ? message.getRecipient().getUsername() : DATA_NULL_BLANK;
//
//                    if (recipient != DATA_NULL_BLANK) {
//                        System.out.println(sender + " -> " + recipient + " : " + message.getContent());
//                    } else {
//                        System.out.println(sender + " - " + message.getContent());
//                    }
//                });
//
//        return new ArrayList<>(returnData);
//    }
//
//    @Override
//    public Message update(UUID id, Message updatedMessage) {
//        if(data.containsKey(id)){
//            Message exsitingMessage  = data.get(id);
////            exsitingMessage.updateContent(updatedMessage.getContent());
//            return  exsitingMessage;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean delete(UUID id) {
//        return data.remove(id) != null;
//    }
//}
