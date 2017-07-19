package com.example.student.ebook_sync;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Book implements Serializable{
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("location")
    public String location;

    @SerializedName("line")
    public double line;

    @SerializedName("exist")
    boolean exists;

    @Override
    public String toString(){
        return new StringBuilder()
                .append("\n   BookID: " + id + "\n")
                .append("   BookName: " + name + "\n")
                .append("   BookLine: " + line + "\n").toString();
    }

    Book(int id, String name, double line, boolean exists){
        this.id = id;
        this.name = name;
        this.line = line;
        this.exists = exists;
        this.location = "";
    }

    Book(int id, String name, double line){
        this.id = id;
        this.name = name;
        this.line = line;
        this.exists = false; //May be replaced with an auto-check for existence
        this.location = "";
    }
}
