package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 직렬화 시 NotSerializableException 발생하는 문제 해결하기 위해 transient 키워드 적용
    private transient UserRepository userRepository;

    // 이메일
    private String email;

    // 이메일 정규식
    public static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

    // 생성자
    public Email(String email, UserRepository userRepository){

        this.userRepository = userRepository;

        email = checkNull(email);
        emailValidation(email);
        this.email = email;
    }

    // 이메일 수정
    public void updateEmail(String email) {

        email = checkNull(email);
        emailValidation(email);
        this.email = email;
    }

    // 이메일 유효성 검사
    public void emailValidation(String email) {

        if (matchEmail(email)){
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        } else if(checkEmail(email, userRepository.load())){
            throw new IllegalArgumentException("이미 가입된 계정입니다.");
        }
    }

    // 이메일이 null인지 확인하고 아니라면 trim()하여 반환
    public String checkNull(String email) {
        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }

        return email.trim();
    }

    // 이메일 형식 확인
    public boolean matchEmail(String email) {
        
        // 이메일 정규식 충족하지 않을 때 true 리턴
        return !email.matches(EMAIL_REGEX);
    }

    // 가입된 이메일인지 확인
    public boolean checkEmail(String email, Map<UUID, User> users) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                // 가입된 경우 true 반환
                return true;
            }
        }
        
        // 미가입된 경우 false 반환
        return false;
    }

    @Override
    public String toString(){
        return email;
    }

    // transient 키워드 적용으로 역직렬화 시 userRepository가 null로 초기화되는 문제 해결위해 작성
    // readObject()는 역직렬화 시 자동으로 호출되는 메서드
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // 역직렬화 실행
        ois.defaultReadObject();

        // 역직렬화 실행 후 수행되는 로직
        // JCFUserRepository를 사용할 때는 쓰이지 않는 메서드이기 때문에 FileUserRepository 대입
        this.userRepository = new FileUserRepository();
    }
}
