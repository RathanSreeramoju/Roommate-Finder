package com.example.roommatefinder.Activty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roommatefinder.Handler.ConnectivityReceiver;
import com.example.roommatefinder.Handler.MyApp;
import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.singletone.ProgressDialog;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity implements ConnectivityReceiver.connectivityReceiverListener {

    private ImageView home;
    private TextView  name,no_internet;
    private Animation top,bottom,expected_anim;
    private Context context;
    private FirebaseAuth mAuth;
    private MySharedPref sharedPref;
    private ConnectivityReceiver changeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        init();

        checkInternetConnection();

    }

    private void checkInternetConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        changeActivity(isConnected);

    }

    private void changeActivity(boolean isConnected) {
        //initi
        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        sharedPref.getUserLoggedInData();
        System.out.println("userId is__"+sharedPref.UserId);

        System.out.println("user logged in_"+sharedPref.loggedin);

        System.out.println("user email solashscreen_"+sharedPref.Email);



        if(isConnected) {
            System.out.println("logged in splashscreen__"+sharedPref.loggedin);
            if(sharedPref.loggedin){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                        finish();//api 21 requires else 17
                    }
                },2800);

             }else {
                emailVerified();
                   }

            if(no_internet.getVisibility()== View.VISIBLE){
                no_internet.startAnimation(expected_anim);
                no_internet.setVisibility(View.GONE);
            }else {
                no_internet.setVisibility(View.GONE);
            }

        }else {
            no_internet.setVisibility(View.VISIBLE);
            no_internet.startAnimation(bottom);
            System.out.println("offline mode.");
        }
    }

    private void emailVerified() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        }, 2800);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register intent filter
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
       changeReceiver = new ConnectivityReceiver();
        registerReceiver(changeReceiver,filter);
        System.out.println("resume is ");
        //register connection status listener
        MyApp.getInstance().setConnectivityListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(changeReceiver);
    }

    private void init() {

        home = findViewById(R.id.imageView4);
        name = findViewById(R.id.name);
        no_internet = findViewById(R.id.no_internet);
        //for animation initialization
        top = AnimationUtils.loadAnimation(context,R.anim.top_anim);
        bottom = AnimationUtils.loadAnimation(context,R.anim.bottom_anim);
        expected_anim = AnimationUtils.loadAnimation(context,R.anim.current_position_to_expected_position);

        //setAnim

        home.startAnimation(top);
        name.startAnimation(bottom);


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        changeActivity(isConnected);
    }
}