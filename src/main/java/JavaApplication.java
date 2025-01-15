import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class JavaApplication {
    public static void main(String[] args) {
        FileUserService userService = new FileUserService();

        // 파일 초기화
        userService.resetFile();

        // 사용자 추가
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", "bob@example.com");
        User user3 = new User("Charlie", "charlie@example.com");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        // 저장 후 데이터 확인
        System.out.println("파일 저장 후 사용자 목록:");
        userService.readAllUsers().forEach(System.out::println);

        // 특정 사용자 검색
        try {
            System.out.println("ID 1 사용자: " + userService.readUser(user2.getId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 특정 사용자 업데이트
        System.out.println("\n 업데이트 : alice의 이름 변경");
        user1.updateEmail("alice@newemail.com");
        userService.updateUser(user1);
        userService.readAllUsers().forEach(System.out::println);

        // 특정 사용자 삭제
        System.out.println("특정 사용자 삭제");
        userService.deleteUser(user1.getId().toString());
        userService.readAllUsers().forEach(System.out::println);
    }


}