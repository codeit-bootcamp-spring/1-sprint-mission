package com.sprint.mission;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class JavaApplication {
    public static void main(String[] args) {

        JCFUserService JCFUserServiceInstance = JCFUserService.getInstance();
        JCFChannelService JCFChannelServiceInstance = JCFChannelService.getInstance();
        FileUserService FileUserServiceInstance = FileUserService.getInstance();
        FileChannelService FileChannelServiceInstance = FileChannelService.getInstance();
        FileMessageService FileMessageServiceInstance = FileMessageService.getInstance();
        JCFMessageService JCFMessageServiceInstance = JCFMessageService.getInstance();

        //JCF 유저 생성
        UUID JCFuser1 = JCFUserServiceInstance.createUser("Kaya");
        UUID JCFuser2 = JCFUserServiceInstance.createUser("Ayden");
        UUID JCFuser3 = JCFUserServiceInstance.createUser("Sia");

        //JCF채널 생성
        UUID JCFchannel1 = JCFChannelServiceInstance.createChannel("Kaya's Channel");

        //JCF채널1에 생성한 모든 유저 추가
        JCFUserServiceInstance.getUsersMap().keySet().stream().forEach(userId -> {JCFChannelServiceInstance.addChannelMember(JCFchannel1, userId);});

        //JCF채널1에 속한 유저 이름 출력
        JCFChannelServiceInstance.printAllMemberNames(JCFchannel1);

        //직접 생성한 JCF 유저들을 모두 직렬화
        JCFUserServiceInstance.exportUsersMap("JCFUsersMap");

        //직렬화했던 JCF 유저들을 File 클래스를 이용해 관리할 수 있도록 역직렬화
        FileUserServiceInstance.importUserMap("JCFUsersMap");

        //FileUserService를 이용해서 불러온 유저들 출력
        System.out.println("역직렬화로 불러온 파일클래스 유저들 : " + FileUserServiceInstance.getUsersMap().keySet().stream().map((userId) -> FileUserServiceInstance.getUserNameById(userId)).collect(Collectors.joining(", ")));

        //메세지 생성
        UUID JCFmessage1 = JCFMessageServiceInstance.createMessage(JCFuser2, JCFchannel1, "Hello World");
        UUID JCFmessage2 = JCFMessageServiceInstance.createMessage(JCFuser1, JCFchannel1, "It's hard to debug... Hey java.. just work al jal ddak ggal sen please.. ");

        //메세지 내용 및 상세정보 출력
        JCFMessageServiceInstance.printMessageDetails(JCFmessage1);

        //생성한 채널 직렬화
        JCFChannelServiceInstance.exportChannelMap("JCFChannelMap");
        //생성한 메세지들 직렬화
        JCFMessageServiceInstance.exportMessagesMap("JCFMessagesMap");
        //생성한 채널 역직렬화
        FileChannelServiceInstance.importChannelMap("JCFChannelMap");
        //생성한 메세지들 역직렬화
        FileMessageServiceInstance.importMessageMap("JCFMessagesMap");

        //JCF로 생성하고 직렬화했던 메세지들 역직렬화하여 상세정보 출력
        UUID FileMessage1 = JCFmessage1;
        UUID FileMessage2 = JCFmessage2;

        FileMessageServiceInstance.reviseMessageContent(JCFmessage2, "Bye World");
        FileMessageServiceInstance.printMessageDetails(JCFmessage2);


    }
}