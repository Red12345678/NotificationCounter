package com.lm.notificationcounter;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by 10528 on 2018/4/8.
 */
public abstract class NObserver implements Observer {
    String[] mTags;
    private Map<String,Integer> mMap;

    public NObserver(String[] tags){
        mTags=tags;
        mMap=new HashMap<>();
    }
    @Override
    public void update(Observable o, Object arg) {
        synchronized (this){
            int count=0;
            Message msg=(Message)arg;
            mMap.put(msg.mTag,msg.mCount);
            for(Map.Entry<String,Integer> entry : mMap.entrySet()){
                count+=entry.getValue();
            }
            update(count);
        }
    }

    public abstract void update(int count);
}
