package com.lm.notificationcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView=findViewById(R.id.text);

        final NotificationCounterV3 notificationCounter=NotificationCounterV3.getInstance();

        for(int i=0;i<10;i++){
            final int j=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    notificationCounter.register(j+"",j);
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                notificationCounter.addObserver(new NObserver(new String[]{"6"}) {
                    @Override
                    public void update(final int count) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.append("tag6:"+count+"\n");
                            }
                        });
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                notificationCounter.addObserver(new NObserver(new String[]{"9"}) {
                    @Override
                    public void update(final int count) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.append("tag9:"+count+"\n");
                            }
                        });
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                notificationCounter.addGroupObserver(new NObserver(new String[]{"0", "9"}) {
                    @Override
                    public void update(final int count) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.append("tag0,9:"+count+"\n");
                            }
                        });
                    }
                });
            }
        }).start();

        for(int i=0;i<10;i++){
            if(i%2==0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        notificationCounter.minusOne("6");
                        notificationCounter.minusOne("9");
                    }
                }).start();
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        notificationCounter.plusOne("0");
                        notificationCounter.plusOne("6");
                        notificationCounter.plusOne("9");
                    }
                }).start();
            }
        }
    }
}
