package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.entity.UserStatus;
import com.sprint.misson.discordeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    final HashMap<UUID, User> data;


    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public boolean createUser(String nickname, String email) {

        //스트림으로 HashMap 의 value(User) 들 중 이미 존재하는 email 인지 검사
        boolean userEmailExists = data.values().stream()
                .anyMatch( u -> u.getEmail().equals(email) );

        if(userEmailExists) {
            return false;
        }

        User newUser = new User(nickname, email, UserStatus.ACTIVE, null, AccountStatus.UNVERIFIED );

        //TODO - 이메일 인증 추가 구현
        //1. 이메일 발송
        //2. 사용자가 이메일에서 인증 버튼 클릭 or 인증 코드 입력
        //3. 인증 완료 시, user.accountStatus 를 VERIFIED 로 변경

        //* put() - 이미 해당 key 에 key-value 셋이 존재했다면 이전 value 반환(value 가 null 이었으면 null 반환)
        //          해당하는 key-value 셋이 없었다면 null 반환
        data.put( newUser.getId(), newUser );

        return true;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User getUserByUUID(String userId) {
        //* get() - key 에 해당하는 값이 없어도 null 반환, 에러 x
        return data.get( UUID.fromString(userId) );
    }

    @Override
    public User getUserByEmail(String email) {
        //스트림으로 해당 email 을 가진 User 객체 찾아서 반환
        return data.values().stream()
                .filter( u -> u.getEmail().equals( email ) )
                .findFirst().orElse(null);
    }

    @Override
    public List<User> getUsersByNickname(String nickname) {
        return data.values().stream()
                .filter( u -> u.getNickname().contains( nickname ))
                .toList();
    }

    @Override
    public List<User> getUsersByAccountStatus(AccountStatus accountStatus) {
        return data.values().stream()
                .filter( u -> u.getAccountStatus().equals( accountStatus ))
                .toList();
    }

    @Override
    public boolean updateUser(User user) {

        // todo - 질문
        // 메인에서 update 함수로 객체 필드 수정 시,
        // HashMap 에 저장된 해당 user 객체도 업데이트 되지 않나?
        // -> 메세지를 다른 메세지로 대체할 수는 없지 않나?
        // -> @transactional 없이 트랜젝션처럼 구현해야하나?

        if(user == null) {
            return false;
        }
        data.put( user.getId(), user );
        return true;

        // 시나리오
        // getUser 로 User 객체를 조회
        // 1) 존재할 경우 data.get()을 통해 user 객체를 가져옴
        // -> main 에서 특정 필드들 수정
        // -> Service 에서 data 에 업데이트

        // 2) 존재하지 않을 경우, null 이므로 업데이트 불가능
        // -> false 반환

        // 주의: HashMap 은 put 할 때 key 가 null 이어도 저장된다.



    }

    @Override
    public boolean deleteUser(User user) {
        //* remove() - key 에 해당하는 값이 없어도 null 반환, 에러 x
        return data.remove(user.getId()) != null;
    }
}
