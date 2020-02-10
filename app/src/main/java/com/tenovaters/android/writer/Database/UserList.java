package com.tenovaters.android.writer.Database;

public class UserList {
    String age;
    String dateofbirth;
    String description;
    String gender;
    String image;
    String location;
    String name;
    String userID;

    public UserList() {
    }

    public UserList(String Name, String UserID, String Age, String Gender) {
        this.name = Name;
        this.userID = UserID;
        this.age = Age;
        this.gender = Gender;
    }

    public UserList(String Name, String UserID, String Age, String Gender, String Description, String DateofBirth, String Location) {
        this.name = Name;
        this.userID = UserID;
        this.age = Age;
        this.gender = Gender;
        this.description = Description;
        this.dateofbirth = DateofBirth;
        this.location = Location;
    }

    public UserList(String Name, String UserID, String Age, String Image, String Gender, String Description, String DateofBirth, String Location) {
        this.name = Name;
        this.userID = UserID;
        this.image = Image;
        this.age = Age;
        this.gender = Gender;
        this.description = Description;
        this.dateofbirth = DateofBirth;
        this.location = Location;
    }

    public UserList(String Name, String UserID, String Age, String Image, String Gender) {
        this.name = Name;
        this.userID = UserID;
        this.age = Age;
        this.image = Image;
        this.gender = Gender;
    }

    public String getName() {
        return this.name;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getAge() {
        return this.age;
    }

    public String getImage() {
        return this.image;
    }

    public String getGender() {
        return this.gender;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDateofbirth() {
        return this.dateofbirth;
    }

    public String getLocation() {
        return this.location;
    }
}
