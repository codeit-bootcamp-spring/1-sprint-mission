package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicUserService implements UserService {
    private UserRepository userRepository;
    private ChannelService channelService;
    private MessageService messageService;

    @Autowired
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setService(ChannelService channelService, MessageService messageService) {
        this.channelService = channelService;
        this.messageService = messageService;
    }

    @Override
    public User createUser(String name, String email,String password) {
        if (iscorrectName(name) && iscorrectEmail(email)) {
            User newUser = new User(name, email, password);
            userRepository.save(newUser);
            System.out.println("환영합니다! " + newUser.getUserName() + "님 반갑습니다.");
            return newUser;
        }
        return null;
    }

    @Override
    public void updateUserName(UUID userId, String newName) {
        try {
            User user = searchById(userId);
            String name = user.getUserName();
            user.setUserName(newName);
            userRepository.save(user);
            System.out.println(name + "님의 이름이" + newName + "으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다.");
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserEmail(UUID userId, String email) {
        try {
            if(iscorrectEmail(email)) {
                System.out.println("이미 존재하는 이메일 입니다.");
                return;
            }
            User user = searchById(userId);
            user.setUserEmail(email);
            userRepository.save(user);
            System.out.println(user.getUserName() + "님의 이메일이" + email + "로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다.");
            e.printStackTrace();
        }


    }

    @Override
    public List<User> getAllUserList() {
        return userRepository.findAll().values().stream().collect(Collectors.toList());
    }

    @Override
    public User searchById(UUID userId) {
        User user = userRepository.findById(userId);
        if(user == null) {
            System.out.println("사용자가 존재하지 않습니다.");
        }
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = searchById(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
        } else  {
            userRepository.delete(userId);
            System.out.println("사용자가 탈퇴되었습니다.");
        }

    }
    private boolean iscorrectName(String name) {
        if (name.isBlank()) {
            System.out.println("이름을 입력해주세요");
        } else if (name.length() < 2) {
            System.out.println("이름은 두글자 이상 입력해주세요");
        } else {
            return true;
        }
        return false;
    }
    private boolean iscorrectEmail(String email) {
        String emailFormat = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailFormat)) {
            System.out.println("이메일 형식이 올바르지 않습니다.");
        }
        return email.matches(emailFormat);
    }
}