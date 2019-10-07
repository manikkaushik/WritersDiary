package com.tenovaters.android.writer.Database;

public class UserList {
        String name;
        String userID;
        String age;
        String image;

        public UserList(){

        }
        public UserList(String Name,String UserID,String Age) {
            this.name = Name;
            this.userID=UserID;
            this.age=Age;
        }

        public UserList(String Name,String UserID,String Age,String Image) {
            this.name = Name;
            this.userID=UserID;
            this.age=Age;
            this.image= Image;

        }
        public String getName() {
            return name;
        }
        public String getUserID() {
            return userID;
        }
        public String getAge(){
            return age;
        }
        public String getImage(){
            return image;
        }

}
