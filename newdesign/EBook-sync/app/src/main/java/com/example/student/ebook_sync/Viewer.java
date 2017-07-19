package com.example.student.ebook_sync;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.skytree.epub.Caret;
import com.skytree.epub.Highlight;
import com.skytree.epub.HighlightListener;
import com.skytree.epub.Highlights;
import com.skytree.epub.NavPoint;
import com.skytree.epub.NavPoints;
import com.skytree.epub.PageInformation;
import com.skytree.epub.PageMovedListener;
import com.skytree.epub.PageTransition;
import com.skytree.epub.ReflowableControl;
import com.skytree.epub.SearchListener;
import com.skytree.epub.SearchResult;
import com.skytree.epub.SelectionListener;
import com.skytree.epub.Setting;
import com.skytree.epub.SkyProvider;
import com.skytree.epub.State;
import com.skytree.epub.StateListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import 	java.util.zip.ZipInputStream;
import java.io.BufferedInputStream;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import 	java.util.zip.ZipEntry;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.ViewFlipper;


public class Viewer extends Activity {

    final Context context = this;
    //TextView debugTextView;
    User user;
    String url = "http://spring-boot-jpa-oracle-example-dev.eu-central-1.elasticbeanstalk.com";
    String urlrequest = "/searchUser?name=";
    String username = "user1";
    String password = "password1";
    RequestQueue requestQueue;
    Gson gson;
    static double page_position;
    static boolean reading = true; // false->home(list of books)
    boolean sceneIsVisible = true; // false->filesView
    ReflowableControl rv;       // ReflowableControl
    RelativeLayout ePubView, homeView;    // Basic View of Activity.
    ViewFlipper scene;
    ListView listView, filesView;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    Button markButton;
    Toolbar toolbar;
    List<String> booksList;
    List<Double> booksPagePositions;
    List<Book> booksListDat;
    int activeBookId;
    final private String TAG = "EPub";
    Highlights highlights;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    protected void makeViewerLayout() {
        highlights = new Highlights();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;

        ePubView = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        ePubView.setLayoutParams(rlp);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height

        rv = new ReflowableControl(context);
       // rv.setBaseDirectory("storage/emulated/0");
        String baseDirectory = getFilesDir().getAbsolutePath() + "/books/";
        Log.i("basefile", baseDirectory);

        rv.setBaseDirectory(baseDirectory);
        //copyToDevice(fileName);
        try {
            rv.setBookName(getBookNameByID(user.activeBookId, booksListDat));
        } catch (NullPointerException e) {
            Toast toast = Toast.makeText(context, "NPException in rv.setBookName(getBookNameByID(user.activeBookId, booksListDat))",
                    Toast.LENGTH_LONG);
            toast.show();
            rv.setBookName(getBookNameByID(0, booksListDat));
        }
//        rv.setLicenseKey("0000-0000-0000-0000");
        rv.setDoublePagedForLandscape(true);
        rv.setFont("TimesRoman", 20);
        rv.setLineSpacing(135); // the value is supposed to be percent(%).
        rv.setHorizontalGapRatio(0.25);
        rv.setVerticalGapRatio(0.1);
        rv.setHighlightListener(new HighlightDelegate());
        rv.setPageMovedListener(new PageMovedDelegate());
        rv.setSelectionListener(new SelectionDelegate());
        rv.setSearchListener(new SearchDelegate());
        rv.setStateListener(new StateDelegate());

        rv.setPageTransition(PageTransition.Slide);

        SkyProvider skyProvider = new SkyProvider();
        rv.setContentProvider(skyProvider);

        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.width = RelativeLayout.LayoutParams.FILL_PARENT; // 400;
        params.height = RelativeLayout.LayoutParams.FILL_PARENT;
        //params.setMargins(0, 0, 0, calcDIP(context, 250));

        rv.setStartPositionInBook(page_position);

        rv.setPageMovedListener(pmlistener);
      //  pi.pagePositionInBook = 0.665934065934066;
     //   rv.setStartPositionInBook(pi.pagePositionInBook);
        rv.setLayoutParams(params);
        rv.useDOMForHighlight(false);
        rv.setNavigationAreaWidthRatio(0.4f); // both left and right side.
        //scene.addView(rv);
        ePubView.addView(rv);


        RelativeLayout.LayoutParams markButtonParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        markButton = new Button(this);
        markButton.setText("Highlight");
        markButtonParam.leftMargin = (int) (240 * density);
        markButtonParam.topMargin = (int) (5 * density);
        markButtonParam.width = (int) (70 * density);
        markButtonParam.height = (int) (35 * density);
        markButton.setLayoutParams(markButtonParam);
        markButton.setId(8083);
        markButton.setOnClickListener(listener);
        markButton.setVisibility(View.INVISIBLE);
        ePubView.addView(markButton);


    }


    public void onCreate(Bundle savedInstanceState) {
        debug("onCreate");
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        user = new User(username, password);
        booksList = new ArrayList<String>();
        booksListDat = new ArrayList<Book>();
        booksPagePositions = new ArrayList<Double>();
        activeBookId = 0;
        try { // Sets up a temporary Books.dat file
            this.makeStubBooksDatFile();
        } catch (IOException e) {
            Log.e("exception", "IOException trying to makestubbooksdat", e);
            Toast toast = Toast.makeText(context, "IOException trying to make a stub Books.dat", Toast.LENGTH_LONG);
            toast.show();
        }
        booksListDat = readBooksDat();
        //---------------Web_Services_Section---------------
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        fetchPosts();
        //--------------------------------------------------
        updateLocalBooksDat(booksListDat);
        copyToDeviceFromBooksDat(booksListDat);
        try {
            page_position = getBookLine(user.activeBookId, booksListDat);
        } catch (NullPointerException e) {
            Toast toast = Toast.makeText(context, "Failed to get the page_position", Toast.LENGTH_LONG);
            toast.show();
            page_position = 0;
            // TODO create new user;
        }
        this.makeViewerLayout();
        setContentView(R.layout.activity_viewer);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Loading...");
        scene = (ViewFlipper) findViewById(R.id.viewFlipper);
        scene.addView(ePubView);
        scene.showNext();

        filesView = (ListView) findViewById(R.id.filesView);
        listView = (ListView) findViewById(R.id.listView);
        booksList = getBooksNameList(booksListDat);
        String []booksForAdapter = new String[booksList.size()];
        booksList.toArray(booksForAdapter);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, booksForAdapter);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, booksForAdapter){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);

                if(getBookExistenceByName(getItem(position), booksListDat))
                {
                    row.setBackgroundColor (Color.WHITE); // some color
                    //convertView.setClickable(false);
                }
                else
                {
                    // default state
                    row.setBackgroundColor (Color.RED); // default coloe
                }

                return row;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String tmp = listView.getAdapter().getItem(position).toString();
                if(getBookExistenceByName(tmp, booksListDat)) {
                    scene.removeView(ePubView);
                    setBookLine(user.activeBookId, booksListDat, page_position);
                    user.activeBookId = position;
                    page_position = getBookLine(user.activeBookId, booksListDat);
                    makeViewerLayout();
                    scene.addView(ePubView);
                    reading = true;
                    toolbar.setTitle("Loading... ");
                    scene.showNext();
                    fab2.setVisibility(View.INVISIBLE);
                } else {
                    Toast toast = Toast.makeText(context , "There is no such book added :(", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });

        fab2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab2.setVisibility(View.INVISIBLE);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sceneIsVisible) {
                    scene.setVisibility(View.INVISIBLE);
                    filesView.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    sceneIsVisible = false;
                    toolbar.setTitle(Environment.getExternalStorageDirectory().toString());
                    booksList = new ArrayList<String>();
                    booksList = getBookNamesInExternalStorage();
                    final String []booksForAdapter = new String[booksList.size()];
                    booksList.toArray(booksForAdapter);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, booksForAdapter);
                    filesView.setAdapter(adapter);

                    filesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            try
                            {
                                String baseDirectory = Environment.getExternalStorageDirectory().toString();
                                String tmp = filesView.getAdapter().getItem(position).toString();
                                File target = new File(tmp);
                                String newName = tmp;
                                boolean bookIsNew = true;
                                for(Book b : booksListDat){
                                    if(newName.equals(b.name))
                                        bookIsNew = false;
                                }
                                if(bookIsNew) {
                                    //newName = target.getName();
                                    String pureName = removeExtention(newName);
                                    String targetDirectory = getFilesDir().getAbsolutePath() + "/books";
                                    Log.i("trgetdir", targetDirectory);
                                    String targetPath = targetDirectory + "/" + newName;
                                    Log.i("copyfile", baseDirectory + "/" + newName);
                                    InputStream localInputStream = new FileInputStream(new File(baseDirectory + "/" + newName));
                                    FileOutputStream localFileOutputStream = new FileOutputStream(targetPath);
                                    byte[] arrayOfByte = new byte[1024];
                                    int offset;
                                    while ((offset = localInputStream.read(arrayOfByte)) > 0) {
                                        localFileOutputStream.write(arrayOfByte, 0, offset);
                                    }
                                    localFileOutputStream.close();
                                    localInputStream.close();
                                    Log.d(TAG, newName + " copied to phone");

                                    Book newBook = new Book(getNewBookID(booksListDat), newName, 0, true);
                                    booksListDat.add(newBook);
                                    updateLocalBooksDat(booksListDat);
                                    Toast toast = Toast.makeText(context , newName + " added", Toast.LENGTH_SHORT);
                                    toast.show();

                                    //-----Updating the BOOK LIST -----------
                                    booksList = getBooksNameList(booksListDat);
                                    String []booksForAdapter = new String[booksList.size()];
                                    booksList.toArray(booksForAdapter);
                                    //---------------------------------------
                                    scene.setVisibility(View.VISIBLE);
                                    filesView.setVisibility(View.INVISIBLE);
                                    fab.setVisibility(View.VISIBLE);
                                    toolbar.setTitle("BOOKS LIST");
                                    sceneIsVisible = true;
                                } else {
                                    Toast toast = Toast.makeText(context , "It's not a vaible file :( " + "\n" + removeExtention(newName) +
                                            "(" + target.getName().substring(target.getName().length() - 5,
                                            target.getName().length()) + ") \ncheckForDuplicates: " + checkForDuplicate(tmp), Toast.LENGTH_SHORT);
                                    toast.show();
                                    
                                }

                            }
                            catch (IOException localIOException)
                            {
                                localIOException.printStackTrace();
                                Log.d(TAG, "failed to copy");
                                return;
                            }
                        }

                    });
                } else {
                    scene.setVisibility(View.VISIBLE);
                    filesView.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    toolbar.setTitle("BOOKS LIST");
                    sceneIsVisible = true;
                }
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reading) {
                    Snackbar.make(view, "FAB: Switched to Home(a.k.a. Settings)", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    toolbar.setTitle("BOOKS LIST");
                    reading = false;
                    fab2.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(view, "FAB: Switched to Viewer(a.k.a. eBook-Reader)", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    setNewTitle();
                    reading = true;
                    fab2.setVisibility(View.INVISIBLE);
                }
                scene.showNext();
            }
        });

    }

    public  void makeStubBooksDatFile() throws IOException {
        copyToDevice();
        List<String> copiedbooks = getCopiedEbooks();
        List<Book> stublist = new ArrayList<Book>();
        int i = 0;
        for(String s : copiedbooks){
            Book book = new Book(i, s, 0.1, true);
            if(!book.name.equals("HTCSpeakData"))
                stublist.add(book);
            i++;
        }
        stublist.add(new Book(i, "FakeBook1.epub", 0.1, false));
        stublist.add(new Book(i+1, "FakeBook2.epub", 0.1, false));
        stublist.add(new Book(i+2, "FakeBook3.epub", 0.1, false));

        FileOutputStream fos = context.openFileOutput("Books.dat", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(stublist);
        os.close();
        fos.close();
    }

    public void copyToDevice() {
        try
        {
            String baseDirectory = Environment.getExternalStorageDirectory().toString();

            File directory = new File(baseDirectory);
            File[] files = directory.listFiles();
            String fileName;
            for (int i = 0; i < files.length; i++)
            {
                if(files[i].isFile() && files[i].getName().substring(files[i].getName().length() - 5,
                        files[i].getName().length()).equals(".epub")) {
                    fileName = files[i].getName();
                    String targetDirectory = getFilesDir().getAbsolutePath() + "/books";
                    String targetPath = targetDirectory + "/" + fileName;
                    Log.i("copyfile", baseDirectory + "/" + fileName);
                    InputStream localInputStream = new FileInputStream(new File(baseDirectory + "/" + fileName));
                    FileOutputStream localFileOutputStream = new FileOutputStream(targetPath);
                    byte[] arrayOfByte = new byte[1024];
                    int offset;
                    while ((offset = localInputStream.read(arrayOfByte)) > 0) {
                        localFileOutputStream.write(arrayOfByte, 0, offset);
                    }
                    localFileOutputStream.close();
                    localInputStream.close();
                    Log.d(TAG, fileName + " copied to phone");
                }
            }

        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
            Log.d(TAG, "failed to copy");
            return;
        }
    }

    public String removeExtention(String filePath) {
        File f = new File(filePath);
        if (f.isDirectory()) return filePath;
        String name = f.getName();
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0)
        {
            return filePath;
        } else {
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }

    public List<String> getCopiedEbooks() {
        String targetDirectory = getFilesDir().getAbsolutePath() + "/books";
        File directory = new File(targetDirectory);
        File[] files = directory.listFiles();
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < files.length; i++)
            result.add(files[i].getName());
        return result;
    }

    public List<Book> readBooksDat() {
        try {
            List<Book> result = new ArrayList<Book>();
            FileInputStream fis = context.openFileInput("Books.dat");
            ObjectInputStream is = new ObjectInputStream(fis);
            result = (ArrayList<Book>) is.readObject();
            is.close();
            fis.close();
            return result;
        } catch (IOException e){
            Log.d(TAG, "failed to readBooksDat (IOExc)");
        } catch (ClassNotFoundException e){
            Log.d(TAG, "failed to readBooksDat (CNFExc)");
        }
        return new ArrayList<Book>();
    }

    public void copyToDeviceFromBooksDat(List<Book> books) {
        try
        {
            String baseDirectory = Environment.getExternalStorageDirectory().toString();
            File directory = new File(baseDirectory);
            File[] files = directory.listFiles();
            String fileName;

            boolean isInBooksDat;
            for (int i = 0; i < files.length; i++)
            {
                isInBooksDat = false;
                for(Book b : books){
                    fileName = b.name;
                    String targetDirectory = getFilesDir().getAbsolutePath() + "/books"; // "/books/" + pureName;
                    /*Log.i("trgetdir", targetDirectory);
                    File dir = new File(targetDirectory);
                    dir.mkdirs();*/
                    String targetPath = targetDirectory + "/" + fileName;
                    Log.i("copyfile", baseDirectory + "/" + fileName);
                    InputStream localInputStream = new FileInputStream(new File(baseDirectory + "/" + fileName));
                    FileOutputStream localFileOutputStream = new FileOutputStream(targetPath);
                    try {
                        byte[] arrayOfByte = new byte[1024];
                        int offset;
                        while ((offset = localInputStream.read(arrayOfByte)) > 0) {
                            localFileOutputStream.write(arrayOfByte, 0, offset);
                        }
                        b.exists = true;
                    } catch(FileNotFoundException ee){
                          b.exists = false;
                    }

                    localFileOutputStream.close();
                    localInputStream.close();
                    Log.d(TAG, fileName + " copied to phone");
                }
            }

        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
            Log.d(TAG, "failed to copy");
            Toast toast = Toast.makeText(context, "copyToDeviceFromBooksDat(List<Book> books) failed", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, url + urlrequest + username, onPostsLoaded, onPostsError);

        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<User> posts = Arrays.asList(gson.fromJson(response, User[].class));

            Log.i("PostActivity", response);
            Toast toast = Toast.makeText(context , "Post response acquired!", Toast.LENGTH_SHORT);
            toast.show();
            int i = 0;
            //Reading only first user instead of FOR LOOP
            User post = posts.get(0);
            user.setID(post.id);
            user.setActiveBookId(post.activeBookId);
            user.setBooks(post.books);
            String textForToast = "Uid: " + post.id + "\nUname: " + post.name +
                    "\nUpassword: " + post.password + "\nActiveBookID: " + post.activeBookId +
                    "\nItems: " + post.books;
            toast = Toast.makeText(context , textForToast , Toast.LENGTH_LONG);
            toast.show();
            //debugTextView.setText(textForToast);
            SynchronizeBooksDats(booksListDat, post.books);
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
            Toast toast = Toast.makeText(context , "Post response failed!", Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    public void SynchronizeBooksDats(List<Book> localBooks, List<Book> cloudBooks) {
        boolean bookisnew;
        for (Book cb : cloudBooks){
            bookisnew = true;
            for(Book lb : localBooks){
                if (cb.name.equals(lb.name))
                    bookisnew = false;
            }
            if(bookisnew)
                localBooks.add(new Book(cb.id, cb.name, cb.line));
        }
    }

    public void updateLocalBooksDat(List<Book> updatedBooks){
        try {
            FileOutputStream fos = context.openFileOutput("Books.dat", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(updatedBooks);
            os.close();
            fos.close();
        } catch (IOException e){
            Toast toast = Toast.makeText(context, "IOException trying to update local Books.dat", Toast.LENGTH_SHORT);
        }
    }

    public List<String> getBooksNameList (List<Book> books){
        List<String> result = new ArrayList<String>();
        for(Book b : books){
            result.add(b.name);
        }
        return result;
    }

    public boolean getBookExistenceByName(String book, List<Book> books){
        boolean result = false;
        for(Book b : books){
            if(book.equals(b.name))
                result = b.exists;
        }
        return result;
    }

    public String getBookNameByID (int id, List<Book> books){
        String result = "";
        for(Book b : books){
            if(id == b.id)
                result = b.name;
        }
        return result;
    }

    public void setBookLine(int id, List<Book> books, double line) {
        for(Book b : books){
            if(b.id == id)
                b.line = line;
        }
    }

    public double getBookLine(int id, List<Book> books) {
        double result = 0;
        for(Book b : books){
            if(b.id == id)
                result = b.line;
        }
        return result;
    }

    public List<String> getBookNamesInExternalStorage(){
        List<String> result = new ArrayList<String>();
        String baseDirectory = Environment.getExternalStorageDirectory().toString();
        File directory = new File(baseDirectory);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].isFile() && files[i].getName().length() >=5 && files[i].getName().substring(files[i].getName().length() - 5,
                    files[i].getName().length()).equals(".epub")) {
                result.add(files[i].getName());
            }
        }
        return result;
    }

    private void setNewTitle() {
        String newtitle = (rv.getPageIndexInChapter() + 0) + //unsuccessfuly tried to use number of previous pages instead of 0
                "  " + rv.getChapterTitle(rv.getChapterIndexByPagePositionInBook(page_position));
        if(newtitle.length() > 23){
            toolbar.setTitle(newtitle.substring(0, 23) + "...");
        } else {
            toolbar.setTitle(newtitle);
        }
    }

    public boolean checkForDuplicate(String tmp){
        for(String s : booksList){
            if(s.equals(tmp.substring(0, tmp.length()-5)) || s.equals(tmp))
                return false;
        }
        return true;
    }

    public int getNewBookID(List<Book> books) {
        int biggestid = 0;
        for(Book b : books){
            if(b.id > biggestid)
                biggestid = b.id;
        }
        return biggestid+1;
    }


    private PageMovedListener pmlistener = new PageMovedListener() {
        @Override
        public void onPageMoved(PageInformation pageInformation) {
            page_position = pageInformation.pagePositionInBook;
            Snackbar.make(ePubView, "chapterIndx = " + rv.getChapterIndexByPagePositionInBook(page_position) +
                    "   |   pageIndx = " + rv.getPageIndexInBook() +
                    "   |   pagePos = " + pageInformation.pagePositionInBook ,Snackbar.LENGTH_LONG).setAction("Action", null).show();
            setNewTitle();
        }

        @Override
        public void onChapterLoaded(int i) {
            //toolbar.setTitle(toolbar.getTitle() + ": " + rv.getChapterTitle(i));
            //toolbar.setTitle(rv.getChapterTitle(i) + " : " + rv.getPageIndexInBookByPagePositionInBook(page_position));

        }

        @Override
        public void onFailedToMove(boolean b) {

        }
    };



    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        verifyStoragePermissions(this);
        getMenuInflater().inflate(R.menu.menu_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private OnClickListener listener = new OnClickListener() {
        public void onClick(View arg) {
            if (arg.getId() == 8080) {
                displayNavPoints();
            } else if (arg.getId() == 8081) {
                finish();
            } else if (arg.getId() == 8082) {

            } else if (arg.getId() == 8083) {
                hideButton();
                mark();
            }
        }
    };



    private void displayNavPoints() {
        Log.i("displayNavPoints","displayNavPoints");
        NavPoints nps = rv.getNavPoints();
        for (int i=0; i<nps.getSize(); i++) {
            NavPoint np = nps.getNavPoint(i);
            debug(""+i+":"+np.text);
        }

        // modify one NavPoint object at will
        NavPoint onp = nps.getNavPoint(1);
        onp.text = "preface - it is modified";

        for (int i=0; i<nps.getSize(); i++) {
            NavPoint np = nps.getNavPoint(i);
            debug(""+i+":"+np.text);
        }
    }

    private void mark() {
        Log.i("mark","displayNavPoints");
        rv.markSelection(0x66FFFF00,"");

    }

    private void showToast(String msg) {
        Log.i("showToast","displayNavPoints");
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    class StateDelegate implements StateListener {
        @Override
        public void onStateChanged(State arg0) {
            Log.i("StateDelegate","onStateChanged");
        }
    }

    class HighlightDelegate implements HighlightListener {
        @Override
        public Highlights getHighlightsForChapter(int chapterIndex) {
            Log.i("HighlightDelegate","HighlightListener");
            Highlights results = new Highlights();
            for (int index = 0; index < highlights.getSize(); index++) {
                Highlight highlight = highlights.getHighlight(index);
                if (highlight.chapterIndex == chapterIndex) {
                    results.addHighlight(highlight);
                }
            }
            return results;
        }

        @Override
        public void onHighlightDeleted(Highlight highlight) {
            Log.i("HighlightDelegate","onHighlightDeleted");
            for (int index = 0; index < highlights.getSize(); index++) {
                Highlight temp = highlights.getHighlight(index);
                if (temp.chapterIndex == highlight.chapterIndex
                        && temp.startIndex == highlight.startIndex
                        && temp.endIndex == highlight.endIndex
                        && temp.startOffset == highlight.startOffset
                        && temp.endOffset == highlight.endOffset) {
                    highlights.removeHighlight(index);
                }
            }
        }

        @Override
        public void onHighlightInserted(Highlight highlight) {
            // TODO Auto-generated method stub
            highlights.addHighlight(highlight);
        }

        @Override
        public void onNoteIconHit(Highlight highlight) {
            debug(highlight.text);
        }


        @Override
        public Bitmap getNoteIconBitmapForColor(int arg0, int arg1) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Rect getNoteIconRect(int arg0, int arg1) {
            //Log.d();
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void onDrawCaret(Canvas arg0, Caret arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDrawHighlightRect(Canvas arg0, Highlight arg1, Rect arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onHighlightHit(Highlight arg0, int arg1, int arg2,
                                   Rect arg3, Rect arg4) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onHighlightUpdated(Highlight arg0) {
            // TODO Auto-generated method stub

        }
    }

    class PageMovedDelegate implements PageMovedListener {
        public void onPageMoved(PageInformation pi) {
            pi.pagePositionInBook = 0.665934065934066;
            rv.setStartPositionInBook(pi.pagePositionInBook);
            Log.i("PageMovedListener",String.valueOf(pi.pagePositionInBook));

        }

        @Override
        public void onChapterLoaded(int chapterIndex) {
            Log.i("PageMovedDelegate","onChapterLoaded");
            // TODO Auto-generated method stub

        }
        @Override
        public void onFailedToMove(boolean chapterIndex) {
            Log.i("PageMovedDelegate","onFailedToMove");
            // TODO Auto-generated method stub

        }
    }

    class SearchDelegate implements SearchListener {
        public void onKeySearched(SearchResult searchResult) {
            debug("pageIndex:" + searchResult.pageIndex + "startOffset:"
                    + searchResult.startOffset + "tag:" + searchResult.nodeName
                    + "text:" + searchResult.text);
        }

        public void onSearchFinishedForChapter(SearchResult searchResult) {
            rv.pauseSearch();
        }

        public void onSearchFinished(SearchResult searchResult) {
        }
    }

    private void moveButton(int x, int y) {
        RelativeLayout.LayoutParams markButtonParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        markButtonParam.leftMargin = x;
        markButtonParam.topMargin = y;
        markButton.setLayoutParams(markButtonParam);
    }

    private void showButton() {
        markButton.setVisibility(View.VISIBLE);
    }

    private void hideButton() {
        markButton.setVisibility(View.INVISIBLE);
        markButton.setVisibility(View.GONE);
    }

    class SelectionDelegate implements SelectionListener {
        @Override
        public void selectionChanged(Highlight highlight, Rect arg1, Rect arg2) {
            Log.i("SelectionDelegate","selectionChanged");
            // TODO Auto-generated method stub
            hideButton();
        }

        @Override
        public void selectionEnded(Highlight highlight, Rect rect1, Rect rect2) {
            // TODO Auto-generated method stub
            Log.i("SelectionDelegate","selectionEnded");
            int startX = rect1.left;
            int startY = rect1.top;
            int endX = rect1.right;
            int endY = rect1.bottom;
            Log.w("EPub", "selectionEnded");
            if ((endY + 30 + markButton.getHeight()) < ePubView.getHeight())
                moveButton(endX, endY + 30);
            else
                moveButton(startX, startY - 30 - markButton.getHeight());
            showButton();
        }
        @Override
        public void selectionStarted(Highlight highlight, Rect arg1, Rect arg2) {
            // TODO Auto-generated method stub
            Log.w("EPub", "selectionStarted");
            hideButton();
        }

        @Override
        public void selectionCancelled() {
            // TODO Auto-generated method stub
            Log.w("EPub", "selectionCancelled");
            hideButton();
        }
    }

    public void debug(String msg) {
        if (Setting.isDebug()) {
            Log.d(Setting.getTag(), msg);
        }
    }


    public static String unzip(String path, String zipname) {
        Log.i("copy", path);
        InputStream is;
        ZipInputStream zis;
        String folderName = null;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;
            boolean firstLoop = true;
            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();
                if (firstLoop) {
                    folderName = filename;
                    firstLoop = false;
                }
                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folderName;
    }





}
