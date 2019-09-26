package com.tenovaters.android.writer.Database;

public class ReadersList {

    String title;
    String authorname;
    String category;
    String story;

    public ReadersList(){

    }
    public ReadersList(String Title,String AuthorName,String Category,String Story) {
        this.title = Title;
        this.authorname=AuthorName;
        this.category=Category;
        this.story=Story;
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

}
