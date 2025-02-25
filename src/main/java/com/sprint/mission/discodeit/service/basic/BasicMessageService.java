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
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    // TODO : FileBinaryContentRepository에 @Repository 추가하니까 밑줄 사라짐 -> 해당 레포지토리 구현체가 빈으로 등록됐다는 말
    // TODO : 즉, 생성자 생성 -> 빈 등록 (@R.A.C, @Service), 의존성 주입 -> "빈 저장소에서 해당 레포지토리 타입으로 검색 후, 구현체 빈을 갖고와 주입해주는데 " -> FileBinaryContentRepository가 빈 저장소에 없었으니까 오류였던 것.

    @Override
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> requests) {
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("아이디가 " + channelId + "인 채널이 존재하지 않습니다.");
        }
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("아이디가" + authorId + "인 회원이 존재하지 않습니다.");
        }

        // 다수의 첨부파일 등록
        List<UUID> attachmentIds = new ArrayList<>(); // id를 저장하기 위한 리스트

        for (BinaryContentCreateRequest attachmentRequest : requests) {
            String fileName = attachmentRequest.fileName();
            String contentType = attachmentRequest.contentType();
            byte[] bytes = attachmentRequest.bytes();

            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            binaryContentRepository.save(binaryContent);
            attachmentIds.add(binaryContent.getId());
        }

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
    // 메세지 아이디를 전달했을 때, 그 메세지가 DB에 존재해야함 -> 메세지를 쓴 사람과 요청한 사람이 일치하는지 검사해야함 -> 일치하면 수정
    public Message update(UUID messageId, UUID requesterId, MessageUpdateRequest request) {
        // DB에서 해당 메세지를 먼저 조회해야함 -> 예외 처리
        // mR.findById~ 는 Optional 타입이라 그냥 Message 타입인 .getAuthorId() 직접 호출이 안됨
        //  -> **.orElseThrow()로 null이 아닐 경우를 벗겨주고 Message 타입 변수에 넣어주는 작업
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메세지가 존재하지 않아 수정할 수 없습니다."));

        if(!message.getAuthorId().equals(requesterId)){
            System.out.println("메세지를 수정할 권한이 없습니다.");
        }
        String newContent = request.newContent();
        message.update(newContent);
        return messageRepository.save(message);

        /* TODO : 객체를 엽데이트함 -> 이걸 다시 레포지토리.save해줘. -> JPA에서는 update 호출만으로 바로 반영되지 않는다 -> 레포지토리.save로 알려줘야 함 -> 그러면 DB에 중복되어 저장되는 거 아니야?
        TODO NO! DB와 스프링 애플리케이션 사이에 캐시=영속성 컨텍스트가 있는데, 변경사항이 바로 DB에 반영되지 않고 캐시에 저장되어 있다가 트랜잭션이 끝나면 한꺼번에 DB에 반영된다!(flush) "" 트랜잭션 ""
        TODO -> 객체가 JPA 영속성 컨텍스트(캐시)에 있는지 여부에 따라 (기존 객체인지 새로운 객체인지 보고) 기존 객체이면 insert가 아닌 update를 하기 때문에 DB에 중복이 아니라 update로 들어감.
        */
    }

    @Override
    // 메세지 아이디를 전달했을 때, 그 메세지가 DB에 존재해야함 -> 메세지를 쓴 사람과 요청한 사람이 일치하는지 검사 -> 일치하면 첨부파일 삭제 -> 메세지 삭제
    public void delete(UUID messageId, UUID requesterId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메세지가 존재하지 않습니다."));

        if(!message.getAuthorId().equals(requesterId)){
            System.out.println("메세지를 삭제할 권한이 없습니다.");
        }
        binaryContentRepository.deleteById(messageId);
        messageRepository.deleteById(messageId);
    }
}