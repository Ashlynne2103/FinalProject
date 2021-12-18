package com.example.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class BBCNewsStat extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcnews_stats);

        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("desc");
        String pubDate =getIntent().getExtras().getString("pubDate");
        String author =getIntent().getExtras().getString("author");

        int position = getIntent().getExtras().getInt("position");
        long id =getIntent().getExtras().getLong("id");

        String str = getIntent().getExtras().getString("TITLE");

        Bundle bundle = new Bundle();
        BBCNewsFragment fragment = new BBCNewsFragment();
        fragment.isTablet=false;
        bundle.putString("desc",description);
        bundle.putString("title",title);
        bundle.putString("pubDate",pubDate);
        bundle.putString("author",author);

        bundle.putLong("id",id);
        bundle.putInt("position",position);
        bundle.putString ("TITLE",str);
        bundle.putBoolean("isTablet",false);

        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
