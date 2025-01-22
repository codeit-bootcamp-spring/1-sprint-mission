package discodeit.service.jcf;

import discodeit.enity.User;
import discodeit.service.UserService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    //싱글톤
    private static volatile JCFUserService instance;
    private final Map<UUID, User> data;   // 모든 유저 데이터, key=id
    private JCFChannelService jcfChannelService;
    private JCFMessageService jcfMessageService;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    public void setService() {
        this.jcfMessageService = jcfMessageService.getInstance();
        this.jcfChannelService = jcfChannelService.getInstance();
    }

    public Map<UUID, User> getData() {
        return new HashMap<>(data);
    }

    @Override
    public UUID createUser(String email, String name) {
        System.out.print("사용자 생성 요청: ");
        if (data.entrySet().stream()
                .anyMatch(entry -> entry.getValue().getEmail().equals(email))) {
            System.out.println("이미 존재하는 계정입니다.");
            return null;
        }
        User user = new User(name, email);
        data.put(user.getId(), user);
        System.out.println(name + "(" + email + ") 사용자가 등록되었습니다.");
        return user.getId();
    }

    @Override
    public void viewUserInfo(UUID userId) {
        System.out.println("--- 사용자 조회 ---");
        if (validateUser(userId)) {
            User user = data.get(userId);
            System.out.println("Email: " + user.getEmail()
                    + " / name: " + user.getName()
                    + " / created at: " + user.getCreatedAt());
            System.out.print("소속 채널: ");
            user.getChannels().stream()
                    .forEach(channel -> {
                        System.out.print(channel.getName() + " ");
                    });
            System.out.println();
        } else {
            System.out.println("존재하지 않는 사용자입니다.");
        }
    }

    @Override
    public void viewAllUser() {
        System.out.println("--- 전체 사용자 목록---");
        if (data.isEmpty()) {
            System.out.println("사용자가 없습니다.");
            return;
        }
        data.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> entry.getValue().getCreatedAt())
                )
                .forEach(entry -> {
                    System.out.println("Email: " + entry.getValue().getEmail()
                            + " / name: " + entry.getValue().getName()
                            + " / created at: " + entry.getValue().getCreatedAt());
                    System.out.println("소속 채널: " + entry.getValue().getChannels());
                });
    }

    @Override
    public void updateUserName(UUID userId, String name) {
        if (validateUser(userId)) {
            User user = data.get(userId);
            String prevName = user.getName();
            user.updateName(name);
            System.out.println(prevName + "님의 이름이 " + name + "으로 변경되었습니다.");
        } else {
            System.out.println("존재하지 않는 사용자입니다.");
        }
    }

    @Override
    public void updateUserEmail(UUID userId, String email) {
        System.out.print("사용자 수정 요청: ");
        if (validateUser(userId)) {
            if (data.entrySet().stream()
                    .anyMatch(entry -> entry.getValue().getEmail().equals(email))) {
                System.out.println(email + "은 이미 존재하는 이메일입니다.");
                return;
            }
            User user = data.get(userId);
            user.updateEmail(email);
            System.out.println(user.getName() + "의 이메일이 " + email + "로 변경되었습니다.");
        } else {
            System.out.println("존재하지 않는 사용자입니다.");
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        System.out.print("사용자 삭제 요청: ");
        if (validateUser(userId)) {
            // 해당 유저가 속한 모든 채널에서 삭제
            User user = data.get(userId);
            user.getChannels().stream()
                    .forEach(channel -> {
                        channel.getUsers().remove(user);
                    });
            data.remove(user.getId());
            System.out.println(user.getName() + " 사용자가 삭제되었습니다.");
        } else {
            System.out.println("존재하지 않는 사용자입니다.");
        }

    }

    @Override
    public boolean validateUser(UUID userId) {
        if (data.containsKey(userId) && data.get(userId) != null) return true;
        return false;
    }
}
