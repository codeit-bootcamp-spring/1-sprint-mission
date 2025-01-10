package discodeit.service.jcf;

import discodeit.entity.User;
import discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFUserService implements UserService {
    private Map<String, User> userData = new HashMap<>();


    @Override
    public User create(User newUser) {
        String userId = newUser.getUserId();
        String userName = newUser.getUserName();
        String phoneNum = newUser.getPhoneNum();
        String email = newUser.getEmail();

        // 예외처리
        if (isUserIdDuplicate(userId)) {
            throw new IllegalArgumentException("[error] 이미 존재하는 user ID입니다.");
        }
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("[error] 유효하지 않은 user 이름입니다.");
        }
        if (isPhoneNumDuplicate(phoneNum)) {
            throw new IllegalArgumentException("[error] 이미 존재하는 전화번호입니다.");
        }
        if (!isValidPhoneNum(phoneNum)) {
            throw new IllegalArgumentException("[error] 유효하지 않은 전화번호 형식입니다. '010-0000-0000' 형식으로 작성해 주세요.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("[error] 유효하지 않은 E-mail 형식입니다.");
        }

        userData.put(userId, newUser);
        return newUser;
    }

    @Override
    public User readById(String userId) {
        if (!userData.containsKey(userId)) {
            throw new RuntimeException("[error] 존재하지 않는 user ID입니다.");
        }
        return userData.get(userId);
    }

    @Override
    public List<User> readAll() {
        return userData.values().stream().toList();
    }

    @Override
    public User update(String userId, User updateUser) {
        if (!userData.containsKey(userId)) {
            throw new RuntimeException("[error] 존재하지 않는 user ID입니다.");
        }
        User originUser = userData.get(userId);

        // 예외처리
        if (isPhoneNumDuplicate(updateUser.getPhoneNum())) {
            throw new IllegalArgumentException("[error] 이미 존재하는 전화번호입니다.");
        }
        if (!isValidPhoneNum(updateUser.getPhoneNum())) {
            throw new IllegalArgumentException("[error] 유효하지 않은 전화번호 형식입니다. '010-0000-0000' 형식으로 작성해 주세요.");
        }
        if(isEmailDuplicate((updateUser.getEmail()))) {
            throw new IllegalArgumentException("[error] 이미 존재하는 E-mail입니다.");
        }
        if (!isValidEmail(updateUser.getEmail())) {
            throw new IllegalArgumentException("[error] 유효하지 않은 E-mail 형식입니다.");
        }

        originUser.updatePhoneNum(updateUser.getPhoneNum());
        originUser.updateEmail(updateUser.getEmail());
        return originUser;
    }

    @Override
    public void delete(String userId) {
        if (!userData.containsKey(userId)) {
            throw new RuntimeException("[error] 존재하지 않는 user ID입니다.");
        }

        userData.remove(userId);
        System.out.println("[삭제 완료]");
    }


    private boolean isUserIdDuplicate(String userId) {
        return userData.containsKey(userId);
    }

    private boolean isPhoneNumDuplicate(String phoneNum) {
        return userData.values().stream().anyMatch(user -> user.getPhoneNum().equals(phoneNum));
    }

    private boolean isEmailDuplicate(String email) {
        return userData.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean isValidPhoneNum(String phoneNum) {
        String phoneNumRegExp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        return phoneNum.matches(phoneNumRegExp);
    }

    private boolean isValidEmail(String email) {
        String emailRegExp = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
        return email.matches(emailRegExp);
    }
}
