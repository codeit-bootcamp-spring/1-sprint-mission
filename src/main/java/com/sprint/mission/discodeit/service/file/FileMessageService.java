//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.common.ErrorMessage;
//import com.sprint.mission.discodeit.common.UtilMethod;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public class FileMessageService implements MessageService {
//    private final FileMessageRepository fileMessageRepository;
//    private final FileUserService fileUserService;
//    private final FileChannelService fileChannelService;
//
//
//    public FileMessageService(FileMessageRepository fileMessageRepository, FileUserService fileUserService,
//                              FileChannelService fileChannelService) {
//        this.fileMessageRepository = fileMessageRepository;
//        this.fileUserService = fileUserService;
//        this.fileChannelService = fileChannelService;
//    }
//
//    @Override
//    public Message createMessage(UUID sendUserId, UUID channelId, String content) {
//        User sendUser = fileUserService.findUserByIdOrThrow(sendUserId);
//        Channel foundChannel = fileChannelService.findChannelByIdOrThrow(channelId);
//
//        Message message = Message.of(sendUser, foundChannel, content);
//
//        return fileMessageRepository.saveMessage(message);
//    }
//
//    @Override
//    public Message findMessageByIdOrThrow(UUID messageId) {
//        return Optional.ofNullable(fileMessageRepository.findMessageById(messageId))
//                .orElseThrow(() -> new RuntimeException(ErrorMessage.MESSAGE_NOT_FOUND.format(messageId)));
//    }
//
//    @Override
//    public List<Message> findAllMessage() {
//        return fileMessageRepository.findAllMessages();
//    }
//
//    @Override
//    public Message updateMessage(UUID sendUserId, UUID messageId, String content) {
//        fileUserService.findUserByIdOrThrow(sendUserId);
//        Message foundMessage = findMessageByIdOrThrow(messageId);
//
//        if (foundMessage.isNotOwner(sendUserId)) {
//            throw new RuntimeException(ErrorMessage.NOT_MESSAGE_CREATOR.format(sendUserId));
//        }
//
//        foundMessage.updateContent(content);
//        foundMessage.updateUpdatedAt(UtilMethod.getCurrentTime());
//
//        return fileMessageRepository.saveMessage(foundMessage);
//    }
//
//    @Override
//    public void deleteMessage(UUID sendUserId, UUID messageId) {
//        Message foundMessage = findMessageByIdOrThrow(messageId);
//
//        if (foundMessage.isNotOwner(sendUserId)) {
//            throw new RuntimeException(ErrorMessage.NOT_MESSAGE_CREATOR.format(sendUserId));
//        }
//
//        fileMessageRepository.removeMessage(messageId);
//    }
//}
