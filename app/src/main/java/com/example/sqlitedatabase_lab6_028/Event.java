package com.example.sqlitedatabase_lab6_028;

public class Event {
    String key = "";
    String name = "";
    String email = "";
    String phoneHome = "";
    String phoneOffice = "";


    public Event(String key, String name, String email, String phoneHome,String phoneOffice){
        this.key = key;
        this.name = name;
        this.email = email;
        this.phoneHome = phoneHome;
        this.phoneOffice = phoneOffice;
    }
}
