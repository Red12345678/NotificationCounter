package com.lm.notificationcounter;

import java.util.Observable;

/**
 * Created by 10528 on 2018/4/8.
 */
public class NCounter {
    int mCounter;
    Observable mObservable;

    public NCounter(int counter){
        mCounter=counter;
        mObservable=new NObservable();
    }
}
