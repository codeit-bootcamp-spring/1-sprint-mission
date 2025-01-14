package mission.service.file;

import mission.entity.User;
import mission.repository.file.FileUserRepository;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileUserService {

    private final FileUserRepository fileUserRepository = new FileUserRepository();

    // 생성
    public void createUserDirectory(){
        try {
            fileUserRepository.createUserDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User create(User user){
        // 검증을 더 앞에서 하자 validateDuplicateName
        try {
            return fileUserRepository.saveUser(user);
        } catch (IOException e) {
            throw new RuntimeException("생성 실패");
        }
    }

    // username, pw 변경 => 메인에서 id 있는지 확인한 뒤 네임, pw를 여기에 넘겨주는
    public User update(User updatingUser) throws IOException {
        // userName, pw 검증은 main 서비스에서
        try {
            // 새로운 닉네임 중복 검증
            validateDuplicateName(updatingUser.getName());
            // oldName을 갖고 잇음 (수정 전)
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("%S는 이미 존재하는 닉네임입니다", updatingUser.getName()));
        }

        return fileUserRepository.updateUserNamePW(updatingUser);
    }

    public User findById(UUID id){
        try {
            return fileUserRepository.findUserById(id);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 오류" + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("입력한 id에 해당하는 파일이 없습니다");
        }
    }

    public List<User> findAll() {
        try {
            return fileUserRepository.findAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(User user){
        try {
            fileUserRepository.delete(user);
        } catch (NoSuchFileException e) {
            throw new RuntimeException(String.format(
                    "[%s] %s의 파일은 존재하지 않습니다", user.getFirstId(), user.getName()));
        } catch (IOException e) {
            System.out.println("User 파일 삭제 중 오류 발생");
            e.printStackTrace();
        }
    }

    // 검증은 레포지토리까지 갈 필요도 없이 여기서 처리
    public void validateDuplicateName(String name) throws IOException {
        List<User> users = findAll();
        // List -> Map으로 바꿔서 조회 빠르게 (이전에 다른 강의에서 봤던건데, 크게 효과있는지는 모르겠다 - map변환과정이 짧나?)
        Map<String, User> userListMap = users.stream().collect(
                Collectors.toMap(User::getName, Function.identity()));
        if (userListMap.get(name) != null){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }
}
