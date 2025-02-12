package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.util.SerializationUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.io.IOException;



public class FileJavaApplication {
  /* 만약 디렉토리가 없다면, 파일 저장 시 오류가 발생하기 때문에 프로그램이 시작될 때 자동으로 디렉토리를 생성하는 초기화 로직을 넣어야함.
    1. 클래스 안에 초기화 메서드
    2. psvm 안에 1) 디렉토리 선언 2) 초기화
   */

  public static void init(Path directory) {
    // 저장할 경로의 파일 초기화 메서드
    if (!Files.exists(directory)) {
      try {
        Files.createDirectories(directory);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }


  public static void main(String[] args){
    // 1) 디렉토리 선언 2) 초기화
    Path directory = Paths.get("data"); // 폴더 경로를 지정하는 부분. data는 파일을 저장할 폴더 이름일 뿐. 마음대로 변경 가능
    init(directory);

    SerializationUtil<User> util = new SerializationUtil<>();

    FileUserRepository fileUserRepository = new FileUserRepository(util, "user.ser");
    FileUserService fileUserService = new FileUserService(fileUserRepository);

    // ===== 회원 직렬화 =====
    System.out.println("\n===== 회원 서비스 CRUD =====");
      // 등록
    User user1 = new User("정연경", 24, Gender.FEMALE);
    boolean created1 = fileUserService.createUser(user1);

    User user2 = new User("신서연", 23, Gender.FEMALE);
    boolean created2 = fileUserService.createUser(user2);

    if(created1 || created2){
      List<User> userList = fileUserService.readAllUsers(); // 직렬화 대상!! : 최종 리스트 readAllUsers()
      util.saveData(userList);
    }

      // 조회 - 단건
    fileUserService.readUser(user1.getId());

     // 조회 - 다건
    fileUserService.readAllUsers();

      // 수정 & 조회
    fileUserService.updateUser(user1.getId(), "정연경", 23, Gender.FEMALE);
    util.saveData(fileUserService.readAllUsers());

      // 삭제 & 조회
    fileUserService.deleteUser(user1.getId());
    util.saveData(fileUserService.readAllUsers());

    // ===== 회원 파일 역직렬화 =====
    // for문 써서 리스트 객체 하나하나 프린트 해줘야 함
    System.out.println("\n===== 파일에서 회원 데이터 불러오기 =====");
    List<User> deserializedUsers = util.loadData();
    for (User u : deserializedUsers)
      System.out.println(u);
    // deserializedUsers.forEach(System.out::println);
  }
}
