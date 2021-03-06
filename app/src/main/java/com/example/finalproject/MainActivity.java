package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String ACTIVITY_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button News = findViewById(R.id.NewsButton);
        News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BBCNewsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == 50 && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "returned to MainActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this, messagePassed, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    public void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    public void onStrop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
