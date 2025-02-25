//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.dto.MessageDto;
//import com.sprint.mission.discodeit.entity.BinaryContent;
//import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
//
//import java.util.List;
//
//public class JCFBinaryContentService {
//    JCFBinaryContentRepository repository;
//
//    public void createMessageContent(MessageDto messageDto){
//        repository.saveMessageContent(messageDto.getMessageId());
//    }
//
//    public void createUserContent(MessageDto messageDto){
//        repository.saveUserContent(messageDto.getUserId());
//    }
//
//    public BinaryContent findByMessage (MessageDto messageDto){
//        return repository.findMessageContent(messageDto.getMessageId());
//    }
//
//    public BinaryContent findByUser(MessageDto messageDto){
//        return repository.findUserContent(messageDto.getUserId());
//    }
//
//    public List<BinaryContent> findAll(){
//        return repository.findAll();
//    }
//
//    public void deleteUserContent(MessageDto messageDto){
//        repository.deleteUser(messageDto.getUserId());
//    }
//
//    public void deleteMessageContent(MessageDto messageDto){
//        repository.deleteMessage(messageDto.getMessageId());
//    }
//}
