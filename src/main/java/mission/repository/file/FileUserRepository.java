package mission.repository.file;

import mission.entity.User;
import mission.repository.UserRepository;
import mission.service.exception.NotFoundId;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {


    // User 디렉토리 Path 객체
    private static final Path USER_DIRECT_PATH = Path.of("userDirectory");

    @Override
    public User saveUser(User user) throws IOException {
        Path filePath = USER_DIRECT_PATH.resolve(user.getId() + ".ser");

        // 파일 존재하지 않으면(나중에 수정할 때 saveUser메서드 활용해야 하므로 구분 필요할것 같음
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(filePath))) {
            oos.writeObject(user);
        } //oos.close(); << 이거 줄이려고 try   +  나중에 리펙토링 시 예외처리 여기서 할 때 편하려고 try
        return user;
    }

    /**
     * 조회
     */
    @Override
    public Set<User> findAll(){
        try {
            if (!Files.exists(USER_DIRECT_PATH)) {
                System.out.println("USER 디렉토리가 존재하지 않습니다");
                return new HashSet<>();
            } else {
                return Files.list(USER_DIRECT_PATH)
                        .filter(path -> path.toString().endsWith(".ser"))
                        .map(this::readUserFromFile)
                        // 역직렬화
                        .collect(Collectors.toCollection(HashSet::new));
            }
        } catch (IOException e) {
            throw new RuntimeException("전체 조회 I/O 오류 발생: " + e.getMessage(), e);
        }
    }

    @Override
    public User findById(UUID id) {
        // 1. id로 filePath 얻기
        Path userFilePath = getUserFilePath(id);

        // 2. 1에서 얻은 filePath가 유효한지 테스트
        if (!Files.exists(userFilePath)) {
            throw new NotFoundId();
        }

        // 3. 읽기
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(userFilePath))){
            return (User) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 오류" + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("입력한 id에 해당하는 파일이 없습니다", e);
        }
    }

    @Override
    public void delete(User user) {
        try {
            Files.delete(getUserFilePath(user.getId()));
        } catch (NoSuchFileException e) {
            throw new RuntimeException(String.format(
                    "[%s] %s의 파일은 존재하지 않습니다", user.getFirstId(), user.getName()), e);
        } catch (IOException e) {
            System.out.println("User 파일 삭제 중 오류 발생");
            e.printStackTrace();
        }

        System.out.printf("%s 파일 삭제 완료", user.getName());
        System.out.println();
    }

//    @Override
//    public User updateUserNamePW(User updatingUser) throws IOException {
//        // 덮어쓰기
//        saveUser(updatingUser);
//        return updatingUser;
//    }


    /**
     * 편의 메서드 모음
     */
    private User readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 역직렬화 실패: " + e.getMessage());
        }
    }

    // Path 만드는 역할만 (검증은 x)
    private Path getUserFilePath(UUID userId) {
        return USER_DIRECT_PATH.resolve(userId.toString() + ".ser");
    }



    // 테스트 시 파일 디렉토리 세팅을 위한 메서드
    public void createUserDirectory() throws IOException {
        // 존재하면 안의 파일 전부 삭제
        if (Files.exists(USER_DIRECT_PATH)){
            try {
                Files.list(USER_DIRECT_PATH).forEach(path -> {
                    try {
                        Files.delete(path); // 개별 파일 삭제
                    } catch (IOException e) {
                        throw new RuntimeException("파일 삭제 실패" + e.getMessage());
                    }
                });
            } catch (IOException e) {
                System.out.println("U 디렉토리 초기화 실패");
            }
        } else {
            Files.createDirectory(USER_DIRECT_PATH);
        }
    }
}

