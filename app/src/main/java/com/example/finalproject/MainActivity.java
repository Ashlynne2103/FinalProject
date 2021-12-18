package com.example.finalproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public TextView titleText;
    public TextView descriptionText;
    public TextView dateText;
    public TextView weblinkText;
    public WebHTTP articles;

    public MainActivity(Context context, int num, ArrayList<WebHTTP> art) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.favourites_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourites, menu);
        return true;
    }

    private void setBBCList(){

        ListView BBC_list = (ListView) findViewById(R.id.articles_list);
        final ArrayAdapter<WebHTTP> adapter = new ArrayAdapter(this,articles);
        articles_list.setAdapter(adapter);

        articles_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actions_add:
                Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.actions_open:
                Toast.makeText(this, "Favourites opened", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }

    WebHTTP web = new WebHTTP();
    web.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

}

class DummyContent {


    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();


    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {

        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details.");
        }
        return builder.toString();
    }


    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}