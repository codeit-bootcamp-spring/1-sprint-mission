package mission.repository.jcf;

import mission.entity.User;
import mission.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();
    private final Set<String> userNames = new HashSet<>();
    // Map<UUID, Map<String, User>> 하면 닉네임 중복 로직에서 더 복잡할 것 같아서 분리

    public User saveUser(User user){
        return saveUserMethod(user);
    }

    public User findById(UUID id){
        return data.get(id);
    }

    @Override
    public User findByNamePW(String name, String password) {
        try {
            validationisUsername(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (User user : data.values()) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                return user;
            }
        }
        // user password 일치하는게 없으면
        return null;
    }


    public User findByUsername(String username){

        validationisUsername(username);

        // 닉네임으로 유저 찾기
        for (User user : data.values()) {
            if (user.getName().equals(username)){
                return user;
            }
        }
        return null;
    }

    public List<User> findAll(){
        return new ArrayList<>(data.values());
    }


    private void validationisUsername(String username) {
        // 빠르게 찾기 위해 셋 자료구조로 중복 확인
        if (!userNames.contains(username)){
            throw new NoSuchElementException("그런 닉네임 없습니다.");
        }
    }


//    public List<User> findUserByTeam(){
//
//    }


    private User saveUserMethod(User user){
        data.put(user.getId(), user);
        userNames.add(user.getName());
        return user;
    }

    @Override
    public void validateDuplicateUserName(String userName){
        if (userNames.contains(userName)){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
        }
    }
}
