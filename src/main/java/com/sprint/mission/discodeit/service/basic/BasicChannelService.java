package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Type.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    //해당 채널 리턴
    @Override
    public Channel getChannelById(UUID channelId) {
        if (channelId == null || channelRepository.isChannelExist(channelId)==false){
            System.out.println("채널 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return channelRepository.getChannel(channelId);
    }

    //채널이름 리턴
    @Override
    public String getChannelNameById(UUID channelId) {
        if (channelId == null || channelRepository.isChannelExist(channelId)==false){
            System.out.println("채널 이름 확인 실패. 입력값을 확인해주세요.");
            return null;
        }
        return channelRepository.getChannel(channelId).getChannelName();
    }

    //채널 생성
    @Override
    public UUID createChannel(ChannelType type, String channelName, String description) {
        if (channelName == null){
            System.out.println("채널 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        Channel newChannel = new Channel(type, channelName, description);
        channelRepository.addChannel(newChannel);
        System.out.println(channelName + " 채널 생성 성공!");
        return newChannel.getId();
    }

    //채널 객체를 찾아 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId == null || channelRepository.isChannelExist(channelId)==false){
            System.out.println("채널 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        channelRepository.deleteChannel(channelId);
        System.out.println("채널 삭제 성공!");
        return true;
    }

    //채널명 변경
    @Override
    public boolean changeChannelName(UUID channelId, String newName) {
        if (channelId == null || newName == null || channelRepository.isChannelExist(channelId)==false){
            System.out.println("채널 이름 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        channelRepository.getChannel(channelId).setChannelName(newName);
        System.out.println("채널 이름 변경 성공!");
        return true;

    }

    //멤버 한명 추가.
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        if (channelId == null || channelRepository.isChannelExist(channelId)==false || memberId==null || userRepository.isUserExistByUUID(memberId) == false) {
            System.out.println("채널 멤버 추가 실패. 입력값을 확인해주세요.");
            return false;
        }
        channelRepository.addChannelMember(channelId, memberId);
        System.out.println(channelRepository.getChannel(channelId).getChannelName() + " 채널에 "+ userRepository.getUserById(memberId).getUserName() +" 멤버 추가 성공!");
        return true;
    }

    //채널 존재여부 확인
    @Override
    public boolean isChannelExist(UUID channelId) {
        if (channelId == null){
            return false;
        }
        return channelRepository.isChannelExist(channelId);
    }

    //해당 채널에 소속되어있는 유저 객체들 ArrayList로 반환
    @Override
    public ArrayList<UUID> getAllMembers(UUID channelId) {
        if (channelId == null || channelRepository.isChannelExist(channelId) == false){
            System.out.println("채널 멤버 리스트 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return channelRepository.getChannel(channelId).getMembers();
    }

    @Override
    public boolean printAllMemberNames(UUID channelId) {
        if (channelId == null || channelRepository.isChannelExist(channelId) == false){
            System.out.println("채널 멤버 출력 실패. 입력값을 확인해주세요.");
            return false;
        }
        Channel channel = channelRepository.getChannel(channelId);
        System.out.println(channel.getChannelName()+" 채널에 소속된 멤버 : " + channel.getMembers().stream().map((memberId) -> userRepository.getUserById(memberId).getUserName()).collect(Collectors.joining(", ")));
        return true;
    }



}
