//package com.sprint.mission.discodeit.service.currentUse;
//
//import com.sprint.mission.discodeit.dto.MessageDto;
//import com.sprint.mission.discodeit.entity.BinaryContent;
//import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class BinaryContentService {
//    FileBinaryContentRepository repository;
//
//    public void createMessageContent(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        repository.saveMessageContent(messageDto.getMessageId());
//    }
//
//    public void createUserContent(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        repository.saveUserContent(messageDto.getUserId());
//    }
//
//    public BinaryContent findByMessage (MessageDto messageDto) throws IOException, ClassNotFoundException {
//        return repository.findMessageContent(messageDto.getMessageId());
//    }
//
//    public BinaryContent findByUser(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        return repository.findUserContent(messageDto.getUserId());
//    }
//
//    public List<BinaryContent> findAll() throws IOException, ClassNotFoundException {
//        return repository.findAll();
//    }
//
//    public void deleteUserContent(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        repository.deleteUser(messageDto.getUserId());
//    }
//
//    public void deleteMessageContent(MessageDto messageDto) throws IOException, ClassNotFoundException {
//        repository.deleteMessage(messageDto.getMessageId());
//    }
//}
