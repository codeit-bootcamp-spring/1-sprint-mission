package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Type.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
    private final MessageRepository messageRepository;

    //프라이빗 타입 채널 생성.
    @Override
    public UUID createPrivateChannel(String channelName, String description, UUID ...memberId) {
        if (channelName == null || description == null || memberId == null || memberId.length == 0) {
            System.out.println("채널 생성 실패. 입력값이 null이거나 추가할 멤버를 입력하지 않았습니다.");
            return null;
        }
        // todo File레포지토리들 로드할때 실패하면 예외를 던지기때문에 람다를 쓸수가없다. 람다안에서 try catch를 써야 사용 가능한데, 그럼 코드가 지저분해진다. 레포지토리 로드 방식을 바꾸거나 해야할듯.

        try {
            //실제로 유저가 존재하는지 검증
            for (UUID userId : memberId) {
                if (userRepository.isUserExistByUUID(userId)==false) {
                    throw new NoSuchElementException("해당 uuid를 가진 유저가 존재하지 않습니다.");
                };
            }
            Channel newChannel = new Channel(ChannelType.PRIVATE, channelName, description);
            LinkedHashMap<UUID, Message> channelMessageMap = new LinkedHashMap<UUID, Message>();
            messageRepository.addChannelMessagesMap(newChannel.getId(), channelMessageMap);
            //채널 생성과 동시에 channelReadStatusMap 생성. 유저마다 readStatus 객체 생성해서 channelReadStatusMap에 삽입.
            HashMap<UUID, ReadStatus> channelReadStatusMap = new HashMap<UUID, ReadStatus>();
            Stream.of(memberId).forEach(_memberId -> channelReadStatusMap.put(_memberId, new ReadStatus()));
            //레포지토리에 저장.
            readStatusRepository.addChannelReadStatusMap(newChannel.getId(), channelReadStatusMap);
            newChannel.setMembers(new ArrayList<UUID>(List.of(memberId)));
            channelRepository.saveChannel(newChannel);
            System.out.println(channelName + " 채널 생성 성공!");
            return newChannel.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("채널 생성 실패");
            return null;
        }
    }

    //퍼블릭 타입 채널 생성
    //public은 채널 생성시 멤버 아무도 안들어가있음.
    @Override
    public UUID createPublicChannel(String channelName, String description) {
        if (channelName == null || description == null) {
            System.out.println("채널 생성 실패. 입력값이 null인 상태입니다. ");
            return null;
        }
        try {
            Channel newChannel = new Channel(ChannelType.PRIVATE, channelName, description);
            channelRepository.saveChannel(newChannel);
            LinkedHashMap<UUID, Message> channelMessageMap = new LinkedHashMap<UUID, Message>();
            messageRepository.addChannelMessagesMap(newChannel.getId(), channelMessageMap);
            System.out.println(channelName + " 채널 생성 성공!");
            return newChannel.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("채널 생성 실패");
            return null;
        }
    }

    //해당 채널 리턴
    @Override
    public ChannelDto findChannelById(UUID channelId) {
        if (channelId == null){
            System.out.println("채널 반환 실패. 입력값이 null인 상태입니다. ");
            return null;
        }
        try {
            Channel channel = channelRepository.getChannel(channelId);
            return ChannelDto.from(channel, messageRepository.getLastMessageCreatedAt(channelId));
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("채널 반환 실패.");
            return null;
        }
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        try {
            HashMap<UUID, Channel> channelsMap = channelRepository.getChannelsMap();
            if (channelsMap.isEmpty()==true){
                System.out.println("채널이 존재하지 않습니다.");
                return null;
            }
            List<Channel> channelsCanShowUser = channelsMap.values().stream().filter(channel -> channel.getMembers().contains(userId) || channel.getType().equals(ChannelType.PUBLIC)).collect(Collectors.toList());
            return channelsCanShowUser.stream().map(
                    channel-> ChannelDto.from(channel, messageRepository.getLastMessageCreatedAt(channel.getId()))).collect(Collectors.toList());

        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("모든 채널 반환 실패.");
            return null;
        }
    }

    //채널이름 리턴
    @Override
    public String getChannelNameById(UUID channelId) {
        if (channelId == null){
            System.out.println("채널 이름 확인 실패. 입력값이 null인 상태입니다.");
            return null;
        }
        try {
            return findChannelById(channelId).channelName();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //채널 객체를 찾아 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId == null){
            System.out.println("채널 삭제 실패. 입력값이 null인 상태입니다.");
            return false;
        }
        try {
            messageRepository.deleteChannelMessagesMap(channelId);

            readStatusRepository.deleteChannelReadStatusMap(channelId);
            channelRepository.deleteChannel(channelId);
            System.out.println("채널 삭제 성공!");
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("채널 삭제 실패");
            return false;
        }
    }

    //채널명 변경
    @Override
    public boolean changeChannelDescription(UUID channelId, String newDescription) {
        if (channelId == null || newDescription == null){
            System.out.println("채널 설명 변경 실패. 입력값이 null인 상태입니다.");
            return false;
        }
        try {
            Channel channel = channelRepository.getChannel(channelId);
            if (channel.getType().equals(ChannelType.PRIVATE)){
                System.out.println("Private 채널은 수정 불가합니다.");
                return false;
            }
            channel.setDescription(newDescription);
            System.out.println("채널 설명 변경 성공!");
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("채널 설명 변경 실패");
            return false;
        }
    }

    //멤버 한명 추가.
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        if (channelId == null || memberId == null) {
            System.out.println("채널 멤버 추가 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            channelRepository.addChannelMember(channelId, memberId);
            System.out.println(channelRepository.getChannel(channelId).getChannelName() + " 채널에 " + userRepository.getUserById(memberId).getUserName() + " 멤버 추가 성공!");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("채널 멤버 추가 실패!");
            return false;
        }
    }

    //채널 멤버들 이름 출력
    @Override
    public boolean printAllMemberNames (UUID channelId){
        if (channelId == null){
            System.out.println("채널 멤버 이름 출력 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            ChannelDto channel = findChannelById(channelId);
            System.out.println(channel.channelName() + " 채널에 소속된 멤버 : " + channel.members().stream().map((memberId) -> {
                try {
                    return userService.findUserById(memberId).userName();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(", ")));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("채널 멤버 이름 출력 실패");
            return false;
        }
    }
}


