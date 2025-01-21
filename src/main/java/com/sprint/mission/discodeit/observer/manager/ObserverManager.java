package com.sprint.mission.discodeit.observer.manager;

import com.sprint.mission.discodeit.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ObserverManager {
    private final List<Observer> observers = new ArrayList<>();


    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void channelDeletionEvent(UUID uuid) {
        for (Observer observer : observers) {
            observer.update(uuid);
        }
    }
}
