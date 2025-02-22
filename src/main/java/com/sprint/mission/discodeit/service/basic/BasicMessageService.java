package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
// TODO : 마지막 return null; 다시 보기
public class BasicMessageService implements MessageService {
    @Autowired
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository; // TODO : File*Repository에 @Repository 추가하니까 밑줄 사라짐

    @Override
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("아이디가 " + channelId + "인 채널이 존재하지 않습니다.");
        }
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("아이디가" + authorId + "인 회원이 존재하지 않습니다.");
        }

        // 다수의 첨부파일 등록
        List<UUID> attachmentIds = new ArrayList<>();

        for (BinaryContentCreateRequest attachmentRequest : binaryContentCreateRequests) {
            String fileName = attachmentRequest.fileName();
            String contentType = attachmentRequest.contentType();
            byte[] bytes = attachmentRequest.bytes();

            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
            attachmentIds.add(createdBinaryContent.getId());
        } // createdBinaryContent에도 들어가고, attachmentIds에도 들어가는 건가..?

        String content = messageCreateRequest.content();
        Message message = new Message(
                content,
                channelId,
                authorId,
                attachmentIds
        );
        Message msg = messageRepository.save(message);

        if(msg != null) {
            System.out.println("생성된 메세지: " + message);
            return message;
        }else{
            return null;
        }
    }

    @Override
    public Optional<Message> find(UUID messageId) {
        Optional<Message> msg = messageRepository.findById(messageId);
        msg.ifPresent(m -> System.out.println("조회된 메세지: " + m));
        return msg;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> msgs = messageRepository.findAllByChannelId(channelId);
        if(msgs != null && !msgs.isEmpty()){
            System.out.println("전체 메세지 목록: " + msgs);
            return msgs;
        } else {
            System.out.println("메세지 목록이 비어 있습니다.");
            return Collections.emptyList(); // 비어 있을 경우 빈 리스트 반환
        }
    }

    @Override
    // TODO : 메세지를 업데이트 하는 주체 authorId도 확인해봐야하는 거 아닌가?
    // TODO 질문 : 객체를 엽데이트함 -> 이걸 다시 레포지토리.save해줘.. 왜? @Transactional...?
    // 파라미터로 메세지 객체를 통째로 줄지 아이디를 줄지 모르겠음
    // 반환타입 설정하는 법 잘 모르겠음 -> 서비스는 객체 반환이고 레포지토리는 불린 반환해도 상관없지 않나..?
    // MessageUpdateRequest의 존재 이유가 뭐지

    // 메세지 아이디를 전달했을 때, 그 메세지가 DB에 존재해야함 -> 메세지를 쓴 사람과 요청한 사람이 일치하는지 검사해야함 -> 일치하면 수정
    public void update(UUID messageId, UUID requesterId, MessageUpdateRequest request) {
        // DB에서 해당 메세지를 먼저 조회해야함 -> 예외 처리
        // Optional은 여기서 ifPresent 쓰고 싶을 때만 쓰는 건가..?
        // mR.findById~ 는 Optional 타입이라 그냥 Message 타입인 .getAuthorId()같은 거 직접 호출이 안됨
        //  -> **.orElseThrow()로 null이 아닐 경우를 벗겨주고 Message 타입 변수에 넣어주는 작업
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메세지가 존재하지 않아 수정할 수 없습니다."));

        if(!message.getAuthorId().equals(requesterId)){
            System.out.println("메세지를 수정할 권한이 없습니다.");
        }
        String newContent = request.newContent();
        message.update(newContent);
    }

    @Override
    // 메세지 아이디를 전달했을 때, 그 메세지가 DB에 존재해야함 -> 메세지를 쓴 사람과 요청한 사람이 일치하는지 검사 -> 일치하면 첨부파일 삭제 -> 메세지 삭제
    public void delete(UUID messageId, UUID requesterId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메세지가 존재하지 않습니다."));

        if(!message.getAuthorId().equals(requesterId)){
            System.out.println("메세지를 삭제할 권한이 없습니다.");
        }
        // TODO : 둘 다 예외처리
        binaryContentRepository.deleteById(messageId);
        messageRepository.deleteById(messageId);
    }
}