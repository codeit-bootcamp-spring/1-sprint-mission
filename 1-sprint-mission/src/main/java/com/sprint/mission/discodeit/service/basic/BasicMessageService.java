package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.interfacepac.*;
import com.sprint.mission.discodeit.service.interfacepac.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;



    @Override
    public MessageResponseDTO create(MessageCreateDTO messageCreateDTO) {
        //사용자 조회
        User user = userRepository.findById(messageCreateDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found" + messageCreateDTO.userId()));
        //채널 조회
        Channel channel = channelRepository.findById(messageCreateDTO.channelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found" + messageCreateDTO.channelId()));
        //메세지 생성
        Message newMessage = new Message(user, channel, messageCreateDTO.content());
        messageRepository.save(newMessage);

        //첨부파일 저장(선택적으로)
        List<BinaryContent> attachments = new ArrayList<>();
        if (messageCreateDTO.attachments() != null && !messageCreateDTO.attachments().isEmpty()) {
            attachments = messageCreateDTO.attachments().stream()
                    .map(fileDTO -> new BinaryContent(
                            UUID.randomUUID(),
                            user.getId(),
                            newMessage.getId(),
                            fileDTO.filename(),
                            "application/octet-stream",
                            fileDTO.fileData()
                    ))
                    .toList();
            attachments.forEach(binaryContentRepository::save);
        }
        //readStatus 업데이트
        ReadStatus readStatus = readStatusRepository.findByUserAndChannel(user, channel)
                .orElseGet(() -> new ReadStatus(user, channel, Instant.now()));

        readStatus.updateReadTime(newMessage.getCreatedAt());
        readStatusRepository.save(readStatus);

        // 메시지 응답 DTO 반환
        return new MessageResponseDTO(
                newMessage.getId(),
                user.getId(),
                channel.getId(),
                newMessage.getContent(),
                attachments.stream().map(BinaryContentDTO::fromEntity).toList(),
                newMessage.getCreatedAt()
        );
    }

    @Override
    public Message find(User user) {
        try {
            return messageRepository.findAll().stream()
                    .filter(message -> message.getUser().equals(user))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(user + " not exists"));
        }catch (IllegalArgumentException e){
            System.out.println("Failed to read message: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Message> findAll() {
        try {
            List<Message> messages = messageRepository.findAll();

            if (messages.isEmpty()) {
                throw new IllegalStateException("No messages found in the system.");
            }
            return messages;

        }catch (IllegalArgumentException e){
            System.out.println("Failed to read all messages: " + e.getMessage());
        }

        return List.of();
    }

    @Override
    public List<MessageResponseDTO> findMessagesByChannelId(UUID channelId) {
        //채널 확인
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        //특정 채널 메서지 조회
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        if(messages.isEmpty()) {
            return List.of();
        }
        // 메시지를 DTO로 변환 반환
        return messages.stream()
                .map(message -> {
            // 메세지에 연결되어있는 첨부파일 가져오기
            List<BinaryContentDTO> attachments = binaryContentRepository.findAllByMessageId(message.getId())
                    .stream()
                    .map(BinaryContentDTO::fromEntity)  //BinaryContent -> BinaryContentDTO로 타입 변경.
                    .toList();
            // 메세지 리스폰스 생성, 반환
            return new MessageResponseDTO(
                    message.getId(),
                    message.getUser().getId(),
                    message.getChannel().getId(),
                    message.getContent(),
                    attachments,
                    message.getCreatedAt()
            );
        }).toList();

    }

    @Override
    public MessageResponseDTO update(MessageUpdateDTO updateDTO) {
        //메세지 조회
        Message messageToUpdate = messageRepository.findById(updateDTO.messageId())
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        //메세지 내용 업데이트, 저장
        messageToUpdate.update(updateDTO.newContent());
        messageRepository.save(messageToUpdate);
        //첨부파일 처리(선택 변경)
        if (updateDTO.attachments() != null && !updateDTO.attachments().isEmpty()) {
            //기존 첨부파일 삭제
            binaryContentRepository.deleteByMessageId(messageToUpdate.getId());
            //새로운 첨부파일 저장
            List<BinaryContent> newAttachments = updateDTO.attachments().stream()
                    .map(fileDTO -> new BinaryContent(
                            UUID.randomUUID(),
                            messageToUpdate.getUser().getId(),
                            messageToUpdate.getId(),
                            fileDTO.filename(),
                            "application/octet-stream",
                            fileDTO.fileData()

                    ))
                    .toList();
            newAttachments.forEach(binaryContentRepository::save);
        }
        ReadStatus readStatus = readStatusRepository.findById(updateDTO.messageId())
                .orElseGet(() -> new ReadStatus(
                        messageToUpdate.getUser(),
                        messageToUpdate.getChannel(),
                        Instant.now()
                ));
        readStatus.updateReadTime(Instant.now());
        readStatusRepository.save(readStatus);

        return new MessageResponseDTO(
                messageToUpdate.getId(),
                messageToUpdate.getUser().getId(),
                messageToUpdate.getChannel().getId(),
                messageToUpdate.getContent(),
                binaryContentRepository.findAllByMessageId(messageToUpdate.getId())
                        .stream()
                        .map(BinaryContentDTO::fromEntity)
                        .toList(),
                messageToUpdate.getCreatedAt()
        );

    }

    @Override
    public void delete(UUID messageId) {
        //메세지 조회
        Message messageToDelete = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        //첨부파일 삭제
        binaryContentRepository.deleteByMessageId(messageId);
        //메시지 삭제
        messageRepository.deleteById(messageId);

       log.error("Message deleted: {}", messageId);

    }
}
