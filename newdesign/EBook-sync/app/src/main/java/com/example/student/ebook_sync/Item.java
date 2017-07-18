package com.example.student.ebook_sync;


import com.google.gson.annotations.SerializedName;


public class Item {
    @SerializedName("id")
    public int bid;

    @SerializedName("name")
    public String bname;

    @SerializedName("location")
    public String location;

    @SerializedName("line")
    public int line;

    @SerializedName("exist")
    boolean exist;

    @Override
    public String toString(){
        return new StringBuilder()
                .append("\n   BookID: " + bid + "\n")
                .append("   BookName: " + bname + "\n")
                .append("   BookLine: " + line + "\n").toString();
    }
}
