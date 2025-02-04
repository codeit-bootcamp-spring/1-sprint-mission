package com.sprint.mission.service.file;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.file.FileUserRepository;
import com.sprint.mission.service.UserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class FileUserService implements UserService {

    private static final FileUserRepository fileUserRepository = new FileUserRepository();
    private FileChannelService fileChannelService;

    private static FileUserService fileUserService;
    private FileUserService(){}
    public static FileUserService getInstance(){
        if (fileUserService == null) return fileUserService = new FileUserService();
        else return fileUserService;
    }

    private FileChannelService getFileChannelService(){
        if (fileChannelService == null) return fileChannelService = FileChannelService.getInstance();
        else return fileChannelService;
    }



    @Override
    public User createOrUpdate(User user) throws IOException {
        return fileUserRepository.save(user);
        // 수정-생성 I/O오류 구분하기 위해 여기서 잡지 않고 I/O 예외 던지기
    }

    @Override
    public User update(User updatingUser) {
        try {
            return createOrUpdate(updatingUser);
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 유저 수정 실패");
        }
    }

    @Override
    public User findById(UUID id) {
        return fileUserRepository.findById(id);
    }

    @Override
    public Set<User> findAll() {
        return fileUserRepository.findAll();
    }

    @Override
    public void delete(User user) {
        for (Channel channel : user.getChannelsImmutable()) {
            user.removeChannel(channel);
            try {
                fileChannelService.createOrUpdate(channel);
            } catch (IOException e) {
                throw new RuntimeException("유저 삭제 도중 소속된 채널 삭제 오류");
            }
        }
        fileUserRepository.delete(user);
    }

    /**
     * 검증, 디렉토리 생성
     */
//    @Override  // 중복 허용 결정
//    public void validateDuplicateName(String name) {
//        boolean isDuplicate = findAll().stream()
//                .anyMatch(user -> user.getName().equals(name));
//        if (isDuplicate) {
//            throw new DuplicateName(String.format("%s는 이미 존재하는 닉네임입니다.", name));
//        }
//    }

    // 디렉토리 생성
    public void createUserDirectory() {
        try {
            fileUserRepository.createUserDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
