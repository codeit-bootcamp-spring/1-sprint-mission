package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class FileUserService implements UserService {
    private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
    private static final String USER_FILE = "user.ser";
    private MessageService messageService;
    private ChannelService channelService;
    private FileStorage<User> fileStorage;

    public FileUserService(MessageService messageService, ChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
        this.fileStorage = new SerializableFileStorage<>(User.class);
        fileStorage.init(ROOT_DIR);
    }

    @Override
    public void setDependencies(MessageService messageService, ChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    @Override
    public User createUser(User user) {
        List<User> users = readAllUsers();
        if(users.stream().anyMatch(u->u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("User Id is already exists: " + user.getId());
        }
        users.add(user);
        fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);
        System.out.println(user);
        return user;
    }

    @Override
    public Optional<User> readUser(User user) {
        return readAllUsers().stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst();
    }

    @Override
    public List<User> readAllUsers() {
        return fileStorage.load(ROOT_DIR);
    }

    @Override
    public User updateUser(User existUser, User updateUser) {
        List<User> users = readAllUsers();
        Optional<User> userToUpdate = users.stream()
                .filter(u -> u.getId().equals(existUser.getId()))
                .findFirst();

        if (userToUpdate.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        User user = userToUpdate.get();
        System.out.println("기존 유저= " + user);
        user.updateTime();
        user.updateUserid(updateUser.getUserid());
        user.updateUsername(updateUser.getUsername());
        user.updatePassword(updateUser.getPassword());
        user.updateUserEmail(updateUser.getEmail());

        fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);
        System.out.println("수정 유저= " + user);
        return user;
    }

    @Override
    public boolean deleteUser(User user) {
        List<User> users = readAllUsers();
        boolean removed = users.removeIf(u -> u.getId().equals(user.getId()));

        if(!removed) {
            return false;
        }

        System.out.println(user);

        // 사용자 삭제 시 관련 메시지 삭제
        messageService.deleteMessageByUser(user);

        // 사용자 삭제 시 모든 채널에서 해당 사용자 삭제
        channelService.readAllChannels().forEach(channel -> {
            if (channel.getParticipants().contains(user)) {
                channel.getParticipants().remove(user);
                System.out.println("채널 '" + channel.getName() + "'에서 사용자" +
                        "'" + user.getUsername() + "' 제거 완료");
            }
        });

        fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);
        return true;
    }
}
