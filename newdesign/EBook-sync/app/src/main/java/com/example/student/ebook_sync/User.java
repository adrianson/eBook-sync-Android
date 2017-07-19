package com.example.student.ebook_sync;



import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("password")
    public String password;

    @SerializedName("bookId")
    public int activeBookId;

    @SerializedName("items")
    public List<Book> books;

    User(String n, String p){
        this.name = n;
        this.password = p;
    }

    public void setID(int id){
        if(id == (int)id)
            this.id = id;
    }

    public void setActiveBookId(int id){
        if(id == (int)id)
            this.activeBookId = id;
    }

    public void setBooks(List<Book> books){
        if(books instanceof  List)
            this.books = books;
        else this.books = new ArrayList<Book>();
    }
}
