package mission.repository.file;

import mission.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserRepository {


    // User 디렉토리 Path 객체
    private static final Path USER_DIRECT_PATH = Path.of("userDirectory");

    // 유저 저장
    public User saveUser(User user) throws IOException {
        // 이름 중복검사는 메인 서비스에서
        Path filePath = USER_DIRECT_PATH.resolve(user.getId() + ".ser");

        //        // 존재하지 않으면 저장 (나중에 수정할 때 saveUser메서드 활용해야 하므로 구분 필요할거같음)
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(user);
        } //oos.close(); << 이거 줄이려고 try   +  나중에 예외처리 할 때 편하려고 try
        return user;
    }

    /**
     * 조회
     */
    public Set<User> findAll() throws IOException {
        if (!Files.exists(USER_DIRECT_PATH)) {
            return new HashSet<>();
        } else {
            return Files.list(USER_DIRECT_PATH)
                    .filter(path -> path.toString().endsWith(".ser"))
                    .map(this::readUserFromFile)
                    // 역직렬화
                    .collect(Collectors.toCollection(HashSet::new));
        }
    }

    public User findUserById(UUID id) throws IOException, ClassNotFoundException {
        // 1. id로 filePath 얻기
        Path userFilePath = getUserFilePath(id);

        // 2. 1에서 얻은 filePath가 유효한지 테스트
        if (!Files.exists(userFilePath)) {
            System.out.println("주어진 id의 유저파일이 존재하지 않습니다.");
            return null;
        }

        // 3. 읽기
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(userFilePath));
        return (User) ois.readObject(); // 저장 성공
    }

    public User updateUserNamePW(User updatingUser) throws IOException {
        UUID id = updatingUser.getId();
        // NULL일리가 없음 이미 서비스 단에서 NAME PW 검사할 때 걸러짐
        // 덮어쓰기
        saveUser(updatingUser);
        return updatingUser;
    }

    public void delete(User user) throws IOException {
        Path deletingUserPath = getUserFilePath(user.getId());
        System.out.printf("%s 파일 삭제 완료", user.getName());
        System.out.println();
        Files.delete(deletingUserPath);


//        } catch (NoSuchFileException e) {
//            throw new RuntimeException(String.format(
//                    "[%s] %s의 파일은 존재하지 않습니다", user.getFirstId(), user.getName()));
//        } catch (IOException e) {
//            System.out.println("User 파일 삭제 중 오류 발생");
//            e.printStackTrace();
//        }
    }


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

    // 파일 생성
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

