package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;


import java.util.ArrayList;
import java.util.UUID;
import java.util.HashMap;

public class JCFChannelService implements ChannelService {

    FileIOHandler fileIOHandlerInstance = FileIOHandler.getInstance();
    JCFChannelRepository JCFChannelRepositoryInstance = JCFChannelRepository.getInstance();

    //생성자 접근 불가능하도록 함.
    private JCFChannelService() {
    }
    //LazyHolder 싱글톤
    private static class JCFChannelServiceHolder {
        private static final JCFChannelService INSTANCE = new JCFChannelService();
    }
    public static JCFChannelService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return JCFChannelServiceHolder.INSTANCE;
    }
    //모든 채널 관리 맵 'channelsMap' 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return JCFChannelRepositoryInstance.getChannelsMap();
    }



    //채널 생성. 'channelsMap'에 uuid-채널객체 주소 넣어줌. 성공여부 리턴
    @Override
    public UUID createChannel(String channelName) {
        if (channelName == null){
            return null;
        }
        Channel newChannel = new Channel(channelName);
        JCFChannelRepositoryInstance.getChannelsMap().put(newChannel.getId(), newChannel);
        return newChannel.getId();
    }
    //UUID를 통해 채널 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId == null){
            return false;
        }
        JCFChannelRepositoryInstance.deleteChannel(channelId);
        return true;
    }

    //채널명 변경. 성공여부 반환
    @Override
    public boolean changeChannelName(UUID channelId, String newName) {
        if (channelId == null || newName == null){
            return false;
        }
        JCFChannelRepositoryInstance.getChannel(channelId).setChannelName(newName);
        return true;

    }

    //채널에 속한 멤버를 전부 교체. 성공여부 리턴
    @Override
    public boolean changeChannelMembers(UUID channelId, ArrayList<User> members){
        if (JCFChannelRepositoryInstance.isChannelExist(channelId)==false || members==null || members.isEmpty()){
            return false;
        }
        JCFChannelRepositoryInstance.getChannel(channelId).setMembers(members);
        return true;
    }

    //멤버 하나 추가. 성공여부 리턴.
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        if (JCFChannelRepositoryInstance.isChannelExist(channelId)==false || memberId==null) {
            return false;
        }
        JCFChannelRepositoryInstance.getChannel(channelId).addMember(JCFUserRepository.getInstance().getUser(memberId));
        return true;
    }

    @Override
    //채널 존재여부 확인
    public boolean isChannelExist(UUID channelId) {
        if (channelId == null){
            return false;
        }
        return JCFChannelRepositoryInstance.isChannelExist(channelId);
    }

    //현재 채널맵에 있는 객체정보 직렬화
    public boolean exportChannelMap(String fileName) {
        if (fileName==null){
            return false;
        }
        fileIOHandlerInstance.serializeHashMap(JCFChannelRepositoryInstance.getChannelsMap(), fileName);
        return true;
    }


}


