package com.tenovaters.android.writer.Database;

public class ReadersList {
    String authorid;
    String authorname;
    String category;
    String comment;
    String description;
    String genere;

    /* renamed from: id */
    String id;
    String image;
    String likes;
    String status;
    String story;
    String title;
    String views;

    public ReadersList() {
    }

    public ReadersList(String Title, String Description, String Category, String Image, String Id, String Authorid, String Genere, String Status) {
        this.title = Title;
        this.category = Category;
        this.description = Description;
        this.authorid = Authorid;
        this.genere = Genere;
        this.image = Image;
        this.status = Status;
        this.id = Id;
    }

    public ReadersList(String Title, String Description, String Category, String Image, String Id, String Authorid, String Genere, String Status, String Story) {
        this.title = Title;
        this.category = Category;
        this.description = Description;
        this.authorid = Authorid;
        this.genere = Genere;
        this.image = Image;
        this.status = Status;
        this.id = Id;
        this.story = Story;
    }

    public ReadersList(String Title, String AuthorName, String Description, String Category, String Story, String Image, String Id, String Likes, String Views, String Comment, String Authorid, String Genere, String Status) {
        this.title = Title;
        this.authorname = AuthorName;
        this.category = Category;
        this.story = Story;
        this.image = Image;
        this.id = Id;
        this.likes = Likes;
        this.views = Views;
        this.comment = Comment;
        this.description = Description;
        this.authorid = Authorid;
        this.genere = Genere;
        this.status = Status;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthorname() {
        return this.authorname;
    }

    public String getCategory() {
        return this.category;
    }

    public String getStory() {
        return this.story;
    }

    public String getImage() {
        return this.image;
    }

    public String getId() {
        return this.id;
    }

    public String getLikes() {
        return this.likes;
    }

    public String getViews() {
        return this.views;
    }

    public String getComment() {
        return this.comment;
    }

    public String getDescription() {
        return this.description;
    }

    public String getAuthorid() {
        return this.authorid;
    }

    public String getGenere() {
        return this.genere;
    }

    public String getStatus() {
        return this.status;
    }
}
