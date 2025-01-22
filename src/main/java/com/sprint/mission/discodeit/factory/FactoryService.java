package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.observer.ObserverService;
import com.sprint.mission.discodeit.validation.MessageValidator;

public class FactoryService implements Factory{
    private static UserService userServiceInstance;
    private static ChannelService channelServiceInstance;
    private static MessageService messageServiceInstance;
    private static MessageValidator messageValidatorInstance;
    private static ObserverManager observerManagerInstance;
    private static ObserverService observerServiceInstance;

    @Override
    public synchronized UserService  createUserService() {
        if(userServiceInstance == null){
            userServiceInstance = new JCFUserService();
        }
        return userServiceInstance;
    }

    @Override
    public synchronized ChannelService createChannelService() {
        if(channelServiceInstance == null){
            channelServiceInstance = new JCFChannelService(createObserverManager());
        }
        return channelServiceInstance;
    }
    //현재는 옵저버가 감지하는게 deleteChannel 메소드 작동만 감지해서 클라이언트에서 테스트시
    //일일이 객체 생성하기 번거로워서 일단 넣었습니다.
    // but: createMessageService() 메서드의 동작이 단순히 MessageService 객체 생성이 아니라 observerService,
    //      MessageValidator 객체를 생성하는 흐름이, 동작을 모르는 사람이 사용 시 혼동??을 줄 수 있지 않을까...요??
    //      만약 그렇다면 어떻게 하는게 좋을지..(-_-)...
    @Override
    public synchronized MessageService createMessageService() {
        if(messageValidatorInstance == null){
            messageServiceInstance = new JCFMessageService(createMessageValidator());
            observerServiceInstance = createObserverService();
        }
        return messageServiceInstance;

    }

    @Override
    public synchronized MessageValidator createMessageValidator(){
        if(messageValidatorInstance == null){
            messageValidatorInstance = new MessageValidator(createChannelService(), createUserService());
        }
        return messageValidatorInstance;
    }

    private synchronized ObserverService createObserverService() {
        if ( observerServiceInstance == null) {
            observerServiceInstance = new ObserverService(createMessageService());
            createObserverManager().addObserver(observerServiceInstance);
        }
        return observerServiceInstance;
    }

//    public synchronized ObserverService createObserverService() {
//        if ( observerServiceInstance == null) {
//            observerServiceInstance = new ObserverService(createMessageService());
//            createObserverManager().addObserver(observerServiceInstance);
//        }
//        return observerServiceInstance;
//    }

    private synchronized ObserverManager createObserverManager(){
        if(observerManagerInstance == null){
            observerManagerInstance = new ObserverManager();
        }
        return observerManagerInstance;
    }

//    public synchronized ObserverManager createObserverManager(){
//        if(observerManagerInstance == null){
//            observerManagerInstance = new ObserverManager();
//        }
//        return observerManagerInstance;
//    }

}