package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JcfChannelService implements ChannelService {
    //private final Map<UUID, Channel> list; //채널 리스트
    //private final Map<UUID, List<UUID>> messages = new HashMap<>(); //채널에서 보유 중인 메시지들 ( 변경 가능성 없음 )
    private JcfMessageService jcfMessageService;
    private final JCFChannelRepository jcfChannelRepository;
    private static volatile JcfChannelService instance;

    public JcfChannelService(JCFChannelRepository jcfChannelRepository) {
        this.jcfChannelRepository = jcfChannelRepository;
    }

    public static JcfChannelService getInstance() {
        if (instance == null) {
            synchronized (JcfUserService.class) {
                if (instance == null) {
                    instance = new JcfChannelService( new JCFChannelRepository(new HashMap<>()));
                }
            }
        }
        return instance;
    }
    public void setJcfMessageService(JcfMessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }

    public UUID addMessage_By_Channel(UUID channelId, UUID sender, String content) {
        if(!jcfChannelRepository.validationUUID(channelId)||sender==null){ return null; }
        //jcfChannelRepository.save(sender)
        UUID messageId = jcfMessageService.createMessage(sender, content);
        jcfChannelRepository.addMessage(channelId, messageId);
        //messages.get(channelId).add(messageId); //검증 필요
        return messageId;
    }



    public List<Message> messages(UUID channelId) {
        if(!jcfChannelRepository.validationUUID(channelId)){ return null; }
        List<Message> collect = jcfChannelRepository.messages(channelId).stream().map(s -> jcfMessageService.getMessage(s)).toList();
        return new ArrayList<>(collect);
    }

    @Override
    public UUID createChannel(String channelName) {

        return jcfChannelRepository.save(channelName);
    }

    @Override
    public Channel getChannel(UUID id) {
        return jcfChannelRepository.findById(id);
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> collect = jcfChannelRepository.findAll();
        //List<Channel> collect = list.entrySet().stream().map(s -> s.getValue()).collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    @Override
    public void updateChannel(UUID id, String channelName) {
        jcfChannelRepository.update(id, channelName);
        //리턴해서 출력하는 방안 고려
    }

    @Override
    public void deleteChannel(UUID channelId) {

        if (jcfChannelRepository.findAll().stream().map(Channel::getId).toList().contains(channelId)) {
            List<Message> collect = jcfChannelRepository.messages(channelId).stream().map(s -> jcfMessageService.getMessage(s)).toList();
            if(jcfChannelRepository.delete(channelId)){
                for (Message message : collect) {
                    try {
                        jcfMessageService.deleteMessage(message.getId());
                    } catch (NullPointerException e) {
                    } //삭제라서 nullPointException 무시했습니다.
                    System.out.println("성공적으로 삭제 되었습니다.");
                }
            }else {
                System.out.println("삭제 실패했습니다.");
            }
        } else {
            System.out.println("채널을 찾을 수 없습니다.");
        }

    }

}
