package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.ChannelDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exepction.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("JCFChannelService")
public class JCFChannelService implements ChannelService {

    protected Map<UUID,Channel> channelList=new LinkedHashMap<>();
    protected static final Set<UUID> existChannelIdCheck = new HashSet<>();
    protected static final Set<String> existNameCheck = new HashSet<>();

    @Autowired
    @Qualifier("FileMessageService")
    private JCFMessageService messageService;

    @Autowired
    @Qualifier("FileUserService")
    private JCFUserService userService;

    @Autowired
    @Qualifier("FileUserStatusService")
    private JFCUserStatusService userStatusService;

    @Override
    public void createNewChannel(String name) {
        if(!existNameCheck.contains(name)){
            System.err.println("이미 존재하는 채널 이름");
        }

        Channel newChannel;
        do{
            newChannel=Channel.createDefaultChannel(name);
        }while(existChannelIdCheck.contains(newChannel.getId()));

        channelList.put(newChannel.getId(),newChannel);
        existChannelIdCheck.add(newChannel.getId());
        existNameCheck.add(name);

    }

    @Override
    public void createPrivateChannel( ) {
        Channel newChannel;
        do{
            newChannel=Channel.createPrivateChannel();
        }while(existChannelIdCheck.contains(newChannel.getId()));

        channelList.put(newChannel.getId(),newChannel);
        existChannelIdCheck.add(newChannel.getId());

    }

    @Override
    public <T> List<ChannelDto> readChannel(T key) {
        List<ChannelDto> list = new ArrayList<>();
        LinkedHashMap<UUID, Channel> foundChannel = new LinkedHashMap<>();

        if (key instanceof String) {
            foundChannel = findChannel((String) key);
        } else if (key instanceof UUID) {
            foundChannel = findChannel((UUID) key);
        }

        for (Channel val : foundChannel.values()) {
            list.add(ChannelDto.fromEntity(val));  // DTO 변환
        }

        return list;
    }


    @Override
    public List<ChannelDto> readChannelAll() {
        List<ChannelDto> list = new ArrayList<>();
        for(Channel val:channelList.values()){
            list.add(ChannelDto.fromEntity(val));
        }

        return list;
    }

    @Override
    public boolean updateChannelName(UUID id, String name) {
        LinkedHashMap<UUID,Channel> instance = findChannel(id);
        return updateChannelInfo(instance,name);
    }

    @Override
    public boolean updateChannelName(String ChannelName, String name) {
        LinkedHashMap<UUID,Channel> instance = findChannel(ChannelName);
        return updateChannelInfo(instance,name);
    }


    @Override
    public boolean updateChannel(ChannelDto channelDto) {
        UUID channelId = channelDto.id();
        String newChannelName = channelDto.channelName();

        LinkedHashMap<UUID, Channel> channelInstance = findChannel(channelId);
        if (channelInstance == null || channelInstance.isEmpty()) {
            System.out.println("해당하는 채널이 없습니다.");
            return false;
        }
        Channel channel = channelInstance.values().iterator().next();
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            System.out.println("PRIVATE 채널은 수정할 수 없습니다.");
            return false;
        }
        return updateChannelInfo(channelInstance, newChannelName);
    }

    @Override
    public boolean deleteChannel(UUID id) {
        LinkedHashMap<UUID,Channel> instance = findChannel(id);
        return deleteChannelInfo(instance);
    }

    @Override
    public boolean deleteChannel(String Name) {
        LinkedHashMap<UUID,Channel> instance = findChannel(Name);
        return deleteChannelInfo(instance);
    }

    @Override
    public String readChannelInUser(String channelName){
        LinkedHashMap<UUID,Channel>channelInstant=findChannel(channelName);
        String returnVal="";
        if (channelInstant==null||channelInstant.isEmpty()){
            System.out.println("해당하는 채널이 없습니다.");
        }else if(channelInstant.size()==1){
            Channel firstChannel=channelInstant.entrySet().iterator().next().getValue();
            returnVal+=firstChannel.readUserList();
        }else{
            System.out.println("해당하는 채널이 중복입니다.");
        }
        return returnVal;
    }


    @Override
    public boolean addMessageToChannel(String channelName,String title){
        LinkedHashMap<UUID,Channel>channelInstant=findChannel(channelName);
        Channel firstChannel=null;
        if (channelInstant==null||channelInstant.isEmpty()){
            System.out.println("해당하는 채널이 없습니다.");
            return false;
        }else if(channelInstant.size()==1){
            firstChannel=channelInstant.entrySet().iterator().next().getValue();
        }else{
            System.out.println("해당하는 채널이 중복입니다.");
            return false;
        }

        LinkedHashMap<UUID, Message> instance =messageService.findMessage(title);
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 유저가 없습니다.");
            return false;
        }else if(instance.size()==1){
            Message firstMessage = instance.entrySet().iterator().next().getValue();
            firstChannel.addMessage(firstMessage);
            return true;
        }
        else
            return false;
    }

    protected Channel getSingleChannel(Object key) {
        LinkedHashMap<UUID, Channel> channels = null;
        if(key instanceof String){
            channels = findChannel((String) key);
        }
        else if(key instanceof UUID){
            channels = findChannel((UUID)key);
        }
        if (channels==null||channels.isEmpty()) throw new ChannelNotFoundException("채널을 찾을 수 없습니다.");
        if (channels.size() > 1) throw new ChannelNotFoundException("채널이 중복됩니다.");
        return channels.values().iterator().next();
    }

    protected User getSingleUser(Object key) {
        LinkedHashMap<UUID, User> users=null;
        if(key instanceof String){
            users = userService.findUser((String) key);
        }
        else if(key instanceof UUID){
            users = userService.findUser((UUID) key);
        }
        if (users==null||users.isEmpty()) throw new ChannelNotFoundException("유저를 찾을 수 없습니다.");
        if (users.size() > 1) throw new ChannelNotFoundException("유저가 중복됩니다.");
        return users.values().iterator().next();
    }


    @Override
    public <T,K> ReadStatus addUserToChannel(T channelName, K userName) {

        Channel channel = getSingleChannel(channelName);
        User user = getSingleUser(userName);

        userStatusService.add(user.getUserStatus());
        channel.addUser(user, user.getUserStatus());

        return channel.getChannelType() == ChannelType.PRIVATE ? channel.addReadStatus(user) : null;
    }


    @Override
    public String readChannelInMessage(String channelName){
        LinkedHashMap<UUID,Channel>channelInstant=findChannel(channelName);
        String returnVal="";
        if (channelInstant==null||channelInstant.isEmpty()){
            System.out.println("해당하는 채널이 없습니다.");
        }else if(channelInstant.size()==1){
            Channel firstChannel=channelInstant.entrySet().iterator().next().getValue();
            returnVal+=firstChannel.readMessageList();
        }else{
            System.out.println("해당하는 채널이 중복입니다.");
        }
        return returnVal;
    }

    protected LinkedHashMap<UUID,Channel> findChannel(String name) {
        LinkedHashMap<UUID,Channel> findChannel=new LinkedHashMap<>();
        for(Channel channel:channelList.values()){
            if(channel.getChannelName().equals(name)){
                findChannel.put(channel.getId(),channel);
            }
        }
        return findChannel;
    }


    protected LinkedHashMap<UUID,Channel> findChannel(UUID ID) {
        LinkedHashMap<UUID,Channel> findChannel=new LinkedHashMap<>();
        findChannel.put(ID,channelList.get(ID));
        return findChannel;
    }


    protected boolean deleteChannelInfo(LinkedHashMap<UUID,Channel> instance) {
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 채널이 없습니다.");
            return false;
        }else if(instance.size()==1){
            Channel firstChannel=instance.entrySet().iterator().next().getValue();
           if (ChannelType.PRIVATE==firstChannel.getChannelType()) {
               System.out.println("PRIVATE 채널은 삭제할 수 없습니다.");
               return false;
           }

            firstChannel.getMessageList().clear();
            firstChannel.getUserList().clear();

            existChannelIdCheck.remove(firstChannel.getId());
            channelList.remove(firstChannel.getId());
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelList.values().stream()
                .filter(channel ->
                        ChannelType.PUBLIC == channel.getChannelType() ||
                                (channel.getChannelType() == ChannelType.PRIVATE &&
                                        channel.getUserList().keySet().stream().anyMatch(user -> user.getId().equals(userId)))
                )
                .map(ChannelDto::fromEntity)
                .collect(Collectors.toList());
    }
    @Override
    public List<ChannelDto> findAllByUserName(String userName) {
        return channelList.values().stream()
                .filter(channel ->
                        ChannelType.PUBLIC == channel.getChannelType() ||
                                (channel.getChannelType() == ChannelType.PRIVATE &&
                                        channel.getUserList().keySet().stream().anyMatch(user -> user.getName().equals(userName)))
                )
                .map(ChannelDto::fromEntity)
                .collect(Collectors.toList());
    }

    protected boolean updateChannelInfo(LinkedHashMap<UUID,Channel> instance, String changeName) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 채널이 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Channel firstChannel = instance.entrySet().iterator().next().getValue();
            existNameCheck.remove(firstChannel.getChannelName());
            firstChannel.updateChannelName(changeName);
            firstChannel.updateUpdatedAt();
            existNameCheck.add(changeName);
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }

    }


    public List<UserStatus> getUserStatusAll(Object key) {
        Channel channel = getSingleChannel(key);
        return new ArrayList<>(channel.getUserList().values());
    }

    public List<ReadStatus> getReadStatusAll(Object key) {
        Channel channel = getSingleChannel(key);
        return new ArrayList<>(channel.getReadStatusList().values());
    }

    @Override
    public void deleteUserToChannel(String channelName, String userName){
        LinkedHashMap<UUID,Channel>channelInstant=findChannel(channelName);
        Channel firstChannel=null;
        if (channelInstant==null||channelInstant.isEmpty()){
            System.out.println("해당하는 채널이 없습니다.");
            throw new ChannelNotFoundException("");
        }else if(channelInstant.size()==1){
            firstChannel=channelInstant.entrySet().iterator().next().getValue();
        }else{
            System.out.println("해당하는 채널이 중복입니다.");
            throw new ChannelNotFoundException("");
        }
        firstChannel.deleteUser(userName);

    }

    @Override
    public <T,K,C,Q> boolean sendMessageInUser(T channel, K sender, C reciver,Q message){
        Channel findChannel=getSingleChannel(channel);
        if(findChannel==null)
            return false;
        boolean result=userService.sendMessageToUser(sender,reciver,message);
        if(result){
            findChannel.addMessage(messageService.readMessage(message).get(0));
        }else{
            return false;
        }
        return true;
    }



}