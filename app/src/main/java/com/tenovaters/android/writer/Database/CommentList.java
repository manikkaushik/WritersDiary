package com.tenovaters.android.writer.Database;

public class CommentList {
    String comment;
    String date;
    String id;

    public CommentList() {
    }

    public CommentList(String Comment, String Id, String Date) {
        this.id = Id;
        this.date = Date;
        this.comment = Comment;
    }

    public String getComment() {
        return this.comment;
    }

    public String getId() {
        return this.id;
    }

    public String getDate() {
        return this.date;
    }
}
