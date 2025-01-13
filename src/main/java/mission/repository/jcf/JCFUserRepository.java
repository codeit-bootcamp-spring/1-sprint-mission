package mission.repository.jcf;

import mission.entity.User;
import mission.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();
    private final Set<String> userNames = new HashSet<>();
    // Map<UUID, Map<String, User>> 하면 닉네임 중복 로직에서 더 복잡할 것 같아서 분리

    public User saveUser(User user){
        if (data.containsKey(user.getId())){
            throw new IllegalArgumentException("이미 존재하는 id입니다.");
        } // 중복될일이 없긴 한데....

        if (userNames.contains(user.getName())){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        // 아이디 등록 + userName 목록도 추가
        return saveUserMethod(user);
    }


    public User findById(UUID id){
        return data.get(id);
    }

    public User findByUsername(String username){
        if (!userNames.contains(username)){
            throw new NoSuchElementException("그런 닉네임 없습니다.");
        }

        // 닉네임으로 유저 찾기
        for (User user : data.values()) {
            if (user.getName().equals(username)){
                return user;
            }
        }
    }

    public List<User> findAll(){
        return new ArrayList<>(data.values());
    }

//    public List<User> findUserByTeam(){
//
//    }


    private User saveUserMethod(User user){
        data.put(user.getId(), user);
        userNames.add(user.getName());
        return user;
    }
}
