package discodeit.service.jcf;

import discodeit.Validator.UserValidator;
import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final UserValidator validator;
    private final List<User> users;
    private ChannelService jcfChannelService;
    private MessageService jcfMessageService;;

    private JCFUserService() {
        validator = new UserValidator();
        users = new ArrayList<>();
    }

    private static class JCFUserServiceHolder {
        private static final UserService INSTANCE = new JCFUserService();
    }

    public static UserService getInstance() {
        return JCFUserServiceHolder.INSTANCE;
    }

    @Override
    public void updateChannelService(ChannelService jcfChannelService) {
        this.jcfChannelService = jcfChannelService;
    }

    @Override
    public void updateMessageService(MessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }

    @Override
    public User createUser(String name, String email, String phoneNumber, String password) {
        validator.validate(name, email, phoneNumber);
        isDuplicateEmail(email);
        isDuplicatePhoneNumber(phoneNumber);
        User newUser = new User(name, email, phoneNumber, password);
        users.add(newUser);
        return newUser;
    }

    @Override
    public User findById(UUID id) {
        User findUser = findUser(id);
        if (findUser == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        return findUser;
    }

    @Override
    public User findUser(UUID id) {
        return users.stream()
                .filter(user -> user.isIdEqualTo(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public String getInfo(User user) {
        return user.toString();
    }

    @Override
    public void updateName(User user, String name) {
        validator.validateName(name);
        user.updateName(name);
        user.updateUpdatedAt();
    }

    @Override
    public void updateEmail(User user, String email) {
        validator.validateEmail(email);
        isDuplicateEmail(email);
        user.updateEmail(email);
        user.updateUpdatedAt();
    }

    @Override
    public void updatePhoneNumber(User user, String phoneNumber) {
        validator.validatePhoneNumber(phoneNumber);
        isDuplicatePhoneNumber(phoneNumber);
        user.updatePhoneNumber(phoneNumber);
        user.updateUpdatedAt();
    }

    @Override
    public void updatePassword(User user, String originalPassword, String newPassword) {
        user.updatePassword(originalPassword, newPassword);
        user.updateUpdatedAt();
    }

    @Override
    public void updateJoinedChannels(User user, Channel channel) {
        user.updateJoinedChannels(channel);
        user.updateUpdatedAt();
    }

    @Override
    public void deleteUser(User user) {
        // 가입된 채널인데 참여자 목록에 없으면 Owner -> 채널 자체를 삭제
        List<Channel> joinedChannels = user.getJoinedChannels();
        while (joinedChannels.size() != 0) {
            try {
                jcfChannelService.deleteParticipant(joinedChannels.get(0), user);
            } catch (IllegalArgumentException e) {
                jcfChannelService.deleteChannel(joinedChannels.get(0), user);
            }
        }
        users.remove(user);
        user.withdraw();
    }

    @Override
    public void deleteJoinedChannel(User user, Channel channel) {
        user.deleteJoinedChannel(channel);
        channel.deleteParticipant(user);
        user.updateUpdatedAt();
        channel.updateUpdatedAt();
    }

    @Override
    public void isDuplicateEmail(String email) {
        users.stream().forEach(user -> user.isDuplicateEmail(email));
    }

    @Override
    public void isDuplicatePhoneNumber(String phoneNumber) {
        users.stream().forEach(user -> user.isDuplicatePhoneNumber(phoneNumber));
    }
}
