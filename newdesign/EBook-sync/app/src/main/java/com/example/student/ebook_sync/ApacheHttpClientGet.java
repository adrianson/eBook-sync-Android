package com.example.student.ebook_sync;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;


public class ApacheHttpClientGet {
    //final static String url = "http://localhost:5000/";
    /*final static String url = "http://spring-boot-jpa-oracle-example-dev.eu-central-1.elasticbeanstalk.com/";
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static User searchUser(User user) throws IOException, JSONException {
        String request = "searchUser?name=";
        String fullUrl = url + request + user.getName();
        InputStream is = new URL(fullUrl).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            Gson gson=new Gson();
            User[] user2 = gson.fromJson(jsonText,User[].class);
            if (user2.length >  0)
                return user2[0];
            else
                return null;
        } finally {
            is.close();
        }
    }
    public static Book searchBook(Book book) throws IOException, JSONException {
        String request = "searchUser?name=";
        String fullUrl = url + request + book.getName();
        InputStream is = new URL(fullUrl).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            Gson gson=new Gson();
            Book[] book2 = gson.fromJson(jsonText,Book[].class);
            if (book2.length >  0)
                return book2[0];
            else
                return null;
        } finally {
            is.close();
        }
    }

    public static void deleteUser(User user) throws IOException, JSONException {
        String request = "deleteUser?id=";
        String fullUrl = url + request + user.getId();
        System.out.println(fullUrl);
        new URL(fullUrl).openStream();
    }
    public static void deleteBook(Book book) throws IOException, JSONException {
        String request = "deleteBook?id=";
        String fullUrl = url + request + book.getId();
        System.out.println(fullUrl);
        new URL(fullUrl).openStream();
    }


    public static User inserUser(User user) throws IOException, JSONException {
        String request = "insertUser?name=";
        String fullUrl = url + request + user.getName();
        System.out.println(fullUrl);
        InputStream is = new URL(fullUrl).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText2 = readAll(rd);
            Gson gson=new Gson();
            User user2 = gson.fromJson(jsonText2,User.class);
            return user2;
        } finally {
            is.close();
        }
    }
    public static Book inserBook(Book book, User user) throws IOException, JSONException {
        String request = "insertBook?name=";
        String request2 = "&user=";
        String fullUrl = url + request + book.getName() + request2 + user.getId();
        System.out.println(fullUrl);
        InputStream is = new URL(fullUrl).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText2 = readAll(rd);
            Gson gson=new Gson();
            Book book2 = gson.fromJson(jsonText2,Book.class);
            return book2;
        } finally {
            is.close();
        }
    }


    public static Book updateBook(Book book, User user) throws IOException, JSONException {
        String request = "insertUser?line=";
        String request2 = "&user=";
        String request3 = "&name=";
        String request4 = "&id=";
        String fullUrl = url + request + book.getLine() + request2 + user.getId()+request3 + book.getName()+request4 + book.getId();
        System.out.println(fullUrl);
        InputStream is = new URL(fullUrl).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText2 = readAll(rd);
            Gson gson=new Gson();
            Book book2 = gson.fromJson(jsonText2,Book.class);
            return book2;
        } finally {
            is.close();
        }
    }
    public static User updateUser(User user) throws IOException, JSONException {
        String request = "insertUser?book=";
        String request1 = "&user=";
        String request2 = "&name=";
        String fullUrl = url + request + user.getBookId() + request1 + user.getId() + request2 + user.getName();
        System.out.println(fullUrl);
        InputStream is = new URL(fullUrl).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText2 = readAll(rd);
            Gson gson=new Gson();
            User user2 = gson.fromJson(jsonText2,User.class);
            return user2;
        } finally {
            is.close();
        }
    }
    */
}