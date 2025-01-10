package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    // DB 대체 User data
    // Map<email, user>
    private final Map<UUID, User> data = new HashMap<>();

    private static UserService userService;

    private JCFUserService(){}

    public static UserService getJCFUserService(){
        if (userService == null){
            userService = new JCFUserService();
        }

        return userService;
    }


    // User 생성(가입)
    @Override
    public void craete(String email, String pw, String name, String nickname, String phoneNumber) {
        
        if (checkEmail(email, data)){
            System.out.println("이미 가입된 이메일입니다.");
        }

        // 유효성 검사
        if (matchEmail(email)) {
            System.out.println("올바르지 않은 형식의 이메일입니다.\n");
            return;
        }
        if (pw.length() < 8) {
            System.out.println("비밀번호는 8자리 이상이어야 합니다.\n");
            return;
        }
        if (matchPhoneNumber(phoneNumber)) {
            System.out.println("올바르지 않은 형식의 전화번호입니다. " +
                    "총 11자리의 전화번호를 '-' 포함 또는 띄어쓰기 없이 입력해주세요.\n");
            return;
        }
        if(name.isBlank() || nickname.isBlank()){
            System.out.println("빈값을 넣을 수 없습니다.");
            return;
        }

        User newUser = new User(email, pw, name, nickname, phoneNumber);

        data.put(newUser.getId(), newUser);
        System.out.println("[ 가입 완료 ]\n");
    }


    // 데이터 읽기
    // id 같은 사람 읽기
    @Override
    public User read(UUID id) {
        return data.get(id);
    }

    // 모든 데이터 읽어오기
    @Override
    public List<User> allRead() {
        return new ArrayList<>(data.values());
    }


    // 데이터 수정
    // 이메일 수정
    @Override
    public void updateEmail(UUID id, String updateEmail) {
        if (!checkEmail(userService.read(id).getEmail(), data)){
            System.out.println("미가입된 계정입니다.");
            return;
        }

        if (userService.read(id).getEmail().equals(updateEmail)) {
            System.out.println("현재 이메일과 수정하고자 하는 이메일이 동일합니다.\n");
            return;
        }

        if (matchEmail(updateEmail)) {
            System.out.println("올바르지 않은 이메일 형식입니다.\n");
            return;
        }

        User updateUser = data.get(userService.read(id).getId());
        updateUser.updateEmail(updateEmail);
    }

    // 비밀번호 수정
    @Override
    public void updatePw(UUID id, String updatePw) {
        if (updatePw.length() < 8) {
            System.out.println("비밀번호는 8자리 이상이어야 합니다.\n");

            return;
        }

        User updateUser = data.get(id);
        updateUser.updatePw(updatePw);
    }

    // 이름 수정
    @Override
    public void updateName(UUID id, String updateName) {

        if (updateName.isBlank()){
            System.out.println("빈값을 넣을 수 없습니다.\n");
            return;
        }

        User updateUser = data.get(id);
        updateUser.updateName(updateName);
    }

    // 닉네임 수정
    @Override
    public void updateNickname(UUID id, String updateNickname) {

        if (updateNickname.isBlank()){
            System.out.println("빈값을 넣을 수 없습니다.\n");
            return;
        }

        User updateUser = data.get(id);
        updateUser.updateNickname(updateNickname);
    }

    // 전화번호 수정
    @Override
    public void updatePhoneNumber(UUID id, String updatePhoneNumber) {

        if (matchPhoneNumber(updatePhoneNumber)) {
            System.out.println("올바르지 않은 형식의 전화번호입니다. " +
                    "총 11자리의 전화번호를 '-' 포함 또는 띄어쓰기 없이 입력해주세요.\n");
            return;
        }

        User updateUser = data.get(id);
        updateUser.updatePhoneNumber(updatePhoneNumber);
    }


    // 유저 삭제 (회원 탈퇴)
    @Override
    public void delete(UUID id) {
        try {
            data.remove(id);

        } catch (Exception e) {
            System.out.println("[ 유저 삭제 실패 ]");
        }
    }

    // 이메일 유효성 검사
    public static boolean matchEmail(String email) {
        // 이메일 정규식
        String EMAIL_REGEX =
                "^[a-zA-Z0-9_+&*-]+(?:\\." +
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        
        // 이메일 정규식 충족하지 않을 때 true 리턴
        return !email.matches(EMAIL_REGEX);
    }

    // 전화번호 유효성 검사
    public static boolean matchPhoneNumber(String phoneNumber) {
        // 전화번호 정규식
        String PHONENUMBER_REGEX1 = "\\d{3}-\\d{4}-\\d{4}$";
        String PHONENUMBER_REGEX2 = "\\d{10,11}$";

        // 정규식 둘 다 충족하지 못할 때 true 리턴
        return !phoneNumber.matches(PHONENUMBER_REGEX1)
                && !phoneNumber.matches(PHONENUMBER_REGEX2);
    }

    // 이메일 존재 여부 확인
    public static boolean checkEmail(String email, Map<UUID, User> data){
        Set<UUID> keySet = data.keySet();

        for (UUID key : keySet) {
            // 가입된 경우 true 반환
            if (data.get(key).getEmail().equals(email)) {
                return true;
            }
        }

        // 미가입된 경우 false 반환
        return false;
    }
}
