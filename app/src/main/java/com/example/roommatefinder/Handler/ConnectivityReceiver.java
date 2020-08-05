package com.example.roommatefinder.Handler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static connectivityReceiverListener connectivityReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo!=null&&networkInfo.isConnectedOrConnecting();
        if(connectivityReceiverListener!=null){

            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }
    //to check manual testing
    public static boolean isConnected(){

        System.out.println("null pointer__"+MyApp.getInstance()+"application__"+MyApp.getInstance().getApplicationContext());
        ConnectivityManager connectivityManager = (ConnectivityManager)  MyApp.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();

        return networkInfo!=null&&networkInfo.isConnectedOrConnecting();
    }

    //create an interface

    public interface connectivityReceiverListener{
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
