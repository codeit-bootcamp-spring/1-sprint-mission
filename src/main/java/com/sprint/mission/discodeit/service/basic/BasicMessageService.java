package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
//
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;
    private final InputHandler inputHandler;

    @Override
    public UUID createMessage(MessageCreateRequest messageCreateRequest) {
        Message message = new Message(messageCreateRequest.channelId(), messageCreateRequest.authorId(), messageCreateRequest.messageText());

        
        // 첨부파일 업로드 단계 (사용자)   | (개발)
        // (1) 첨부파일 아이콘 클릭         |
        // (2) UI 창에서 선택하기          |
        // (3) 한 개, 또는 여러 개 선택     |
        // (4) 업로드 아이콘 클릭 또는 엔터  |  첨부파일과 첨부파일 개수 확인( 컨트롤러 쪽에서 첨부된 파일의 개수를 int binaryContentNum 으로 전달하면 되지 앟ㄴ으려나? )
        //                               |  첨부파일 개수만큼 binaryContent 도메인 객체 생성 * 이때 Message content가 없어도 Message가 생성된다.

        // binaryContent 도메인 객체 생성
        // sprint3 기준 : 개수 제한은 두지 않음
        if(messageCreateRequest.binaryContentNum() != 0){
            for(int i = 0; i < messageCreateRequest.binaryContentNum(); i++) {
                BinaryContent binaryContent = new BinaryContent();
                binaryContent.saveUserProfileImage(message.getId());
            }
        }

        messageRepository.saveMessage(message);
        return message.getId();
    }

    @Override
    public Collection<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllMessages().stream()
                .filter( message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Message getMessageById(UUID id) {
        return messageRepository.findMessageById(id)
                .orElseThrow(()-> new NoSuchElementException("해당 메세지가 없습니다."));
    }

    @Override
    public void updateMessageText(MessageUpdateRequest messageUpdateRequest) {
        // String messageText = inputHandler.getNewInput();
        Optional<Message> messageOptional = messageRepository.findMessageById(messageUpdateRequest.messageId());

        Message message = messageOptional.orElseThrow( () -> new NoSuchElementException("메세지가 존재하지 않습니다."));

        message.setMessageText(messageUpdateRequest.newMessage());

        messageRepository.saveMessage(message);
    }

    @Override
    public void deleteMessageById(UUID id) {
        String keyword = inputHandler.getYesNOInput();
        if(keyword.equalsIgnoreCase("y")){
            messageRepository.deleteMessageById(id);
            binaryContentService.deleteBinaryContentByMessageId(id);
        }
    }
}
