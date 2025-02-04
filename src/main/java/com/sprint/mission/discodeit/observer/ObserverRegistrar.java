package com.sprint.mission.discodeit.observer;

import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import org.springframework.stereotype.Component;

@Component
public class ObserverRegistrar {

    private final Observer ChannelObserverService;
    private final ObserverManager observerManager;

    public ObserverRegistrar(Observer channelObserverService, ObserverManager observerManager){
        this.ChannelObserverService = channelObserverService;
        this.observerManager = observerManager;
        add();

    }

    private void add(){
        observerManager.addObserver(ChannelObserverService);
    }

}
