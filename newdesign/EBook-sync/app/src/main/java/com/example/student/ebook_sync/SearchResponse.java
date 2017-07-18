package com.example.student.ebook_sync;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

import javax.xml.transform.Result;

public class SearchResponse {

    @SerializedName("id")
    public int uid;

    @SerializedName("name")
    public String uname;

    @SerializedName("password")
    public String password;

    @SerializedName("bookId")
    public int bookId;

    @SerializedName("items")
    public List<Item> items;

    public String query;
}
