package com.sprint.mission.service.jcf.main;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.MessageMapper;
import com.sprint.mission.dto.PageResponseMapper;
import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.PageResponse;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.response.ScrollPageResponse;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.BinaryContentStorage;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.addOn.BinaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

//import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryService binaryService;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;


    @Override
    public Message create(MessageDtoForCreate responseDto, List<BinaryContentDtoForCreate> binaryContentDtoForCreateList) {

        User author = userRepository.findById(responseDto.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));

        Channel writtenPlace = channelRepository.findById(responseDto.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));

        Message createdMessage = messageMapper.toEntity(writtenPlace, author, responseDto.content());
        //Message createdMessage = new Message(writtenPlace, author, responseDto.content());

        log.info("attachmentsDto: {}", binaryContentDtoForCreateList);
        if (!binaryContentDtoForCreateList.isEmpty()) {
            for (BinaryContentDtoForCreate bcd : binaryContentDtoForCreateList) {
                BinaryContent createdBinaryContent = binaryService.create(bcd);
                log.info("메시지의 생성된 BinaryContent: {}", createdBinaryContent);
                binaryContentStorage.put(createdBinaryContent.getId(), bcd.bytes());
                createdMessage.addAttachment(createdBinaryContent);
            }
        }
        return messageRepository.save(createdMessage);
    }

    @Override
    public Message update(UUID messageId, MessageDtoForUpdate updateDto) {
        Message updatingMessage = this.findById(messageId);
        updatingMessage.update(updateDto.content());
        return updatingMessage;
    }


    @Override
    public Message findById(UUID channelId) {
        return messageRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
    }

    @Override
    public List<PageResponse<MessageDto>> findAllByChannelId(UUID channelId, Pageable pageable) {
        if (!channelRepository.existsById(channelId)) {
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }

        Page<Message> pageMessages;
        List<PageResponse<MessageDto>> messageDtoList = new ArrayList<>();
        do {
            pageMessages = messageRepository.findPagingAllByChannel_Id(channelId, pageable);
            log.info("현재 페이지 : {}", pageMessages.getNumber());
            log.info("dto 변환 전 channel 정보 : {}", pageMessages.getContent().get(0).getChannel());
            pageMessages.map((message) -> {
                System.out.println("message.getAuthor().getStatus() = " + message.getAuthor().getStatus());
                MessageDto dtoMessage = messageMapper.toDto(message);
                System.out.println("dtoMessage.author().online() = " + dtoMessage.author().online());
                return message;
            });
            PageResponse<MessageDto> messagePageResponse = pageResponseMapper.fromPage(pageMessages.map(messageMapper::toDto));
            messageDtoList.add(messagePageResponse);
            log.info("생성한 DTO : {}", pageMessages);
            log.info("다음 페이지 여부 : {}", pageMessages.hasNext());
            pageable = pageMessages.nextPageable();
        } while (pageMessages.hasNext());

        return messageDtoList;
    }

    // 스크롤링
    // CREATED_AT이 겹칠 경우 어떻게 해결해야하는지 (쿼리보면 spring data jpa가 id기준 정렬도 자동 추가해주나?)
    public List<ScrollPageResponse<MessageDto>> findAllByChannelIdWithScroll(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }

        List<ScrollPageResponse<MessageDto>> messageDtoList = new ArrayList<>();

        Long totalMessageCount = messageRepository.countByChannel_Id(channelId);
        ScrollPosition position = ScrollPosition.keyset();

        while (true){
            Window<MessageDto> messageDtoWindow = messageRepository
                    .findFirst50ByChannel_IdOrderByCreatedAtDesc(channelId, (KeysetScrollPosition) position)
                    .map(messageMapper::toDto);

            messageDtoList.add(pageResponseMapper.toScrollPageResponse(messageDtoWindow, position, totalMessageCount));

            // 포지션 초기화
            position = getScrollPosition(messageDtoWindow);
            if (!messageDtoWindow.hasNext()) {
                break;
            }
        }
        return messageDtoList;
    }

    private ScrollPosition getScrollPosition(Window<MessageDto> messageDtoWindow) {
        MessageDto lastDto = messageDtoWindow.getContent().getLast();
        Map<String, Object> keysetMap = new HashMap<>();
        keysetMap.put("createdAt", lastDto.createdAt());
        keysetMap.put("id", lastDto.id());
        return ScrollPosition.forward(keysetMap);
    }

    @Override
    public void delete(UUID messageId) {
        //Message deletingMessage = this.findById(messageId);
        // BinaryContent랑 cascade Remove관계라
        if (!messageRepository.existsById(messageId)) {
            throw new CustomException(ErrorCode.NO_SUCH_MESSAGE);
        } else {
            messageRepository.deleteById(messageId);
        }
    }


    @Override
    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.deleteAllByChannel_Id(channelId);
    }
}
