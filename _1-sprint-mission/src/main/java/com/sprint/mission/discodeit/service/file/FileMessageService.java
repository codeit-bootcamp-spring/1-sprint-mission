//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.io.*;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//
//public class FileMessageService implements MessageService {
//    private final FileMessageRepository fileMessageRepository;
//    private final FileChannelRepository fileChannelRepository;
//    private final FileUserRepository fileUserRepository;
//
//    public FileMessageService(FileMessageRepository fileMessageRepository,FileChannelRepository fileChannelRepository,FileUserRepository fileUserRepository) {
//        this.fileMessageRepository = fileMessageRepository;
//        this.fileChannelRepository = fileChannelRepository;
//        this.fileUserRepository = fileUserRepository;
//    }
//
//    @Override
//    public Message create(String content, UUID channelId, UUID userId) {
//        if(fileChannelRepository.findById(channelId).isEmpty()) {
//            throw new NoSuchElementException("ChannelId not found");
//        }
//        if(fileUserRepository.findById(userId).isEmpty()) {
//            throw new NoSuchElementException("UserId not found");
//        }
//        Message message = new Message(content,channelId,userId);
//        return null;
//    }
//
//    @Override
//    public Message findById(UUID messageId) {
//        return fileMessageRepository.findById(messageId)
//                .orElseThrow(()->new NoSuchElementException("Message not found"));
//    }
//
//
//    @Override
//    public List<Message> findAll() {
//        return fileMessageRepository.findAll();
//    }
//
//    @Override
//    public Message update(UUID messageId, String newContent) {
//        Message message = fileMessageRepository.findById(messageId)
//                .orElseThrow(()->new NoSuchElementException("Message not found"));
//        message.updateContent(newContent);
//        return fileMessageRepository.create(message);
//    }
//
//    @Override
//    public void delete(UUID messageId) {
//        fileMessageRepository.delete(messageId);
//    }
//}
