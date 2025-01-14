package mission;

import mission.entity.User;
import mission.service.file.FileMainService;
import mission.service.jcf.ProjectManager;

import java.io.IOException;
import java.util.List;

public class javaApplication {

    private static final ProjectManager projectManager = new ProjectManager();
    private static final FileMainService fileMainService = new FileMainService();

    public static void main(String[] args) throws IOException {
        // 사전 세팅
        fileMainService.createUserDirectory();

        User user = fileMainService.createUser("요한", "sjsjsjs");
        User findUser = fileMainService.findUserById(user.getId());
        System.out.println("findUser = " + findUser);
        System.out.println("user = " + user);
        System.out.println("유저 검증: " + user.equals(findUser));
        System.out.println("fileMainService.findAll().size() = " + fileMainService.findAll().size());
    }

    private static void initDirectory(){

    }
}
