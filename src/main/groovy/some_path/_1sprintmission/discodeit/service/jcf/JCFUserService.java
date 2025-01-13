package some_path._1sprintmission.discodeit.service.jcf;


import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;
public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();
    Random random = new Random();


    @Override
    public void createUser(User user) {
        int discriminator = makeDiscriminator(); // 고유번호 생성
        user.setDiscriminator(discriminator);    // 사용자에 고유번호 설정
        user.setVerified(false);
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        Optional<User> user = Optional.ofNullable(data.get(id));
        user.ifPresent(System.out::println); // User 객체의 내용을 출력
        return user;
    }

    @Override
    public List<User> getUserAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, User updatedUser) {
        if (data.containsKey(id)) {
            data.put(id, updatedUser);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }


    @Override
    public int makeDiscriminator(){
        int sureNumber = 0;
        while(Boolean.TRUE){
            //난수생성
            int notSureNumber = random.nextInt(10000);
            boolean isDuplicate = getUserAll().stream()
                    .anyMatch(user -> Objects.equals(user.getDiscriminator(), notSureNumber));
            if (!isDuplicate) {
                sureNumber = notSureNumber;
                break;
            }
        }
        return sureNumber;
    };
}