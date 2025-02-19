package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.messageDto.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.messageDto.FindMessageResponseDto;
import com.sprint.mission.discodeit.dto.messageDto.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.event.MessageDeletedEvent;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.FilePathContents;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {


    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UUID create(CreateMessageRequestDto createMessageRequestDto) throws IOException {

        UUID channelId = createMessageRequestDto.channelId();
        UUID writerId = createMessageRequestDto.writerId();
        String context = createMessageRequestDto.context();
        List<MultipartFile> images = createMessageRequestDto.images();

        List<UUID> imagesId = new ArrayList<>();
        if (images != null){
            for (MultipartFile image : images) {
                BinaryContent a = new BinaryContent(image, FilePathContents.MESSAGEIMAGE_DIR);
                imagesId.add(a.getId());
            }
        }

        Message message = new Message(channelId, writerId, context, imagesId);

        messageRepository.save(message);

        return message.getId();
    }

    @Override
    public FindMessageResponseDto find(UUID id) {

        messageIsExist(id);

        Message message = messageRepository.load().get(id);

        return new FindMessageResponseDto(message);
    }

    @Override
    public List<FindMessageResponseDto> findAllByChannelId(UUID channelID) {

        channelService.channelIsExist(channelID);

        return messageRepository.load().values().stream()
                .map(FindMessageResponseDto::new)
                .toList();
    }

    @Override
    public void updateContext(UpdateMessageRequestDto updateMessageRequestDto) {

        Message message = messageRepository.load().get(updateMessageRequestDto.id());

        message.updateContext(updateMessageRequestDto.context());

        messageRepository.save(message);
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
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.");
        }
    }
}
