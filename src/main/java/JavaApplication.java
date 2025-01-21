import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

//        // [ ] Application에서 서비스 구현체를 File*Service로 바꾸어 테스트해보세요.
//
//        // FileUserService 초기화
//        InputHandler inputHandler = new InputHandler();  // 필요 시 InputHandler 구현 필요
//        FileUserService fileUserService = new FileUserService(inputHandler);
//
//        // 1. 유저 생성
//        String nickname = "Winter";
//        UUID userId = fileUserService.createUser(nickname);
//        System.out.println("User created with ID: " + userId);
//
//        System.out.println("--------------------------------------------------------");
//
//        // 2. 생성된 유저 로드
//        User user = fileUserService.load(userId);
//        System.out.println("Loaded user: " + user);
//
//        System.out.println("--------------------------------------------------------");
//
//        // 3. 유저 닉네임 업데이트
//        String newNickname = "Spring";
//        fileUserService.updateUserNickname(userId);
//        System.out.println("asdfasdffsd " + userId);
//        User updatedUser = fileUserService.load(userId);
//        System.out.println("asdfas1564dffsd " + userId);
//        System.out.println("Updated user nickname: " + updatedUser.getNickname());
//
//
//        System.out.println("--------------------------------------------------------");
//
//        // 4. 유저 삭제
//        fileUserService.removeUserById(userId);
//        System.out.println("User with ID " + userId + " deleted.");
//
//
//        System.out.println("--------------------------------------------------------");
//
//        // 5. 사용자 파일 삭제 확인
//        User deletedUser = fileUserService.load(userId);
//        if (deletedUser == null) {
//            System.out.println("User not found, as expected.");
//        }
    }
}
