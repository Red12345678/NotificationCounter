package com.lm.notificationcounter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by 10528 on 2018/4/8.
 */
public class NotificationCounterV3 {
    private static NotificationCounterV3 sInstance;
    private Map<String,NCounter> mMap;
    //保存還未註冊的觀察者
    private LinkedList<NObserver> mPendingObservers;
    //保存還未執行的操作(減/加)
    private LinkedList<Operator> mPendingOperators;

    public static synchronized NotificationCounterV3 getInstance() {
        if (sInstance == null) {
            sInstance = new NotificationCounterV3();
        }
        return sInstance;
    }

    private NotificationCounterV3() {
        mMap=new HashMap<>();
        mPendingObservers = new LinkedList<>();
        mPendingOperators = new LinkedList<>();
    }

    public synchronized void register(String tag, int num) {
        if(mMap.containsKey(tag)){
            NCounter nCounter=mMap.get(tag);
            nCounter.mCounter=num;
            nCounter.mObservable.notifyObservers(new Message(tag,nCounter.mCounter));
        }else {
            mMap.put(tag, new NCounter(num));
            for (Iterator<NObserver> iterator = mPendingObservers.iterator(); iterator.hasNext(); ) {
                NObserver observer = iterator.next();
                boolean flag = true;
                for (String t : observer.mTags) {
                    if (!mMap.containsKey(t)) {
                        flag = false;
                        break;
                    }
                }
                //所有通知都註冊完添加觀察者
                if (flag) {
                    for (String t : observer.mTags) {
                        mMap.get(t).mObservable.addObserver(observer);
                        //註冊完成后更新一次，保證數據準確性
                        //0 1
                        //6 -1
                        //6 1
                        //6 -1
                        //6 -1
                        //0 1
                        //6 1
                        //0 1
                        //6 1
                        //0 1
                        //6 1
                        //0 1
                        //6 1
                        //0registered
                        //9registered
                        //9 -1
                        //98
                        //9 1
                        //99
                        //9 -1
                        //98
                        //9 -1
                        //97
                        //9 1
                        //98
                        //9 1
                        //99
                        //9 1
                        //910
                        //9 1
                        //911
                        //6 -1
                        //9 -1
                        //910
                        //6 -1
                        //9 -1
                        //99
                        observer.update(null, new Message(t, mMap.get(t).mCounter));
                        System.out.println(t + "registered");
                    }
                    iterator.remove();
                }
            }

            for (Iterator<Operator> iterator = mPendingOperators.iterator(); iterator.hasNext(); ) {
                Operator operator = iterator.next();
                if (tag.equals(operator.mTag)) {
                    //運算
                    operate(operator.mTag, operator.mNum);
                    iterator.remove();
                }
            }
        }
    }

    public synchronized void operate(String tag, int num) {
        if (mMap.get(tag) == null) {
            mPendingOperators.add(new Operator(tag, num));
        } else {
            NCounter counter=mMap.get(tag);
            counter.mCounter+=num;
            System.out.println(tag+" "+ num);
            counter.mObservable.notifyObservers(new Message(tag,counter.mCounter));
        }
    }

    public void plusOne(String tag){
        operate(tag,1);
    }

    public void minusOne(String tag){
        operate(tag,-1);
    }

    /**
     * 監聽單個通知
     * @param observer 觀察者
     */
    public synchronized void addObserver(NObserver observer){
        if(mMap.containsKey(observer.mTags[0])){
            NCounter counter=mMap.get(observer.mTags[0]);
            counter.mObservable.addObserver(observer);
            observer.update(null,new Message(observer.mTags[0],mMap.get(observer.mTags[0]).mCounter));
            System.out.println(observer.mTags[0]+"registered");
        }else{
            mPendingObservers.add(observer);
        }
    }

    /**
     * 監聽多個通知
     * @param observer 觀察者
     */
    public synchronized void addGroupObserver(NObserver observer){
        boolean flag=true;
        for(String t : observer.mTags){
            if(!mMap.containsKey(t)){
                flag=false;
                break;
            }
        }
        if(flag){
            for(String t : observer.mTags){
                NCounter counter=mMap.get(t);
                counter.mObservable.addObserver(observer);
                observer.update(null,new Message(t,mMap.get(t).mCounter));
                System.out.println(t+"registered");
            }
        }else{
            mPendingObservers.add(observer);
        }
    }

    public synchronized void removeObserver(NObserver observer){
        for(String t : observer.mTags){
            if(mMap.containsKey(t)){
                mMap.get(t).mObservable.deleteObserver(observer);
            }
        }
        mPendingObservers.remove(observer);
    }

    public synchronized int getCount(String tag){
        if(mMap.containsKey(tag)){
            return mMap.get(tag).mCounter;
        }else{
            return -1;
        }
    }
}
