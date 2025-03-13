package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.FindMessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.MessageDeletedEvent;
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
import java.util.Map;
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
    public FindMessageResponseDto create(CreateMessageRequestDto createMessageRequestDto) throws IOException {

        UUID channelId = createMessageRequestDto.channelId();
        channelService.channelIsExist(channelId);
        Channel channel = channelService.find(channelId);

        UUID authorId = createMessageRequestDto.authorId();
        userService.userIsExist(authorId);
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

        return FindMessageResponseDto.fromEntity(message);
    }

    @Override
    public FindMessageResponseDto find(UUID id) {

        messageIsExist(id);

        Message message = messageRepository.load().get(id);

        return FindMessageResponseDto.fromEntity(message);
    }

    @Override
    public List<FindMessageResponseDto> findAllByChannelId(UUID channelID) {

        channelService.channelIsExist(channelID);

        return messageRepository.load().values().stream()
                .filter(message -> message.getChannel().equals(channelID))
                .map(FindMessageResponseDto::fromEntity)
                .toList();
    }

    @Override
    public List<FindMessageResponseDto> findAllByUserId(UUID userId) {

        userService.userIsExist(userId);

        return messageRepository.load().values().stream()
                .filter(message -> message.getAuthor().equals(userId))
                .map(FindMessageResponseDto::fromEntity)
                .toList();
    }

    @Override
    public FindMessageResponseDto updateContent(UpdateMessageRequestDto updateMessageRequestDto) {

        messageIsExist(updateMessageRequestDto.id());

        Message message = messageRepository.load().get(updateMessageRequestDto.id());

        message.updateContent(updateMessageRequestDto.context());

        messageRepository.save(message);

        return FindMessageResponseDto.fromEntity(message);
    }

    @Override
    public void delete(UUID id) {

        messageIsExist(id);

        Message message = messageRepository.load().get(id);

        messageRepository.delete(id);

        // 메시지 삭제 이벤트 발생
        eventPublisher.publishEvent(new MessageDeletedEvent(message));
    }

    // 메시지 존재 여부 확인
    @Override
    public void messageIsExist(UUID id) {

        Map<UUID, Message> messages = messageRepository.load();

        if (!messages.containsKey(id)) {
            throw new NoSuchElementException("존재하지 않는 메시지입니다.");
        }
    }
}
