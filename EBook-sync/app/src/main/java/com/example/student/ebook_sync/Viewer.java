package com.example.student.ebook_sync;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.skytree.epub.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.xml.sax.ContentHandler;

import static com.example.student.ebook_sync.R.id.toolbar;


public class Viewer extends AppCompatActivity {

    ReflowableControl rv;       // ReflowableControl
    RelativeLayout ePubView;    // Basic View of Activity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        final Context context = this;

        TextView textViewer = (TextView) findViewById(R.id.myTextViewer);
        textViewer.setMovementMethod(new ScrollingMovementMethod());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(context, Home.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


}





/*
WebView webview = (WebView) findViewById(R.id.myWebViewer);
        AssetManager assetManager = getAssets();
        try {
            // find InputStream for book
            InputStream epubInputStream = assetManager
                    .open("Metamorphosis-jackson.epub");

            // Load Book from inputStream
            Book book = (new EpubReader()).readEpub(epubInputStream);

            // Log the book's authors
            Log.i("author", " : " + book.getMetadata().getAuthors());

            // Log the book's title
            Log.i("title", " : " + book.getTitle());

            /* Log the book's coverimage property */
// Bitmap coverImage =
// BitmapFactory.decodeStream(book.getCoverImage()
// .getInputStream());
// Log.i("epublib", "Coverimage is " + coverImage.getWidth() +
// " by "
// + coverImage.getHeight() + " pixels");

// Log the tale of contents
//logTableOfContents(book.getTableOfContents().getTocReferences(), 0);
/*textViewer.setText(new String(book.getContents().get(4).getData()));
                    webview.loadDataWithBaseURL("file:///android_asset/", new String(book.getContents().get(4).getData()), "text/html", "UTF-8", "");
                    } catch (IOException e) {
                    Log.e("epublib exception", e.getMessage());
                    Toast.makeText(getApplicationContext(), "IOException caught!",
                    Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                    Log.e("epublib exception", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                    }

                    String javascrips = "";
                    try {
                    // InputStream input = getResources().openRawResource(R.raw.lights);
                    InputStream input = this.getAssets().open(
                    "Metamorphosis-jackson.epub");

                    int size;
                    size = input.available();
                    byte[] buffer = new byte[size];
                    input.read(buffer);
                    input.close();
                    // byte buffer into a string
                    javascrips = new String(buffer);
                    } catch (IOException e) {
                    e.printStackTrace();
                    }
                    // String html = readFile(is);

                    //webview.loadDataWithBaseURL(null, javascrips, "application/epub+zip", "UTF-8", null);
                    //webview.loadDataWithBaseURL("", finalstr, "text/html", "UTF-8", "");
                    // "application/epub+zip"
                    }



private void printBookData(Book neededbook){
        // TODO implement an option to switch between books (currently opens a stub boob)
        AssetManager assetManager = getAssets();
        TextView textViewer = (TextView) findViewById(R.id.myTextViewer);
        try {
        // find InputStream for book
        InputStream epubInputStream = assetManager.open("Metamorphosis-jackson.epub");
        //InputStream epubInputStream = assetManager.open("The-Problems-of-Philosophy-LewisTheme.epub");

        // Load Book from inputStream
        Book book = (new EpubReader()).readEpub(epubInputStream);

        String text = "";
        // Log the book's authors
        text = "author(s): " + book.getMetadata().getAuthors() + "\n";

        // Log the book's title
        text += "title: " + book.getTitle() + "\n";

        // Log the book's coverimage property
        Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage()
        .getInputStream());
        Log.i("epublib", "Coverimage is " + coverImage.getWidth() + " by "
        + coverImage.getHeight() + " pixels");

        textViewer.setText(text);
        // Log the tale of contents
        //printTableOfContents(book.getTableOfContents().getTocReferences(), 0);
        } catch (IOException e) {
        Log.e("epublib", e.getMessage());
        textViewer.setText("IO Exception while trying to read the file!");
        }
        }

private void printTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
        return;
        }
        TextView textViewer = (TextView) findViewById(R.id.myTextViewer);
        String txt = (String) textViewer.getText();
        for (TOCReference tocReference : tocReferences) {
        StringBuilder tocString = new StringBuilder();
        for (int i = 0; i < depth; i++) {
        tocString.append("\t");
        }
        tocString.append(tocReference.getTitle());
        Log.i("epublib", tocString.toString());
        txt += tocString + "\n";
        textViewer.setText(txt);

        printTableOfContents(tocReference.getChildren(), depth + 1);
        }
        }

private void printBook(Book thebook){
        // TODO implement an option to switch between books (currently opens a stub boob)
        AssetManager assetManager = getAssets();
        TextView textViewer = (TextView) findViewById(R.id.myTextViewer);
        String text = "";
        Book book;
        List<Resource> resources;
        EpubReader epubReader = new EpubReader();
        try {
        // read epub file
        //book = epubReader.readEpub(new FileInputStream("/storage/emulated/0/Download/Metamorphosis-jackson.epub"));
        book = (new EpubReader()).readEpub(assetManager.open("Metamorphosis-jackson.epub"));
        resources = book.getContents();
        for(Resource r : resources) {
        text += r.toString() + "\n";
        }
        textViewer.setText(text);

        } catch(NullPointerException e) {
        textViewer.setText("NullPointerException caught while trying to read or print a book!");
        } catch(Exception e){
        textViewer.setText("Exception caught while trying to read or print a book!");
        }
        }
 */