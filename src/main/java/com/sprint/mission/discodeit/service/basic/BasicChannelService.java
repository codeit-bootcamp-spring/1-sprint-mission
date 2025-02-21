package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Override
    public ChannelDto create(ChannelDto dto, List<UUID> userList) {
        //private 신청 검토
        Channel savedChannel = channelRepository.save(dto.type());
        userList.forEach(userId -> readStatusRepository.save(new ReadStatusDto(null, userId, savedChannel.getId(), null, null, null)));
        return new ChannelDto(savedChannel);
    }

    @Override
    public ChannelDto create(ChannelDto dto) {
        //public 신청 검토
        Channel savedChannel = channelRepository.save(dto.name(), dto.type());
        //readStatus 업데이트 필요

        return new ChannelDto(savedChannel);
    }

    @Override
    public ChannelFindDto find(UUID id) {
        Channel channel = channelRepository.findById(id);
        if(channel == null){
            throw new IllegalStateException("채널을 찾을 수 없습니다.");
        }
        List<Message> messagesById = messageRepository.findMessagesById(id);
        if(messagesById.isEmpty()) {return new ChannelFindDto(channel, null);}
        Instant updatedAt = messagesById.stream().min(Comparator.comparing(Message::getUpdatedAt)).get().getUpdatedAt();//가장 최근의 메시지 시간 정보

        if (channel.getType() == ChannelType.PRIVATE) {
            //Private 채널에 참여한 User의 ID 정보 포함
            List<ReadStatus> privateChannelUsers = readStatusRepository.findAllByChannelId(id);
            List<UUID> userList = privateChannelUsers.stream().map(ReadStatus::getUserId).toList();
            return new ChannelFindDto(channel, updatedAt, userList);
        } else {
            return new ChannelFindDto(channel, updatedAt);
        }
    }

    @Override
    public ChannelFindAllDto findAll() {
        List<Channel> channels = channelRepository.findAll();
        Map<UUID, Instant> latestMessages = new HashMap<>();
        List<UUID> list = channels.stream().map(Channel::getId).toList(); //채널 ID
        for (UUID uuid : list) {
            List<Message> messagesById = messageRepository.findAll().stream().filter(s -> s.getChannelId().equals(uuid)).toList();
            //효율성? 고려해야될 듯
            if(messagesById.isEmpty()) {
                continue;
            }
            Instant updatedAt = messagesById.stream().min(Comparator.comparing(Message::getUpdatedAt)).get().getUpdatedAt();
            latestMessages.put(uuid, updatedAt); //채널 별 가장 최근의 메시지
        }
        List<List<UUID>> userList = new ArrayList<>();
        for (Channel channel : channels) {
            if (channel.getType() == ChannelType.PRIVATE) {
                if(readStatusRepository.findAllByChannelId(channel.getId())==null) { continue; }
                //Private 채널에 참여한 User의 ID 정보 포함
                List<ReadStatus> privateChannelUsers = readStatusRepository.findAllByChannelId(channel.getId());
                userList.add(privateChannelUsers.stream().map(ReadStatus::getUserId).toList());
            }
        }
        return new ChannelFindAllDto(channels, latestMessages, userList);
        //채널, 채널마다 시간 정보, 유저 리스트
    }

    @Override
    public ChannelFindAllDto findAllByUserId(UUID userId) {
        List<Channel> channels = readStatusRepository.findAllByUserId(userId).stream().map(ReadStatus::getChannelId).map(channelRepository::findById).toList();
        Map<UUID, Instant> latestMessagesInstant = new HashMap<>();
        List<UUID> list = channels.stream().map(Channel::getId).toList(); //채널 ID
        for (UUID uuid : list) {
            List<Message> messagesById = messageRepository.findMessagesById(uuid);
            Instant updatedAt = messagesById.stream().min(Comparator.comparing(Message::getUpdatedAt)).get().getUpdatedAt();
            latestMessagesInstant.put(uuid, updatedAt);
        }
        //채널 속 해당 유저 ID에 대한 ReadStatus 값 최신화
        List<ReadStatus> findAllByUserId = readStatusRepository.findAllByUserId(userId);
        for (ReadStatus readStatus : findAllByUserId) {
            readStatusRepository.update(new ReadStatusDto(readStatus), new ReadStatusDto(readStatus, Instant.now()));
        }
        return new ChannelFindAllDto(channels, latestMessagesInstant, null);
    }

    @Override
    public void updateChannel(ChannelDto dto) {
        channelRepository.update(dto.id(), dto.name());
    }

    @Override
    public void deleteChannel(UUID id) {
        //채널과 관련된 메시지 삭제 (Message.ser 속 객체 )
        List<UUID> messages = channelRepository.findMessagesByChannelId(id);
        if(!messages.isEmpty()) {
            int count = 0;
            for (UUID message : messages) {
                boolean delete = messageRepository.delete(message);
                System.out.println("delete = " + delete+" message = " + message);
                if (delete == true ) count++;
            }
            System.out.println("채널과 연관된 메시지 삭제 성공 - " + count+"개");
        }
        //채널 삭제 ( 내부 Map )
        channelRepository.delete(id);
        //채널과 관련된 ReadStatus 삭제
        if(readStatusRepository.findAllByChannelId(id)==null) {
            System.out.println("채널 속 유저 상태 정보가 없습니다."); return;
        }
        readStatusRepository.deleteByChannelId(id);
    }

    @Override
    public UUID addMessageInChannel(MessageDto messageDto) {
        //채널에 메시지 추가하기
        UUID senderId = messageDto.senderId();
        UUID channelId = messageDto.channelId();
        if(senderId == null || channelId == null) {
            throw new IllegalStateException("아이디와 채널 아이디를 추가해주세요.");
        }
        UUID savedMessageId = messageRepository.save(senderId, channelId, messageDto.content());
        channelRepository.addMessage(channelId, savedMessageId);
        return savedMessageId;
    }

    @Override
    public List<UUID> findAllMessagesByChannelId(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new IllegalStateException("채널을 찾을 수 없습니다.");
        }
        List<UUID> messagesByChannelId = channelRepository.findMessagesByChannelId(channelId);
        return new ArrayList<>(messagesByChannelId);
    }
}
