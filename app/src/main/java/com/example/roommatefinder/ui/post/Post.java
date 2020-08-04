package com.example.roommatefinder.ui.post;

public class Post {
    private String postKey;
    private String txt_title;
    private String txt_desc;

    private String txt_roommates;
    private String txt_gender;
    private String txt_loc;
    private String txt_contact;
    private String userPhoto;


    public Post(){

    }

    public Post(String postKey, String txt_title, String txt_desc, String txt_roommates, String txt_gender, String txt_loc, String txt_contact, String userPhoto) {
        this.postKey = postKey;
        this.txt_title = txt_title;
        this.txt_desc = txt_desc;
        this.txt_roommates = txt_roommates;
        this.txt_gender = txt_gender;
        this.txt_loc = txt_loc;
        this.txt_contact = txt_contact;
        this.userPhoto = userPhoto;
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

    public String getTxt_roommates() {
        return txt_roommates;
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