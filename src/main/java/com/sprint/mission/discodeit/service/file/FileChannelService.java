package com.sprint.mission.discodeit.service.file;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Entity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
    public static FileChannelService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return FileChannelService.JCFChannelServiceHolder.INSTANCE;
    }

    //모든 채널 관리 맵 'channelsMap' 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return FileChannelRepositoryInstance.getChannelsMap();
    }

    //채널 생성. 'channelsMap'에 uuid-채널객체 주소 넣어줌. 성공여부 리턴
    @Override
    public UUID createChannel(String channelName) {
        if (channelName == null){
            return null;
        }
        Channel newChannel = new Channel(channelName);
        FileChannelRepositoryInstance.getChannelsMap().put(newChannel.getId(), newChannel);
        return newChannel.getId();
    }
    //UUID를 통해 채널 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId == null){
            return false;
        }
        FileChannelRepositoryInstance.deleteChannel(channelId);
        return true;
    }

    //채널명 변경. 성공여부 반환
    @Override
    public boolean changeChannelName(UUID channelId, String newName) {
        if (channelId == null || newName == null){
            return false;
        }
        FileChannelRepositoryInstance.getChannel(channelId).setChannelName(newName);
        return true;

    }

    //채널에 속한 멤버를 전부 교체. 성공여부 리턴
    @Override
    public boolean changeChannelMembers(UUID channelId, ArrayList<User> members){
        if (isChannelExist(channelId)==false || members==null || members.isEmpty()){
            return false;
        }
        FileChannelRepositoryInstance.getChannel(channelId).setMembers(members);
        return true;
    }

    //멤버 하나 추가. 성공여부 리턴.
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        if (isChannelExist(channelId)==false || memberId==null) {
            return false;
        }
        FileChannelRepositoryInstance.getChannel(channelId).addMember(JCFUserService.getInstance().getUser(memberId));
        return true;
    }

    //현재 채널맵에 있는 객체정보 직렬화
    public boolean exportChannelMap(String fileName) {
        if (fileName==null){
            return false;
        }
        fileIOHandlerInstance.serializeHashMap(FileChannelRepositoryInstance.getChannelsMap(), fileName);
        return true;
    }

    //channelsMap에 해당 id 존재여부 리턴
    public boolean isChannelExist(UUID channelId) {
        if (channelId == null || FileChannelRepositoryInstance.getChannel(channelId) == null) {
            return false;
        }
        return true;

    }

    //역직렬화로 불러온 채널맵을 검증하고 기존의 채널맵에 저장
    //다른 타입의 value가 역직렬화되어 들어왔을 경우를 고려하여 검증코드 추가 예정
    public boolean importChannelMap(String fileName) {
        if (fileName==null){
            return false;
        }
        HashMap<UUID, Channel> importedChannelMap = (HashMap<UUID, Channel>) fileIOHandlerInstance.deserializeHashMap(fileName);
        if (importedChannelMap==null || importedChannelMap.isEmpty()){
            return false;
        }
        FileChannelRepositoryInstance.addChannels(importedChannelMap);
        return true;
    }




}
