package com.danktankapps.coontown.mysqlpractice;

/**
 * Created by coontown on 8/11/15.
 */
public class User {

    String name, username, password;
    int age;

    public User(String nm, int ag, String usrnm, String psswrd){
        this.name = nm;
        this.age = ag;
        this.username = usrnm;
        this.password = psswrd;
    }

    public User(String usrnm, String psswrd ){
        this("", -1, usrnm, psswrd);
    }
}
