//package com.sprint.mission.service.file;
//
//import com.sprint.mission.entity.main.Message;
//import com.sprint.mission.repository.file.FileMessageRepository;
//import com.sprint.mission.service.MessageService;
//import com.sprint.mission.service.exception.NotFoundId;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class FileMessageService {
//
//    private final FileMessageRepository fileMessageRepository;
//
//    @SneakyThrows
//    //@Override
//    public Message create(Message message) {
//        return fileMessageRepository.save(message);
//    }
//
//    @SneakyThrows
//    //@Override
//    public Message update(UUID messageId, String newMassage) {
//        Message updatingMessage = findById(messageId);
//        // 나중에 바꿔야될 컨텐츠 및 dto
//        updatingMessage.setContent(newMassage);
//        return fileMessageRepository.save(updatingMessage);
//    }
//
//    @SneakyThrows
//    //@Override
//    public Message findById(UUID messageId) {
//        return fileMessageRepository.findById(messageId).orElseThrow(NotFoundId::new);
//    }
//
//    @SneakyThrows
//    //@Override
//    public List<Message> findAll() {
//        return fileMessageRepository.findAll();
//    }
//
//    @SneakyThrows
//    //@Override
//    public List<Message> findMessagesInChannel(UUID channelId) {
//        return fileMessageRepository.findMessagesInChannel(channelId);
//    }
//
//    @SneakyThrows
//    //@Override
//    public void delete(UUID messageId) {
//        fileMessageRepository.delete(messageId);
//    }
//
//    /**
//     * 메시지 디렉토리 생성
//     */
//    @SneakyThrows
//    public void createDirectory() {
//        fileMessageRepository.createDirectory();
//    }
//}