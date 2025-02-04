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
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {

    FileIOHandler fileIOHandlerInstance = FileIOHandler.getInstance();
    JCFChannelRepository JCFChannelRepositoryInstance = JCFChannelRepository.getInstance();
    JCFUserRepository JCFUserRepositoryInstance = JCFUserRepository.getInstance();

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

    //해당 채널 리턴
    @Override
    public Channel getChannelById(UUID channelId) {
        if (channelId == null || JCFChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return JCFChannelRepositoryInstance.getChannel(channelId);
    }

    //채널이름 리턴
    @Override
    public String getChannelNameById(UUID channelId) {
        if (channelId == null || JCFChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 이름 확인 실패. 입력값을 확인해주세요.");
            return null;
        }
        return JCFChannelRepositoryInstance.getChannel(channelId).getChannelName();
    }

    //채널 생성 후 채널맵에 객체 넣어줌.
    @Override
    public UUID createChannel(String channelName) {
        if (channelName == null){
            System.out.println("채널 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        Channel newChannel = new Channel(channelName);
        JCFChannelRepositoryInstance.addChannel(newChannel);
        System.out.println(channelName + " 채널 생성 성공!");
        return newChannel.getId();
    }
    //UUID를 통해 채널 객체를 찾아 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId == null || JCFChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        JCFChannelRepositoryInstance.deleteChannel(channelId);
        System.out.println("채널 삭제 성공!");
        return true;
    }

    //채널명 변경
    @Override
    public boolean changeChannelName(UUID channelId, String newName) {
        if (channelId == null || newName == null || JCFChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 이름 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        JCFChannelRepositoryInstance.getChannel(channelId).setChannelName(newName);
        System.out.println("채널 이름 변경 성공!");
        return true;

    }

    //채널에 속한 멤버를 전부 교체.
    @Override
    public boolean changeChannelMembers(UUID channelId, ArrayList<User> members){
        if (JCFChannelRepositoryInstance.isChannelExist(channelId)==false || members==null || members.isEmpty()){
            System.out.println("채널 멤버 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        JCFChannelRepositoryInstance.getChannel(channelId).setMembers(members);
        System.out.println("채널 멤버 변경 성공!");
        return true;
    }

    //멤버 하나 추가.
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        if (memberId==null || channelId == null || JCFChannelRepositoryInstance.isChannelExist(channelId)==false || JCFUserRepositoryInstance.isUserExist(memberId) == false) {
            System.out.println("채널 멤버 추가 실패. 입력값을 확인해주세요.");
            return false;
        }
        Channel channel = JCFChannelRepositoryInstance.getChannel(channelId);
        User user = JCFUserRepositoryInstance.getUser(memberId);
        JCFChannelRepositoryInstance.getChannel(channelId).addMember(user);

        System.out.println(channel.getChannelName() + " 채널에 " + user.getUserName() + "멤버 추가 성공!");
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

    //해당 채널에 소속되어있는 유저들 ArrayList로 반환
    @Override
    public ArrayList<User> getAllMembers(UUID channelId) {
        if (channelId == null || JCFChannelRepositoryInstance.isChannelExist(channelId) == false){
            System.out.println("채널 멤버 리스트 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return JCFChannelRepositoryInstance.getChannel(channelId).getMembers();
    }

    //해당 채널에 소속되어있는 유저들 ArrayList로 반환
    @Override
    public boolean printAllMemberNames(UUID channelId) {
        if (channelId == null || JCFChannelRepositoryInstance.isChannelExist(channelId) == false){
            System.out.println("채널 멤버 출력 실패. 입력값을 확인해주세요.");
            return false;
        }
        System.out.println(JCFChannelRepositoryInstance.getChannel(channelId).getChannelName()+" 채널에 소속된 멤버 : " + JCFUserRepositoryInstance.getUsersMap().keySet().stream().map((userId) -> JCFUserService.getInstance().getUserNameById(userId)).collect(Collectors.joining(", ")));
        return true;
    }

    //현재 채널맵에 있는 객체정보 직렬화
    public boolean exportChannelMap(String fileName) {
        if (fileName==null){
            System.out.println("채널 직렬화 실패. 입력값을 확인해주세요.");
            return false;
        }
        fileIOHandlerInstance.serializeHashMap(JCFChannelRepositoryInstance.getChannelsMap(), "Channel\\"+fileName);
        System.out.println("채널 직렬화 성공!");
        return true;
    }


}


