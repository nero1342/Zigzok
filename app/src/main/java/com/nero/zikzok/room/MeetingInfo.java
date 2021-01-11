package com.nero.zikzok.room;

public class MeetingInfo {

    private MeetingInfo() {

    }

    private static MeetingInfo instance = new MeetingInfo();

    // Getter-Setters
    public static MeetingInfo getInstance() {
        return instance;
    }

    private String user_id;     // only exists when creating room
    private String meetingId;
    private String username;
    private String password;
    private String zak;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZak() {
        return zak;
    }

    public void setZak(String zak) {
        this.zak = zak;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
