package com.tenovaters.android.writer.Database;

import android.media.Image;

public class ReadersList {

    String title;
    String authorname;
    String category;
    String story;
    String image;

    public ReadersList(){

    }
    public ReadersList(String Title,String AuthorName,String Category,String Story,String Image) {
        this.title = Title;
        this.authorname=AuthorName;
        this.category=Category;
        this.story=Story;
        this.image=Image;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthorname() {
        return authorname;
    }
    public String getCategory(){
        return category;
    }
    public String getStory(){
        return story;
    }
    public String getImage(){
        return image;
    }

}
