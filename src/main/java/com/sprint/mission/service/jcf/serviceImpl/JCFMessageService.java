package com.sprint.mission.service.jcf.serviceImpl;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryServiceImpl binaryService;
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

        log.info("attachmentsDto: {}", binaryContentDtoForCreateList);
        if (!binaryContentDtoForCreateList.isEmpty()) {
            binaryContentDtoForCreateList.forEach(bcd -> {
                BinaryContent createdBinaryContent = binaryService.create(bcd);
                binaryContentStorage.put(createdBinaryContent.getId(), bcd.bytes());
                log.info("메시지의 생성된 BinaryContent: {}", createdBinaryContent);
            });
        }
        return messageRepository.save(createdMessage);
    }

    @Override
    public Message update(UUID messageId, MessageDtoForUpdate updateDto) {
        Message updatingMessage = this.findById(messageId);
        return updatingMessage.update(updateDto.content());
    }


    @Override
    public Message findById(UUID channelId) {
        return messageRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MESSAGE));
    }

    // 스크롤링
    @Override
    public List<ScrollPageResponse<MessageDto>> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }

        // 매번 새 페이지마다 요청할지 아니면 한번에 다 가져올지 고민 (중간에 총개수가 바뀔 수 있으니)
        Long totalMessageCount = messageRepository.countByChannel_Id(channelId);
        ScrollPosition position = ScrollPosition.keyset();
        List<ScrollPageResponse<MessageDto>> messageDtoList = new ArrayList<>();
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

    // Page 버전
    @Override
    public List<PageResponse<MessageDto>> findAllByChannelId(UUID channelId, Pageable pageable) {
        if (!channelRepository.existsById(channelId)) {
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }

        Page<Message> pageMessages;
        List<PageResponse<MessageDto>> messageDtoList = new ArrayList<>();
        do {
            pageMessages = messageRepository.findPagingAllByChannel_Id(channelId, pageable);

            Page<MessageDto> dtoPage = pageMessages.map(messageMapper::toDto);
            PageResponse<MessageDto> messagePageResponse = pageResponseMapper.fromPage(dtoPage);
            messageDtoList.add(messagePageResponse);

            pageable = pageMessages.nextPageable();
        } while (pageMessages.hasNext());
        log.info("messageDtoList: {}", messageDtoList);
        return messageDtoList;
    }


    @Override
    public void delete(UUID messageId) {
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


    private ScrollPosition getScrollPosition(Window<MessageDto> messageDtoWindow) {
        MessageDto lastDto = messageDtoWindow.getContent().getLast();
        Map<String, Object> keysetMap = new HashMap<>();
        keysetMap.put("createdAt", lastDto.createdAt());
        keysetMap.put("id", lastDto.id());
        return ScrollPosition.forward(keysetMap);
    }
}
