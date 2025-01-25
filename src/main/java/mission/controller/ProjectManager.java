package mission.controller;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import java.util.Set;
import java.util.UUID;

public class ProjectManager {

    // Channel, Message, User 서비스간 의존이 생기므로(앞으로 더 생길 예정) 이렇게 Main 클래스 있는게 더 나을거라 판단
    // 추가로 service의 업무를 분담(ex. 중복 검증 후 생성)
    private final JCFUserService userService = new JCFUserService();
    private final JCFMessageService messageService = new JCFMessageService();
    private final JCFChannelService channelService = new JCFChannelService();






    /**
     * 채널과 유저 서로 추가
     */






}
