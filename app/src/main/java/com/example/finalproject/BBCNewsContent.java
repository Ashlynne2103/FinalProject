package com.example.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class BBCNewsContent extends Activity {
    private static final String ACTIVITY_NAME = "News Content Activity";
    Button btnSave;
    String title, link,description, pubDate, author;
    private BBCDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private boolean isTablet;
    Cursor cursor;
    ArrayList <String> dbList;
    int position;
    String dbColTitle;
    long id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcnews_content);

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        pubDate = intent.getExtras().getString("pubDate");
        author = intent.getExtras().getString("author");
        link = intent.getExtras().getString("link");
        description = intent.getExtras().getString("desc");

        EditText titleView = findViewById(R.id.BBC_NewsContent);
        TextView linkView = findViewById(R.id.BBC_NewsLink);
        WebView descView = findViewById(R.id.BBC_NewsDescription);

        titleView.setText(title);
        linkView.setText(link);
        descView.setVerticalScrollBarEnabled(true);
        descView.setHorizontalScrollBarEnabled(true);
        descView.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);

        linkView.setOnClickListener(view->{
            Intent intentLink = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intentLink);
        });
        dbList = new ArrayList<String>();
        dbHelper = new BBCDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor =  db.rawQuery( "select * from " + BBCDatabaseHelper.TABLE_NAME, null );

        if(cursor.getCount()>0){
            cursor.moveToFirst();
        }
        while(!cursor.isAfterLast()){
            dbColTitle = cursor.getString(cursor.getColumnIndexOrThrow(BBCDatabaseHelper.COLUMN_NEWS));

            Log.i(ACTIVITY_NAME, "SQL ID:" + id);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + dbColTitle);


            System.out.println("database testing: "+dbList);
            cursor.moveToNext();
        }
        if(cursor.getCount()>0){
            cursor.moveToFirst();

        }
        for(int i=0;i<cursor.getColumnCount();i++){
            cursor.getColumnCount();
        }

        FrameLayout frameLayout = findViewById(R.id.bbc_framelayout);
        if(frameLayout == null){
            Log.i(ACTIVITY_NAME, "framelayout not loaded:" +frameLayout);
            Toast.makeText(this,"framelayout not loaded",Toast.LENGTH_SHORT).show();
            isTablet = false;
        } else{
            Toast.makeText(this,"framelayout loaded",Toast.LENGTH_SHORT).show();
            isTablet = true;
        }
        btnSave = findViewById(R.id.BBC_saveButton);

        btnSave.setOnClickListener((View view) -> {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BBCDatabaseHelper.COLUMN_NEWS,title);
                    db.insert(BBCDatabaseHelper.TABLE_NAME,null, contentValues);

                    if (isTablet) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title",title);
                        bundle.putString("pubDate",pubDate);
                        bundle.putString("author",author);
                        bundle.putString("desc", description);
                        bundle.putString("TITLE", dbColTitle);

                        bundle.putInt("position",position);
                        bundle.putLong("id",id);
                        bundle.putBoolean("isTablet", true);

                        BBCNewsFragment fragment = new BBCNewsFragment();
                        fragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.bbc_framelayout, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }else {
                        Intent intent2 = new Intent(BBCNewsContent.this, BBCNewsStat.class);
                        intent2.putExtra("position",position);
                        intent2.putExtra("id",id);
                        intent2.putExtra("TITLE",dbColTitle);
                        startActivity(intent2);
                    }
                }
        );
    }


    public void deleteNews(int position){
        try {
            dbList.remove(position);
        }catch(SQLException e){

        }
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode == 10  && responseCode == -1) {
            Bundle bundle = data.getExtras();
            int position = bundle.getInt("position");
            long id = bundle.getLong("id");
            deleteNews(position);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
