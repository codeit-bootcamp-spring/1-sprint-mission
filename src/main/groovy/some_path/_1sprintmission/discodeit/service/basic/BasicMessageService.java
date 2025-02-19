package some_path._1sprintmission.discodeit.service.basic;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import some_path._1sprintmission.discodeit.dto.FileAttachmentRequestDTO;
import some_path._1sprintmission.discodeit.dto.MessageCreateRequestDTO;
import some_path._1sprintmission.discodeit.dto.MessageUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.BinaryContent;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.BinaryContentRepository;
import some_path._1sprintmission.discodeit.repository.ChannelRepository;
import some_path._1sprintmission.discodeit.repository.MessageRepository;
import some_path._1sprintmission.discodeit.repository.UserRepository;
import some_path._1sprintmission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    //
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;


    @Override
    public Message create(MessageCreateRequestDTO request) {
        UUID channelId = request.getChannelId();
        UUID authorId = request.getAuthorId();

        // 채널과 유저가 존재하는지 확인
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found with id " + channelId);
        }
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("Author not found with id " + authorId);
        }

        // 메시지 생성
        Message message = new Message(channelId, authorId, request.getContent());
        message = messageRepository.save(message);

        // 첨부파일이 있는 경우 BinaryContent 생성
        if (request.getAttachments() != null) {
            for (FileAttachmentRequestDTO attachment : request.getAttachments()) {
                BinaryContent binaryContent = new BinaryContent(
                        attachment.getData(),
                        attachment.getContentType(),
                        userRepository.findById(authorId).orElseThrow(() -> new NoSuchElementException("User not found")),
                        message
                );
                binaryContentRepository.save(binaryContent); // 저장소에 저장
            }
        }

        return message;
    }


    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public Message update(MessageUpdateRequestDTO request) {
        return messageRepository.update(request);
    }
    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        // 1. 메시지에 연결된 BinaryContent가 있는지 확인하고 삭제
        binaryContentRepository.deleteByMessageId(messageId);

        // 2. 메시지 삭제
        messageRepository.deleteById(messageId);
    }
}
