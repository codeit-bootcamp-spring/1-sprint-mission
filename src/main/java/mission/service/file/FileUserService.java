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
    public User createOrUpdate(User user) throws IOException {
        return fileUserRepository.saveUser(user);
        // 수정-생성 I/O오류 구분하기 위해 I/O 예외 던지기
    }

    @Override
    public User update(User updatingUser) {
        try {
            return createOrUpdate(updatingUser);
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 유저 수정 실패");
        }
    }

    @Override
    public User findById(UUID id) {
        return fileUserRepository.findById(id);
    }

    @Override
    public Set<User> findAll() {
        return fileUserRepository.findAll();
    }


    @Override
    public Set<User> findUsersByName(String findName) {
        return findAll().stream()
                .filter(user -> user.getName().equals(findName))
                .collect(Collectors.toSet());
    }


    /**
     * 삭제
     */
    @Override
    public void delete(User user) {
        fileUserRepository.delete(user);
    }

    /**
     * 검증, 디렉토리 생성
     */
    // 검증은 레포지토리까지 갈 필요도 없이 여기서 처리
    @Override
    public void validateDuplicateName(String name) {
        boolean isDuplicate = findAll().stream()
                .anyMatch(user -> user.getName().equals(name));
        if (isDuplicate) {
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
