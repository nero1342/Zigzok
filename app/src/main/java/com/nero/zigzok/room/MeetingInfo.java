package com.nero.zigzok.room;

public class MeetingInfo {

    private MeetingInfo() {

    }

    private static MeetingInfo instance = new MeetingInfo();

    // Getter-Setters
    public static MeetingInfo getInstance() {
        return instance;
    }

    public static void setInstance(MeetingInfo instance) {
        MeetingInfo.instance = instance;
    }

    private String meetingId;
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
}
