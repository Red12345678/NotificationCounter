package com.lm.notificationcounter;

import java.util.Observable;

/**
 * Created by 10528 on 2018/3/8.
 */

public class NObservable extends Observable {
    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
