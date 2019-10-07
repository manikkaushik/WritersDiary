package com.tenovaters.android.writer.Database;

import android.media.Image;

public class ReadersList {

    String title;
    String authorname;
    String category;
    String story;
    String image;
    String id;
    String likes;
    String views;

    public ReadersList(){

    }
    public ReadersList(String Title,String AuthorName,String Category,String Story,String Image,String Id,String Likes,String Views,String Favourites) {
        this.title = Title;
        this.authorname=AuthorName;
        this.category=Category;
        this.story=Story;
        this.image=Image;
        this.id=Id;
        this.likes=Likes;
        this.views=Views;
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
    public String getId(){
        return id;
    }
    public String getLikes(){
        return likes;
    }
    public String getViews(){
        return views;
    }

}
