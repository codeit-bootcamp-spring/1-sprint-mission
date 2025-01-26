package mission;

import mission.controller.ChannelController;
import mission.controller.MessageController;
import mission.controller.UserController;
import mission.controller.file.FileChannelController;
import mission.controller.file.FileMessageController;
import mission.controller.file.FileUserController;
import mission.service.file.FileChannelService;
import mission.service.file.FileMessageService;
import mission.service.file.FileUserService;

import java.io.IOException;

public class fileApplication {

    private static final ChannelController fileChannelController = new FileChannelController();
    private static final UserController fileUserController = new FileUserController();
    private static final MessageController fileMessageController = new FileMessageController();

    public static void main(String[] args) {
        initDirectory();

        fileUserController.create("유저 A", "12345");
        fileUserController.create("유저 B", "12345");
        fileUserController.create("유저 C", "12345");
        //fileUserController.find


    }


    private static void initDirectory(){
        fileChannelController.createChannelDirectory();
        System.out.println("Channel 디렉토리 추가 완료");
        fileUserController.createUserDirectory();
        System.out.println("User 디렉토리 추가 완료");
        fileMessageController.createMessageDirectory();
        System.out.println("Message 디렉토리 추가 완료");
    }
}
