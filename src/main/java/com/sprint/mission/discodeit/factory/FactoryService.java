package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.observer.Observer;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageReplsitory;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.observer.ChannelObserver;
import com.sprint.mission.discodeit.validation.ChannelValidtor;
import com.sprint.mission.discodeit.validation.MessageValidator;
import com.sprint.mission.discodeit.validation.UserValidator;

public class FactoryService implements Factory{

    private static UserService userServiceInstance;
    private static UserService fileUserServiceInstance;

    private static ChannelService channelServiceInstance;
    private static ChannelService fileChannelServiceInstance;

    private static MessageService messageServiceInstance;
    private static MessageService fileMessageServiceInstance;
    //repository(User)
    private static UserRepository userRepositoryInstance;
    private static FileUserRepository fileUserRepositoryInstance;
    //repository(Channel)
    private static ChannelRepository channelRepositoryInstance;
    private static FileChannelRepository fileChannelRepositoryInstance;
    //repository(Message)
    private static MessageRepository messageRepositoryInstance;
    private static MessageRepository fileMessageRepositoryInstance;
    //validation 패키지 객체를
    private static MessageValidator messageValidatorInstance;
    private static UserValidator userValidatorInstance;
    private static ChannelValidtor channelValidatorInstance;

    //Observer
    private static ObserverManager observerManagerInstance;
    private static Observer channelObserverInstance;

    //유저 서비스
    @Override
    public synchronized UserService  createUserService() {
        if(userServiceInstance == null){
            userServiceInstance = new JCFUserService(userRepositoryInstance,userValidatorInstance);
        }
        return userServiceInstance;
    }

    @Override
    public synchronized UserService  createFileUserService() {
        if(fileUserServiceInstance == null){
            fileUserServiceInstance = new FileUserService(fileUserRepositoryInstance,userValidatorInstance);

        }

        return fileUserServiceInstance;
    }

    //채널 서비스
    @Override
    public synchronized ChannelService createChannelService() {
        if(channelServiceInstance == null){
            channelServiceInstance
                    = new JCFChannelService(createObserverManager(),channelValidatorInstance,channelRepositoryInstance);
        }
        return channelServiceInstance;
    }

    @Override
    public synchronized ChannelService createFileChannelService() {
        if(fileChannelServiceInstance == null){
            fileChannelServiceInstance
                    = new FileChannelService(createObserverManager(),channelValidatorInstance,fileChannelRepositoryInstance);
        }
        return fileChannelServiceInstance ;
    }


    //현재는 옵저버가 감지하는게 deleteChannel 메소드 작동만 감지해서 클라이언트에서 테스트시
    //일일이 객체 생성하기 번거로워서 일단 넣었습니다.
    // but: createMessageService() 메서드의 동작이 단순히 MessageService 객체 생성이 아니라 observerService,
    //      MessageValidator 객체를 생성하는 흐름이, 동작을 모르는 사람이 사용 시 혼동??을 줄 수 있지 않을까...요??
    //      만약 그렇다면 어떻게 하는게 좋을지..(-_-)...
    //-------------------> 결론 따로 분리합시다.. <----------------------
    @Override
    public synchronized MessageService createMessageService() {
        if(messageServiceInstance == null){
            messageServiceInstance = new JCFMessageService(messageValidatorInstance, messageRepositoryInstance);
        }
        return messageServiceInstance;

    }

    @Override
    public synchronized MessageService createFileMessageService() {
        if(fileMessageServiceInstance == null){
            fileMessageServiceInstance = new FileMessageService(messageValidatorInstance,fileMessageRepositoryInstance);
        }
        return fileMessageServiceInstance;

    }

    //observer
    @Override
    public synchronized Observer createChannelObserver() {
        if ( channelObserverInstance == null) {
            if(messageServiceInstance == null){
                channelObserverInstance = new ChannelObserver(fileMessageServiceInstance);
                observerManagerInstance.addObserver(channelObserverInstance);
            }else{
                channelObserverInstance = new ChannelObserver(messageServiceInstance);
                observerManagerInstance.addObserver(channelObserverInstance);
            }
        }
        return channelObserverInstance;
    }
    @Override
    public synchronized ObserverManager createObserverManager(){
        if(observerManagerInstance == null){
            observerManagerInstance = new ObserverManager();
        }
        return observerManagerInstance;
    }


    //repository(User)
    @Override
    public synchronized UserRepository createUserRepository(){
        if(userRepositoryInstance == null){
            userRepositoryInstance = new JCFUserRepository();
        }
        return userRepositoryInstance;
    }

    @Override
    public synchronized UserRepository createFileUserRepository(){
        if(fileUserRepositoryInstance == null){
            fileUserRepositoryInstance = new FileUserRepository();
        }
        return fileUserRepositoryInstance;
    }

    //repository(Channel)
    @Override
    public synchronized ChannelRepository createChannelRepository(){
        if(channelRepositoryInstance == null){
            channelRepositoryInstance = new JCFChannelRepository();
        }
        return channelRepositoryInstance;
    }

    @Override
    public synchronized ChannelRepository createFileChannelRepository(){
        if(fileChannelRepositoryInstance == null){
            fileChannelRepositoryInstance = new FileChannelRepository();
        }
        return fileChannelRepositoryInstance;
    }

    //repository(Message)
    @Override
    public synchronized MessageRepository createMessageRepository(){
        if(messageRepositoryInstance == null){
            messageRepositoryInstance = new JCFMessageReplsitory();
        }
        return messageRepositoryInstance;
    }

    @Override
    public synchronized MessageRepository createFileMessageRepository(){
        if(fileMessageRepositoryInstance == null){
            fileMessageRepositoryInstance = new FileMessageRepository();
        }
        return fileMessageRepositoryInstance;
    }




    //validation
    @Override
    public synchronized MessageValidator createMessageValidator(){
        if(messageValidatorInstance == null) {
            if (userServiceInstance == null) {
                messageValidatorInstance = new MessageValidator(fileChannelServiceInstance, fileUserServiceInstance);
            } else {
                messageValidatorInstance = new MessageValidator(channelServiceInstance, userServiceInstance);
            }
        }
        return messageValidatorInstance;
    }
    @Override
    public synchronized UserValidator createUserValidator(){
        if(userValidatorInstance == null){
            userValidatorInstance = new UserValidator();
        }
        return userValidatorInstance;
    }

    @Override
    public synchronized ChannelValidtor createChannelValidator(){
        if(channelValidatorInstance == null){
            channelValidatorInstance = new ChannelValidtor();
        }
        return channelValidatorInstance;
    }



}