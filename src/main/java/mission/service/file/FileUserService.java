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

    private static FileUserService fileUserService;
    private FileUserService(){}
    public static FileUserService getInstance(){
        if (fileUserService == null) return fileUserService = new FileUserService();
        else return fileUserService;
    }

    @Override
    public User createOrUpdate(User user) throws IOException {
        return fileUserRepository.save(user);
        // 수정-생성 I/O오류 구분하기 위해 여기서 잡지 않고 I/O 예외 던지기
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
    public void delete(User user) {
        fileUserRepository.delete(user);
    }

    /**
     * 검증, 디렉토리 생성
     */
//    @Override  // 중복 허용 결정
//    public void validateDuplicateName(String name) {
//        boolean isDuplicate = findAll().stream()
//                .anyMatch(user -> user.getName().equals(name));
//        if (isDuplicate) {
//            throw new DuplicateName(String.format("%s는 이미 존재하는 닉네임입니다.", name));
//        }
//    }

    // 디렉토리 생성
    public void createUserDirectory() {
        try {
            fileUserRepository.createUserDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
