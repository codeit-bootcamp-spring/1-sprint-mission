package com.sprint.mission.discodeit.service.file;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    FileIOHandler fileIOHandlerInstance = FileIOHandler.getInstance();
    FileChannelRepository FileChannelRepositoryInstance = FileChannelRepository.getInstance();

    //생성자 접근 불가능하도록 함.
    private FileChannelService() {
    }
    //LazyHolder 싱글톤
    private static class JCFChannelServiceHolder {
        private static final FileChannelService INSTANCE = new FileChannelService();
    }
    //외부에서 접근 가능한 인스턴스
    public static FileChannelService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return FileChannelService.JCFChannelServiceHolder.INSTANCE;
    }




    //모든 채널 관리 맵 'channelsMap' 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return FileChannelRepositoryInstance.getChannelsMap();
    }

    //해당 채널 리턴
    @Override
    public Channel getChannelById(UUID channelId) {
        if (channelId == null || FileChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return FileChannelRepositoryInstance.getChannel(channelId);
    }

    //채널이름 리턴
    @Override
    public String getChannelNameById(UUID channelId) {
        if (channelId == null || FileChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 이름 확인 실패. 입력값을 확인해주세요.");
            return null;
        }
        return FileChannelRepositoryInstance.getChannel(channelId).getChannelName();
    }

    //채널 생성 후 채널맵에 객체 넣어줌.
    @Override
    public UUID createChannel(String channelName) {
        if (channelName == null){
            System.out.println("채널 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        Channel newChannel = new Channel(channelName);
        FileChannelRepositoryInstance.addChannel(newChannel);
        System.out.println(channelName + "채널 생성 성공!");
        return newChannel.getId();
    }

    //채널 객체를 찾아 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId == null || FileChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileChannelRepositoryInstance.deleteChannel(channelId);
        System.out.println("채널 삭제 성공!");
        return true;
    }

    //채널명 변경
    @Override
    public boolean changeChannelName(UUID channelId, String newName) {
        if (channelId == null || newName == null || FileChannelRepositoryInstance.isChannelExist(channelId)==false){
            System.out.println("채널 이름 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileChannelRepositoryInstance.getChannel(channelId).setChannelName(newName);
        System.out.println("채널 이름 변경 성공!");
        return true;

    }

    //채널에 속한 멤버를 전부 교체.
    @Override
    public boolean changeChannelMembers(UUID channelId, ArrayList<User> members){
        if (FileChannelRepositoryInstance.isChannelExist(channelId)==false || members==null || members.isEmpty()){
            System.out.println("채널 멤버 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileChannelRepositoryInstance.getChannel(channelId).setMembers(members);
        System.out.println("채널 멤버 변경 성공!");
        return true;
    }

    //멤버 하나 추가.
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        if (FileChannelRepositoryInstance.isChannelExist(channelId)==false || memberId==null) {
            System.out.println("채널 멤버 추가 실패. 입력값을 확인해주세요.");
            return false;
        }
        FileChannelRepositoryInstance.getChannel(channelId).addMember(FileUserRepository.getInstance().getUser(memberId));
        System.out.println("채널 멤버 추가 성공!");
        return true;
    }

    //채널 존재여부 확인
    @Override
    public boolean isChannelExist(UUID channelId) {
        if (channelId == null){
            return false;
        }
        return FileChannelRepositoryInstance.isChannelExist(channelId);
    }

    //해당 채널에 소속되어있는 유저 객체들 ArrayList로 반환
    @Override
    public ArrayList<User> getAllMembers(UUID channelId) {
        if (channelId == null || FileChannelRepositoryInstance.isChannelExist(channelId) == false){
            System.out.println("채널 멤버 리스트 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return FileChannelRepositoryInstance.getChannel(channelId).getMembers();
    }

    @Override
    public boolean printAllMemberNames(UUID channelId) {
        if (channelId == null || FileChannelRepositoryInstance.isChannelExist(channelId) == false){
            System.out.println("채널 멤버 출력 실패. 입력값을 확인해주세요.");
            return false;
        }
        System.out.println(JCFChannelRepository.getInstance().getChannel(channelId).getChannelName()+" 채널에 소속된 멤버 : " + FileUserRepository.getInstance().getUsersMap().keySet().stream().map((userId) -> JCFUserService.getInstance().getUserNameById(userId)).collect(Collectors.joining(", ")));
        return true;
    }

    //현재 채널맵에 있는 객체정보 직렬화
    public boolean exportChannelMap(String fileName) {
        if (fileName==null){
            System.out.println("채널 직렬화 실패. 입력값을 확인해주세요.");
            return false;
        }
        fileIOHandlerInstance.serializeHashMap(FileChannelRepositoryInstance.getChannelsMap(), fileName);
        System.out.println("채널 직렬화 성공!");
        return true;
    }


    //역직렬화로 불러온 채널맵을 검증하고 기존의 채널맵에 저장
    //다른 타입의 밸류가 들어있는 해쉬맵이 역직렬화되어 들어왔을 경우를 고려하여 검증코드 추가 예정
    public boolean importChannelMap(String fileName) {
        if (fileName==null){
            System.out.println("채널 불러오기 실패. 입력값을 확인해주세요.");
            return false;
        }
        HashMap<UUID, Channel> importedChannelMap = (HashMap<UUID, Channel>) fileIOHandlerInstance.deserializeHashMap(fileName);
        if (importedChannelMap==null || importedChannelMap.isEmpty()){
            System.out.println("채널 불러오기 실패. 추가할 채널이 존재하지 않습니다.");
            return false;
        }
        FileChannelRepositoryInstance.addChannels(importedChannelMap);
        System.out.println("채널 불러오기 성공!");
        return true;
    }

}
