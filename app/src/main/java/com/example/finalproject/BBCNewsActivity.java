package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * activity: BBC News RSS Reader
 * Date: Dec 16, 2021
 */

public class BBCNewsActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "BBC News Reader";
    private ProgressBar progressBar;
    private Button btnSearch,btnFavorite;
    protected ListView newsListView;
    private NewsAdapter newsAdapter;
    private TextView titleText;
    private EditText editText;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ArrayList<BBCNewsData> newsList;
    private String tempTitle, tempLink, tempDescription, tempPubDate, tempAuthor;
    BBCNewsData newsData;

    /**
     * onCreate()method is to create the activity
     * instantiate variables and
     * populate data into the listView
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcnews);

        //instantiate the progress bar and newsList as an ArrayList


        newsList = new ArrayList<>();

        progressBar = findViewById(R.id.BBC_progressBar);

        titleText = findViewById(R.id.News_title);
        editText = findViewById(R.id.BBC_editText);
        btnFavorite=findViewById(R.id.BBC_favorites);
        editText.setShowSoftInputOnFocus(false);

        //to populate the listView with data

        newsListView = findViewById(R.id.BBC_listView);
        newsAdapter = new NewsAdapter(BBCNewsActivity.this);
        newsListView.setAdapter(newsAdapter);

        String BBCurl = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";


        newsListView.setOnItemClickListener((parent, view, position, id) -> {
            progressBar.setProgress(50);
            Toast.makeText(getBaseContext(), "Going to the details of the news..." , Toast.LENGTH_SHORT).show();
            newsData = newsAdapter.getItem(position);
            Intent intent = new Intent(BBCNewsActivity.this, BBCNewsContent.class);
            intent.putExtra("title", newsData.getNewsTitle());
            intent.putExtra("link", newsData.getNewsLink());
            intent.putExtra("desc", newsData.getNewsDescription());
            intent.putExtra("pubDate", newsData.getPubDate());
            intent.putExtra("author", newsData.getAuthor());
            startActivity(intent);
        });

        //click search for news titles that you want

        btnSearch = findViewById(R.id.BBC_SearchNews);

        btnSearch.setOnClickListener(view -> {


            for(int i=0;i<newsList.size();i++) {
                if ((newsList.get(i).getNewsTitle()).contains(editText.getText().toString())) {
                    Snackbar.make(findViewById(R.id.BBC_SearchNews), newsList.get(i).getNewsTitle(), Snackbar.LENGTH_LONG).show();
                    Spannable spannable = new SpannableString(newsList.get(i).getNewsTitle());
                    titleText.setText(spannable);

                    String[] count = String.valueOf(Html.fromHtml(newsList.get(i).getNewsDescription())).split("\\s+");
                    builder = new AlertDialog.Builder(BBCNewsActivity.this);
                    builder.setTitle("About the news");
                    builder.setMessage("Headline: \n " + newsList.get(i).getNewsTitle() + "\n" + "publish date: "
                            + newsList.get(i).getPubDate()
                            + "\n Author: " + newsList.get(i).getAuthor() + "\n Total Word Count: " + count.length);
                    builder.setNegativeButton("cancel", (dialog, id12) -> dialog.dismiss());
                    dialog = builder.create();
                    dialog.show();
                }
            }
        });

        btnFavorite.setOnClickListener((View View) ->{
            Intent intent = new Intent(BBCNewsActivity.this, BBCNewsStat.class);
            newsData=new BBCNewsData();
            intent.putExtra("title", newsData.getNewsTitle());
            intent.putExtra("desc", newsData.getNewsDescription());
            intent.putExtra("pubDate", newsData.getPubDate());
            intent.putExtra("author", newsData.getAuthor());
            startActivity(intent);
        });

        Toolbar bbc_toolbar = findViewById(R.id.bbc_toolbar);
        setSupportActionBar(bbc_toolbar);
        new NewsQuery().execute();
    }

    /**
     * onCreateOptionsMenu (menu) is to display the menu and menuitem
     */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bbc_toolbarmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();
        switch (id) {

            case R.id.BBC_InstructionItem: {
                builder = new AlertDialog.Builder(BBCNewsActivity.this);
                builder.setTitle("Instructions");
                builder.setMessage("Click news title for more details, then click the link to view the website. " +
                        "To go to another activity, please click one icon in the toolbar" );
                builder.setNegativeButton("cancel", (dialog, id12) -> dialog.dismiss());
                dialog = builder.create();
                dialog.show();
                break;
            }

            case R.id.BBC_AboutItem:
                builder = new AlertDialog.Builder(BBCNewsActivity.this);
                builder.setTitle("About")
                        .setMessage("BBC News RSS Reader APP V1.0")
                        .setNegativeButton("cancel", (dialog, id12) -> dialog.dismiss());
                dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }

    /**
     * Adpater class, to pass data to the listView
     */


    public class NewsAdapter extends ArrayAdapter<BBCNewsData> {
        protected NewsAdapter(Context ctx) {
            super(ctx, 0);
        }


        public int getCount() {
            return newsList.size();
        }

        public BBCNewsData getItem(int position) {
            return newsList.get(position);
        }

        /**
         * @param position
         * @param view
         * @param parent
         * @return news title as a View object at the row position
         */


        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater = BBCNewsActivity.this.getLayoutInflater();
            View resultView = inflater.inflate(R.layout.list_view_bbcnews, null);
            titleText = resultView.findViewById(R.id.News_title);
            titleText.setText(newsList.get(position).getNewsTitle());
            return resultView;
        }

        public long getItemId(int position) {
            return position;
        }
    }

    /**
     * class NewsQuery is used to get data from the news web site
     */

    private class NewsQuery extends AsyncTask<String, String, String> {
        InputStream stream;
        XmlPullParser parser;
        XmlPullParserFactory xmlPullParserFactory;
        private String title, link, description;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            newsAdapter.notifyDataSetChanged();
        }


        private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "title");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "title");
            return title;
        }


        private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "link");
            String link= readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "link");
            return link;
        }

        private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "pubDate");
            String pubDate = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "pubDate");
            return pubDate;
        }
        private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "author");
            String author = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "author");
            return author;
        }

        private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {

            parser.require(XmlPullParser.START_TAG, null, "description");

            String description = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "description");
            return description;
        }


        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = null;
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

        @Override
        protected String doInBackground(String... args) {
            URL url = null;

            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.i(ACTIVITY_NAME, "Device is connecting to the network");
            } else {
                Log.i(ACTIVITY_NAME, "Device is not connecting to network");
            }
            try {

                URL BBCurl = new URL(args[0]);
                HttpsURLConnection conn = (HttpsURLConnection) BBCurl.openConnection();
                InputStream inputBBC = conn.getInputStream();

                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                Log.d(ACTIVITY_NAME, "Connecting with URL...");
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("debug", "the response is: " + response);
                stream = conn.getInputStream();
                Log.d(ACTIVITY_NAME, "Reading rss. Stream is: " + stream);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                xmlPullParserFactory = XmlPullParserFactory.newInstance();
                xmlPullParserFactory.setNamespaceAware(true);
                parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(stream, null);
                parser.nextTag();
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    if (name.equals("title")) {
                        tempTitle = readTitle(parser);
                    }
                    if (name.equals("link")) {
                        tempLink = readLink(parser);
                    }
                    if (name.equals("pubDate")) {
                        tempPubDate = readPubDate(parser);
                    }
                    if (name.equals("author")) {
                        tempAuthor = readAuthor(parser);
                    }
                    if (name.equals("description")) {
                        tempDescription = readDescription(parser);
                        newsList.add(new BBCNewsData(tempTitle, tempLink, tempDescription,tempPubDate,tempAuthor));
                        progressBar.setProgress(75);
                    }

                }
                stream.close();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return title;
        }

        public void onProgressUpdate(Integer... value) {
            progressBar.setProgress(value[0]);
            progressBar.setVisibility(View.VISIBLE);
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
        }
    }
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}