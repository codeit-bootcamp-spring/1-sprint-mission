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
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
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

    private static UserService jcfUserServiceInstance;
    private static UserService fileUserServiceInstance;
    private static UserService basicUserServiceInstance;

    private static ChannelService JCFChannelServiceInstance;
    private static ChannelService fileChannelServiceInstance;
    private static ChannelService basicChannelServiceInstance;

    private static MessageService JCFMessageServiceInstance;
    private static MessageService fileMessageServiceInstance;
    private static MessageService basicMessageServiceInstance;


    //repository(User)
    private static UserRepository jcfUserRepositoryInstance;
    private static FileUserRepository fileUserRepositoryInstance;
    //repository(Channel)
    private static ChannelRepository jcfChannelRepositoryInstance;
    private static ChannelRepository fileChannelRepositoryInstance;
    //repository(Message)
    private static MessageRepository jcfMessageRepositoryInstance;
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
    public synchronized UserService  createJCFUserService() {
        if(jcfUserServiceInstance == null){
            jcfUserServiceInstance = new JCFUserService(userValidatorInstance);
        }
        return jcfUserServiceInstance;
    }

    @Override
    public synchronized UserService  createFileUserService() {
        if(fileUserServiceInstance == null){
            fileUserServiceInstance = new FileUserService(userValidatorInstance);

        }

        return fileUserServiceInstance;
    }

    @Override
    public synchronized UserService createBasicUserService(){
        if(basicUserServiceInstance ==null){
            basicUserServiceInstance = new BasicUserService(fileUserRepositoryInstance, userValidatorInstance);
        }
        return basicUserServiceInstance;
    }


    //채널 서비스
    @Override
    public synchronized ChannelService createJCFChannelService() {
        if(JCFChannelServiceInstance == null){
            JCFChannelServiceInstance
                    = new JCFChannelService(createObserverManager(),channelValidatorInstance);
        }
        return JCFChannelServiceInstance;
    }

    @Override
    public synchronized ChannelService createFileChannelService() {
        if(fileChannelServiceInstance == null){
            fileChannelServiceInstance
                    = new FileChannelService(createObserverManager(),channelValidatorInstance);
        }
        return fileChannelServiceInstance ;
    }

    @Override
    public synchronized ChannelService createBasicChannelService() {
        if(basicChannelServiceInstance ==null){
            basicChannelServiceInstance = new BasicChannelService(createObserverManager(),channelValidatorInstance, fileChannelRepositoryInstance );
        }
        return basicChannelServiceInstance;
    }

    //메시지 서비스
    @Override
    public synchronized MessageService createJCFMessageService() {
        if(JCFMessageServiceInstance == null){
            JCFMessageServiceInstance = new JCFMessageService(messageValidatorInstance);
        }
        return JCFMessageServiceInstance;

    }

    @Override
    public synchronized MessageService createFileMessageService() {
        if(fileMessageServiceInstance == null){
            fileMessageServiceInstance = new FileMessageService(messageValidatorInstance);
        }
        return fileMessageServiceInstance;

    }

    @Override
    public synchronized MessageService createBasicMessageService(){
        if(basicMessageServiceInstance == null){
            basicMessageServiceInstance = new BasicMessageService(messageValidatorInstance,fileMessageRepositoryInstance);
        }
        return basicMessageServiceInstance;
    }



    //observer
    @Override
    public synchronized Observer createChannelObserver() {
        if ( channelObserverInstance == null) {
            if(JCFMessageServiceInstance == null && fileMessageServiceInstance == null){
                channelObserverInstance = new ChannelObserver(basicMessageServiceInstance);
                observerManagerInstance.addObserver(channelObserverInstance);
            }else if(JCFMessageServiceInstance == null){
                channelObserverInstance = new ChannelObserver(fileMessageServiceInstance);
                observerManagerInstance.addObserver(channelObserverInstance);
            }else{
                channelObserverInstance = new ChannelObserver(JCFMessageServiceInstance);
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
    public synchronized UserRepository createJCFUserRepository(){
        if(jcfUserRepositoryInstance == null){
            jcfUserRepositoryInstance = new JCFUserRepository();
        }
        return jcfUserRepositoryInstance;
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
    public synchronized ChannelRepository createJCFChannelRepository(){
        if(jcfChannelRepositoryInstance == null){
            jcfChannelRepositoryInstance = new JCFChannelRepository();
        }
        return jcfChannelRepositoryInstance;
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
    public synchronized MessageRepository createJCFMessageRepository(){
        if(jcfMessageRepositoryInstance == null){
            jcfMessageRepositoryInstance = new JCFMessageReplsitory();
        }
        return jcfMessageRepositoryInstance;
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
            if (jcfUserServiceInstance == null && fileChannelServiceInstance == null) {
                messageValidatorInstance = new MessageValidator(basicChannelServiceInstance, basicUserServiceInstance);
            } else if(jcfUserServiceInstance == null){
                messageValidatorInstance = new MessageValidator(fileChannelServiceInstance, fileUserServiceInstance);
            } else{
                messageValidatorInstance = new MessageValidator(JCFChannelServiceInstance, jcfUserServiceInstance);
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