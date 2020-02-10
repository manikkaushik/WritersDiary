package com.tenovaters.android.writer.Database;

public class NotificationList {
    String authorid;
    String date;
    String notiferid;
    String storyid;
    String title;

    public NotificationList() {
    }

    public NotificationList(String Title, String Storyid, String NotiferId, String Date, String AuthorId) {
        this.storyid = Storyid;
        this.notiferid = NotiferId;
        this.date = Date;
        this.title = Title;
        this.authorid = AuthorId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStoryid() {
        return this.storyid;
    }

    public String getNotiferid() {
        return this.notiferid;
    }

    public String getDate() {
        return this.date;
    }

    public String getAuthorid() {
        return this.authorid;
    }
}
