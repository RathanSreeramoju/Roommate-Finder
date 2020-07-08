package com.example.roommatefinder.ui.post;

public class Post {
    private String postKey;
    private String txt_title;
    private String txt_desc;
    private String txt_loc;
    private String txt_roommates;
    private String txt_gender;
    private String txt_contact;
    private String userPhoto;
    private String picture;

    public Post(String txt_title, String txt_desc, String txt_roommates, String txt_loc, String txt_gender, String txt_contact,String userPhoto,
                String picture,String postKey) {
        this.txt_title = txt_title;
        this.txt_desc = txt_desc;
        this.txt_loc = txt_roommates;
        this.txt_roommates = txt_loc;
        this.txt_gender = txt_gender;
        this.txt_contact = txt_contact;
        this.userPhoto = userPhoto;
        this.picture = picture;
        this.postKey = postKey;
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }



    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getPicture() {
        return picture;
    }

    public String getTxt_title() {
        return txt_title;
    }

    public String getTxt_desc() {
        return txt_desc;
    }

    public String getTxt_loc() {
        return txt_loc;
    }

    public String getTxt_roommates() {
        return txt_roommates;
    }

    public String getTxt_gender() {
        return txt_gender;
    }

    public String getTxt_contact() {
        return txt_contact;
    }

    public void setTxt_title(String txt_title) {
        this.txt_title = txt_title;
    }

    public void setTxt_desc(String txt_desc) {
        this.txt_desc = txt_desc;
    }

    public void setTxt_loc(String txt_loc) {
        this.txt_loc = txt_loc;
    }

    public void setTxt_roommates(String txt_roommates) {
        this.txt_roommates = txt_roommates;
    }

    public void setTxt_gender(String txt_gender) {
        this.txt_gender = txt_gender;
    }

    public void setTxt_contact(String txt_contact) {
        this.txt_contact = txt_contact;
    }
}