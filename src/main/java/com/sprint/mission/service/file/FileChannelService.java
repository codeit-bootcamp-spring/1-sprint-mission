package com.sprint.mission.service.file;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.file.FileChannelRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.exception.DuplicateName;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {


    private static final FileChannelRepository fileChannelRepository = new FileChannelRepository();
    private FileUserService fileUserService;

    private static FileChannelService fileChannelService;
    FileChannelService(){}
    public static FileChannelService getInstance(){
        if (fileChannelService == null) return fileChannelService = new FileChannelService();
        else return fileChannelService;
    }

    private FileUserService getFileUserService(){
        if (fileUserService == null) return fileUserService = FileUserService.getInstance();
        else return fileUserService;
    }

    // 생성 수정 오류 메시지 다르게 처리하기위해 오류 던지기
    @Override
    public Channel createOrUpdate(Channel channel) throws IOException {
        //validateDuplicateName(channel.getName());
        return fileChannelRepository.create(channel);
    }

    @Override
    public Channel update(Channel updatingChannel, String newName) {
        validateDuplicateName(newName);
        try {
            updatingChannel.setName(newName);
            return createOrUpdate(updatingChannel);
        } catch (Exception e){
            throw new RuntimeException("채널 이름 수정 실패" + e.getMessage());
        }
    }

    @Override
    public Set<Channel> findAll() {
        try {
            return fileChannelRepository.findAll();
        } catch (IOException e) {
            System.out.println("I/O 오류 : 채널 불러오기 실패");
            return null;
        }
    }

    @Override
    public Channel findById(UUID id) {
        try {
            return fileChannelRepository.findById(id);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 파일(id) 찾는데 오류 발생했습니다" + e.getMessage());
        }
    }

    @Override
    public void delete(Channel deletingChannel){
        for (User user : deletingChannel.getUsersImmutable()) {
            deletingChannel.removeUser(user);
            try {
                fileUserService.createOrUpdate(user);  // 유저 파일 업데이트
            } catch (IOException e) {
                throw new RuntimeException("채널 삭제 도중 소속된 user 삭제 실패");
            }
        }
        fileChannelRepository.delete(deletingChannel);
    }

    /**
     * 검증
     */
    public void validateDuplicateName(String validatingName){
        // 맵 방식 적용
        Map<String, Channel> channelMap = findAll().stream().collect(Collectors.toMap(
                Channel::getName,
                Function.identity()
        ));

        if (!(channelMap.get(validatingName) == null)){
            throw new DuplicateName("채널명 중복");
        }
        //System.out.println("중복되지 않은 채널 이름입니다");
    }

    /**
     * 세팅
     */

    public void createChannelDirect(){
        try {
            fileChannelRepository.createChannelDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
