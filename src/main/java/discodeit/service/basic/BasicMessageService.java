package discodeit.service.basic;

import discodeit.dto.binarycontent.AddBinaryContentRequest;
import discodeit.dto.message.CreateMessageRequest;
import discodeit.dto.message.UpdateMessageRequest;
import discodeit.entity.*;
import discodeit.repository.*;
import discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepo;
    private final MessageRepository messageRepo;
    private final ChannelRepository channelRepo;
    private final UserStatusRepository userStatusRepo;
    private final BinaryContentRepository binaryContentRepo;

    @Override
    public Message create(CreateMessageRequest request) {
        // 예외처리
        if (request.messageDetail() == null || request.messageDetail().isEmpty()) {
            throw new IllegalArgumentException("[error] 유효하지 않은 메세지 형식입니다.");
        }
        if (request.userId() == null || request.userId().equals(new UUID(0L, 0L)) || !userRepo.existsById(request.userId())) {
            throw new IllegalArgumentException("[error] 존재하지 않는 사용자는 메세지를 전송할 수 없습니다.");
        }
        if (request.channelId() == null || request.channelId().equals(new UUID(0L, 0L)) || !channelRepo.existsById(request.channelId())) {
            throw new IllegalArgumentException("[error] 존재하지 않는 채널에서 메세지를 전송할 수 없습니다.");
        }
//        if (channelRepo.findAllByUserId(request.userId()) == null || channelRepo.findAllByUserId(request.userId()).isEmpty()) {
//            throw new IllegalArgumentException("[error] 메세지 작성자가 해당 채널의 참여자가 아닙니다.");
//        }

        Message message = new Message(request.userId(), request.channelId(), request.messageDetail(), null);
        updateUserStatus(userStatusRepo, message.getSendUserId());
        return messageRepo.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        Message message = messageRepo.findById(messageId).orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 메세지 ID입니다."));
        updateUserStatus(userStatusRepo, message.getSendUserId());
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (!channelRepo.findById(channelId).orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 채널 ID입니다.")).getType().equals(ChannelType.PRIVATE)) {
            return messageRepo.findAllByChannelId(channelId).stream().toList();
        } else {
            throw new IllegalArgumentException("[error] PRIVATE 채널은 메세지 목록을 확인할 수 없습니다.");
        }
    }

    @Override
    public Message update(UpdateMessageRequest request) {
        Message message = messageRepo.findById(request.messageId()).orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 메세지 ID입니다."));

        if (request.newMessageDetail() == null || request.newMessageDetail().isBlank()) {
            throw new IllegalArgumentException("[error] 빈 메세지는 전송할 수 없습니다");
        }

        // 메세지는 작성한 사람만 수정할 수 있음
//        if (!message.getId().equals(request.writerId())) {
//            throw new IllegalArgumentException("[error] 작성자가 아니면 메세지를 수정할 수 없습니다.");
//        }

        for (UUID id : request.attachmentIds()) {
            if (message.getAttachmentIds().contains(id)) {
                message.getAttachmentIds().remove(id);
            } else {
                message.getAttachmentIds().add(id);
            }
        }

        message.update(request.newMessageDetail());
        updateUserStatus(userStatusRepo, message.getSendUserId());
        return messageRepo.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepo.findById(messageId).orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 메세지 ID입니다."));

//        if (!message.getId().equals(writerId)) {
//            throw new IllegalArgumentException("[error] 작성자가 아니면 메세지를 수정할 수 없습니다.");
//        }

        message.getAttachmentIds().forEach(binaryContentRepo::deleteById);
        messageRepo.deleteById(messageId);
        updateUserStatus(userStatusRepo, message.getSendUserId());
        System.out.println("[삭제 완료]");
    }

    private void updateUserStatus(UserStatusRepository userStatusRepo, UUID userId) {
        UserStatus userStatus = userStatusRepo.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User ID입니다."));
        userStatus.update(Instant.now());
    }

}
