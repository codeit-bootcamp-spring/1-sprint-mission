package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {
    @Autowired
    private final ChannelRepository channelRepository;
    public BasicChannelService(ChannelRepository channelRepository){this.channelRepository = channelRepository;}
    @Autowired
    private final ReadStatusRepository readStatusRepository;
    public BasicChannelService(ReadStatusRepository readStatusRepository){this.readStatusRepository = readStatusRepository;}
    @Autowired
    private final MessageRepository messageRepository;
    public BasicChannelService(MessageRepository messageRepository){this.messageRepository = messageRepository;}
    @Autowired
    private final BinaryContentRepository binaryContentRepository;
    public BasicChannelService(BinaryContentRepository binaryContentRepository){this.binaryContentRepository = binaryContentRepository;}

    @Override
    public boolean createPublicChannel(ChannelDto channelDto) {
        boolean created = channelRepository.save(channelDto);
        if(created){
            System.out.println("생성된 Public 채널: " + channelDto);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public boolean createPrivateChannel(ChannelDto channelDto) {
        boolean created = channelRepository.save(channelDto);
        if(created){
            // 참여한 User 별로 ReadStatus 생성 및 저장
            for (User user : channelDto.participants()) {
                // 도메인 모델에 선언해서 연결해야할지, Dto로 묶어야할지 이런거 헷갈림
                // 한 채널 - 채널 참가자들, 여러 메세지들 => 여러(참가자1-메세지1 (다대다))
                for (Message msg : channelDto.messages()) {
                    ReadStatus readStatus = new ReadStatus(user, channelDto.channel().getId(), msg.getId());
                    readStatusRepository.save(readStatus);
                }
            }
            System.out.println("생성된 Private 채널: " + channelDto);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public ChannelDto findPublicChannel(ChannelDto channelDto) {
        // 크기 비교 반복문 : 초기값 설정 - 가장 오래된 시간으로 설정
        Instant latestMessageAt = Instant.MIN;
        for (Message msg : channelDto.messages()) {
            if (msg.getCreatedAt().isAfter(latestMessageAt)) {
                latestMessageAt = msg.getCreatedAt();
            }
        }
        System.out.println("조회된 채널" + channelDto.channel().getName() + ", 가장 최근 메세지 시간: " + latestMessageAt);
        return channelDto;
    }

    @Override
    public ChannelDto findPrivateChannelByUserId(ChannelDto channelDto) {
        Optional<Channel> ch = channelRepository.findById(channelDto.channel().getId());
        if(ch.isPresent()){
            System.out.println("조회된 채널" + channelDto.channel().getName() + ", 참가자 목록: " + channelDto.participants());
        }
        return channelDto;
    }

    /* TODO : findAllByUserId 구현 - 어렵다. 더 생각해봐야할 것 같다
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {

    }
    */


    @Override
    public void updatePublicChannel(UpdateChannelDto updateChannelDto) {
        UUID id = updateChannelDto.id();
        boolean updated = channelRepository.updateOne(
                updateChannelDto.id(),
                updateChannelDto.name(),
                updateChannelDto.topic()
        );

        if (updated) {
            Optional<Channel> ch = channelRepository.findById(id);
            ch.ifPresent(c -> System.out.println("수정된 Public 채널 : " + c));
            List<ChannelDto> allChannels = channelRepository.findAll();
            System.out.println("수정 후 전체 채널 목록 : " + allChannels);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        // 삭제 전에 채널 정보 가져오기 (로그를 위해)
        Optional<Channel> channelToDelete = channelRepository.findById(id);

        if (channelToDelete.isEmpty()) {
            System.out.println("삭제하려는 채널이 존재하지 않습니다: ID = " + id);
            return;
        }

        // 관련 도메인 데이터 삭제
        binaryContentRepository.deleteAllByChannelId(id);
        readStatusRepository.deleteAllByChannelId(id);
        messageRepository.deleteAllByChannelId(id);

        // 채널 삭제
        boolean deleted = channelRepository.deleteOne(id);

        if (deleted) {
            System.out.println("삭제된 채널: " + channelToDelete.get()); // 삭제 전 가져온 정보 출력
            List<ChannelDto> allChannels = channelRepository.findAll();
            System.out.println("삭제 후 전체 채널 목록: " + allChannels);
        } else {
            System.out.println("채널 삭제에 실패했습니다: ID = " + id);
        }
    }
}
