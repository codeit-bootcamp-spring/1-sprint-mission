package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.MessageDeletedEvent;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FilePathContents;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {


    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public MessageDto create(CreateMessageRequestDto createMessageRequestDto) throws IOException {

        UUID channelId = createMessageRequestDto.channelId();
        Channel channel = channelService.find(channelId);

        UUID authorId = createMessageRequestDto.authorId();
        User author = userService.find(authorId);

        String context = createMessageRequestDto.context();
        List<MultipartFile> images = createMessageRequestDto.images();

        List<BinaryContent> attachments = new ArrayList<>();
        if (!images.isEmpty()){
            for (MultipartFile image : images) {
                CreateBinaryContentRequestDto createBinaryContentRequestDto = new CreateBinaryContentRequestDto(image, FilePathContents.MESSAGEIMAGE_DIR);
                BinaryContent attachment = binaryContentService.create(createBinaryContentRequestDto);
                attachments.add(attachment);
            }
        }

        Message message = new Message(channel, author, context, attachments);

        messageRepository.save(message);

        return MessageMapper.INSTANCE.toDto(message);
    }

    @Override
    public MessageDto find(UUID id) {

        Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다."));

        return MessageMapper.INSTANCE.toDto(message);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelID) {

        Channel channel = channelService.find(channelID);

        return messageRepository.findAll().stream()
                .filter(message -> message.getChannel().equals(channel))
                .map(MessageMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public List<MessageDto> findAllByUserId(UUID userId) {

        User user = userService.find(userId);

        return messageRepository.findAll().stream()
                .filter(message -> message.getAuthor().equals(user))
                .map(MessageMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public MessageDto updateContent(UpdateMessageRequestDto updateMessageRequestDto) {

        Message message = messageRepository.findById(updateMessageRequestDto.id()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다."));

        message.updateContent(updateMessageRequestDto.context());

        messageRepository.save(message);

        return MessageMapper.INSTANCE.toDto(message);
    }

    @Override
    public void delete(UUID id) {

        Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다."));

        messageRepository.deleteById(id);

        // 메시지 삭제 이벤트 발생
        eventPublisher.publishEvent(new MessageDeletedEvent(message));
    }
}
