package com.example.roommatefinder.Handler;

import android.app.Application;

public class MyApp extends Application {

    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        System.out.println("my app___"+mInstance);
    }

    public static synchronized MyApp getInstance() {
        System.out.println("instance__"+new MyApp());
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.connectivityReceiverListener listener){
//        Toast.makeText(mInstance, "when offline to online", Toast.LENGTH_SHORT).show();
        ConnectivityReceiver.connectivityReceiverListener=listener;


    }

}
