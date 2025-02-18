//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.common.ErrorMessage;
//import com.sprint.mission.discodeit.common.UtilMethod;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public class JCFMessageService implements MessageService {
//    private final JCFMessageRepository jcfMessageRepository;
//    private final JCFUserService jcfUserService;
//    private final JCFChannelService jcfChannelService;
//
//    public JCFMessageService(JCFMessageRepository jcfMessageRepository, JCFUserService jcfUserService,
//                             JCFChannelService jcfChannelService) {
//        this.jcfMessageRepository = jcfMessageRepository;
//        this.jcfUserService = jcfUserService;
//        this.jcfChannelService = jcfChannelService;
//    }
//
//    @Override
//    public Message createMessage(UUID sendUserId, UUID channelId, String content) {
//        User sendUser = jcfUserService.findUserByIdOrThrow(sendUserId);
//        Channel foundChannel = jcfChannelService.findChannelByIdOrThrow(channelId);
//
//        Message message = Message.of(sendUser, foundChannel, content);
//
//        return jcfMessageRepository.saveMessage(message);
//    }
//
//    @Override
//    public Message findMessageByIdOrThrow(UUID messageId) {
//        return Optional.ofNullable(jcfMessageRepository.findMessageById(messageId))
//                .orElseThrow(() -> new RuntimeException(ErrorMessage.MESSAGE_NOT_FOUND.format(messageId)));
//    }
//
//    @Override
//    public List<Message> findAllMessage() {
//        return jcfMessageRepository.findAllMessages();
//    }
//
//    @Override
//    public Message updateMessage(UUID sendUserId, UUID messageId, String content) {
//        jcfUserService.findUserByIdOrThrow(sendUserId);
//        Message foundMessage = findMessageByIdOrThrow(messageId);
//
//        if (foundMessage.isNotOwner(sendUserId)) {
//            throw new RuntimeException(ErrorMessage.NOT_MESSAGE_CREATOR.format(sendUserId));
//        }
//
//        foundMessage.updateContent(content);
//        foundMessage.updateUpdatedAt(UtilMethod.getCurrentTime());
//
//        return jcfMessageRepository.saveMessage(foundMessage);
//    }
//
//    @Override
//    public void deleteMessage(UUID sendUserId, UUID messageId) {
//        // 메세지 조회
//        Message foundMessage = findMessageByIdOrThrow(messageId);
//
//        // 메세지 생성자가 맞는지 확인
//        if (foundMessage.isNotOwner(sendUserId)) {
//            throw new RuntimeException(ErrorMessage.NOT_MESSAGE_CREATOR.format(sendUserId));
//        }
//
//        // 메세지 삭제
//        jcfMessageRepository.removeMessage(messageId);
//    }
//}
