package com.example.roommatefinder.singletone;

import android.content.Context;
import android.content.SharedPreferences;

import static com.squareup.okhttp.internal.Internal.instance;

public class MySharedPref {
    public String LOGGED_IN = "loggedIn";
    public String UserId    = "userId";
    public  String  Email = "email";
    public String  ImageUrl = "imgUrl";
    public   String AppLogIn = "AppLogIn";

    private Context context;


    private MySharedPref instance;



    //save data in local database.
    private SharedPreferences sharedPreferences;

    private  SharedPreferences.Editor editor;
    public boolean loggedin=false;

    public MySharedPref(Context context){

        this.context = context;
        sharedPreferences = context.getSharedPreferences(AppLogIn, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



    }


    public  MySharedPref getInstance(Context context){
        if(instance == null)
            instance = new MySharedPref(context);

        return instance;

    }

    public void setUserId(String userId) {
        editor.putString(UserId, userId);
        editor.commit();
    }

    public void setAppLogIn(boolean log) {
        editor.putBoolean(LOGGED_IN,log);
        editor.commit();

    }

    public void setEmailID(String EmailId) {
        editor.putString(Email, EmailId);
        editor.commit();
    }



    public void setImageUrl(String url) {
        editor.putString(ImageUrl, url);
        editor.apply();
        editor.commit();
    }

    //fetch logged in user data
    public  void getUserLoggedInData() {

        UserId   = sharedPreferences.getString(UserId, null);
        loggedin = sharedPreferences.getBoolean(LOGGED_IN, false);
        Email    = sharedPreferences.getString(Email,null);
        ImageUrl = sharedPreferences.getString(ImageUrl,null);

    }
    //clear insta logged in user data
    public void clearInstaData() {
        editor.putBoolean(LOGGED_IN, false);
        editor.remove(LOGGED_IN);
        editor.apply();
        editor.commit();
        //editor.clear ().commit();//delete all the data avilabe in sharedPref
    }

}
