package mission.service.file;

import mission.entity.User;
import mission.repository.file.FileUserRepository;
import mission.service.UserService;
import mission.service.exception.DuplicateName;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUserService implements UserService {

    private final FileUserRepository fileUserRepository = new FileUserRepository();

    @Override
    public User create(User user) throws IOException {
        return fileUserRepository.saveUser(user);
    }

    @Override
    public User update(User updatingUser) {
        try {
            return create(updatingUser);
        } catch (IOException e) {
            System.out.println("I/O 오류 : 수정 실패");
            return null;
        }
    }

    @Override
    public User findById(UUID id) {
        try {
            return fileUserRepository.findById(id);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 오류" + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("입력한 id에 해당하는 파일이 없습니다", e);
        }
    }

    @Override
    public Set<User> findAll() {
        return fileUserRepository.findAll();
    }

    @Override
    public void delete(User user) {
        try {
            fileUserRepository.delete(user);
        } catch (NoSuchFileException e) {
            throw new RuntimeException(String.format(
                    "[%s] %s의 파일은 존재하지 않습니다", user.getFirstId(), user.getName()), e);
        } catch (IOException e) {
            System.out.println("User 파일 삭제 중 오류 발생");
            e.printStackTrace();
        }
    }

    @Override
    public Set<User> findUsersByName(String findName) {
        return findAll().stream()
                .filter(user -> user.getName().equals(findName))
                .collect(Collectors.toSet());
    }

    @Override
    public User findByNamePW(String name, String password) {
        return findUsersByName(name).stream()
                .filter(user -> user.getPassword().equals(password))
                .findAny()
                .orElse(null);

        // 패스워드도 이름도 똑같다면 컬렉션 반환인데, 그럴 가능성 없다고 가증
    }

    /**
     *  검증, 디렉토리 생성
     */
    // 검증은 레포지토리까지 갈 필요도 없이 여기서 처리
    @Override
    public void validateDuplicateName(String name) {
        boolean isDuplicate = findAll().stream()
                .anyMatch(user -> user.getName().equals(name));
        if (isDuplicate){
            throw new DuplicateName(String.format("%s는 이미 존재하는 닉네임입니다.", name));
        }
    }

    // 디렉토리 생성
    public void createUserDirectory() {
        try {
            fileUserRepository.createUserDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
