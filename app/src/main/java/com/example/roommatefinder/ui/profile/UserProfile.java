package com.example.roommatefinder.ui.profile;

public class UserProfile {
    private String date;
    private String event_title;
    private String event_description;

    public UserProfile(){

    }

    public UserProfile(String date, String event_title, String event_description) {
        this.date = date;
        this.event_title = event_title;
        this.event_description = event_description;
    }

    public String getDate() {
        return date;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getEvent_description() {
        return event_description;
    }
}
