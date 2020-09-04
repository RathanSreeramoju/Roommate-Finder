package com.example.roommatefinder.ui.favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavouriteViewModel  {
    private String postKey;
    private String txt_title;
    private String txt_desc;

    private String txt_Price;
    private String txt_gender;
    private String txt_loc;
    private String txt_contact;
    private String userPhoto;

    public FavouriteViewModel(){

    }

    public String getPostKey() {
        return postKey;
    }

    public String getTxt_title() {
        return txt_title;
    }

    public String getTxt_desc() {
        return txt_desc;
    }

    public String getTxt_Price() {
        return txt_Price;
    }

    public String getTxt_gender() {
        return txt_gender;
    }

    public String getTxt_loc() {
        return txt_loc;
    }

    public String getTxt_contact() {
        return txt_contact;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
}
