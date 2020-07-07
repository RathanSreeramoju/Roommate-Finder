package com.example.roommatefinder;

public class Posts {
    private String postTitle;
    private String postDescription;
    private String sex;
    private String numberOfRoommates;
    private String location;

    public Posts(){

    }

    public Posts(String postTitle, String postDescription, String sex, String numberOfRoommates, String location) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.sex = sex;
        this.numberOfRoommates = numberOfRoommates;
        this.location = location;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNumberOfRoommates() {
        return numberOfRoommates;
    }

    public void setNumberOfRoommates(String numberOfRoommates) {
        this.numberOfRoommates = numberOfRoommates;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
