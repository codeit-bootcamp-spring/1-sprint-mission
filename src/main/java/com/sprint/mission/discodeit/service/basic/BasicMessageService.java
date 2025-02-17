package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final FileIOHandler fileIOHandler;

    //해당 메세지 객체 리턴
    @Override
    public MessageDto findMessageById(UUID channelId, UUID messageId) {
        if (messageId == null){System.out.println("메세지 반환 실패. 입력값이 null인 상태입니다."); return null; }
        Message message = messageRepository.getMessage(channelId, messageId);
        return message!=null ? MessageDto.from(message) : null;
    }

    //채널에 속한 메세지 해쉬맵으로 반환. 채널 해쉬맵 없으면 null 반환
    @Override
    public LinkedHashMap<UUID, Message> getChannelMessagesMap(UUID channelId) {
        if (channelId == null){System.out.println("메세지맵 반환 실패. 입력값이 null인 상태입니다."); return null; }
        return messageRepository.getChannelMessagesMap(channelId);
    }

    //해당채널에 속한 메세지 리스트 반환. 생성된 메세지 존재하지 않을시 빈 리스트 반환.
    @Override
    public List<MessageDto> findAllMessagesByChannelId(UUID channelId) {
        if (channelId == null){
            System.out.println("채널별 메세지리스트 반환 실패. 입력값이 null인 상태입니다.");
            return null;
        }
        HashMap<UUID, Message> messagesMap = messageRepository.getChannelMessagesMap(channelId);
        return messagesMap.size()>0 ? messagesMap.values().stream().map(message -> MessageDto.from(message)).collect(Collectors.toList()):Collections.emptyList();
    }

    //첨부파일 포함해서 메세지 생성.
    @Override
    public UUID createMessageWithBinaryContents(UUID authorId, UUID channelId, String content, String... attachmentsPaths) {
        if (authorId == null || channelId == null || content == null || attachmentsPaths == null) {System.out.println("메세지 생성 실패. 입력값을 확인해주세요."); return null;}
        if (userRepository.isUserExistByUUID(authorId) == false || channelRepository.isChannelExist(channelId) == false){
            System.out.println("메세지 생성 실패. 입력한 id를 가진 유저나 채널이 존재하지 않습니다."); }
        List<BufferedImage> images = Stream.of(attachmentsPaths).map(path -> fileIOHandler.loadImage(path)).collect(Collectors.toList());
        ArrayList<UUID> binaryImagesId = new ArrayList<>();
        for (BufferedImage image : images){
            if (image == null){ System.out.println("불러오기 실패한 이미지는 건너뜁니다. "); continue; }
            BinaryContent binaryimage = new BinaryContent(authorId, BinaryContentType.Aettached_File, image);
            binaryContentRepository.saveBinaryContent(binaryimage);
            binaryImagesId.add(binaryimage.getId());
        }
        Message newMessage = new Message(userRepository.getUserById(authorId), channelRepository.getChannel(channelId), content, binaryImagesId);
        messageRepository.saveMessage(channelId, newMessage);
        System.out.println("메세지 생성 성공!");
        return newMessage.getId();
    }

    //메세지 생성.
    @Override
    public UUID createMessage(UUID authorId, UUID channelId, String content) {
        if (authorId == null || channelId == null || content == null) { System.out.println("메세지 생성 실패. 입력값을 확인해주세요."); return null; }
        if (userRepository.isUserExistByUUID(authorId) == false || channelRepository.isChannelExist(channelId) == false){
            System.out.println("메세지 생성 실패. 입력한 id를가진 유저나 채널이 존재하지 않습니다."); return null;}
        Message newMessage = new Message(userRepository.getUserById(authorId), channelRepository.getChannel(channelId), content);
        messageRepository.saveMessage(channelId, newMessage);
        System.out.println("메세지 생성 성공!");
        return newMessage.getId();
    }

    //UUID를 통해 메세지 객체를 찾아 삭제
    @Override
    public boolean deleteMessage(UUID channelId, UUID messageId) {
        if (messageId == null || channelId == null || messageRepository.isMessageExist(channelId, messageId)) {
            System.out.println("메세지 삭제 실패. 입력값을 확인해주세요."); return false; }
        Message message = messageRepository.getMessage(channelId, messageId);
        if (message.getBinaryContentsId().size() > 0){
            message.getBinaryContentsId().stream().forEach(binaryContentId -> binaryContentRepository.deleteBinaryContent(binaryContentId));
        }
        messageRepository.deleteMessage(channelId, messageId);
        System.out.println("메세지 삭제 성공!");
        return true;
    }

    //메세지 내용 변경
    @Override
    public boolean reviseMessageContent(UUID channelId, UUID messageId, String newContent) {
        if (channelId == null|| messageId == null || newContent == null || messageRepository.isMessageExist(channelId, messageId)) { System.out.println("메세지 수정 실패. 입력값을 확인해주세요.");return false;}
        Message message = messageRepository.getMessage(channelId, messageId);
        message.setContent(newContent);
        messageRepository.saveMessage(channelId, message);
        System.out.println("메세지 수정 성공!");
        return true;
    }

    //메세지 존재여부 확인
    @Override
    public boolean isMessageExist(UUID channelId, UUID messageId) {
        if (messageId == null || channelId == null ) {System.out.println("메세지 존재여부 확인 실패. 입력값을 확인해주세요."); return false; }
        return messageRepository.isMessageExist(channelId, messageId);
        }
}
