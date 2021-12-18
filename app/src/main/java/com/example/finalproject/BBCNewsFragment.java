package com.example.finalproject;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BBCNewsFragment extends Fragment {
    View view;
    TextView newsView;
    String newsDescription, title, pubDate, author, savedTitle;
    Bundle bundle;
    boolean isTablet;
    int position;
    long id;
    Button deleteBtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if(bundle!=null){
            title = bundle.getString("title");
            newsDescription = bundle.getString("desc");
            pubDate = bundle.getString("pubDate");
            author = bundle.getString("author");

            position = bundle.getInt("position");
            savedTitle = bundle.getString("TITLE");
            id = bundle.getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bbcnews, container, false);
        newsView = view.findViewById(R.id.BBC_stats);
        newsView.setText(savedTitle);

        deleteBtn = view.findViewById(R.id.DeleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isTablet) {
                    Intent intent = new Intent(getActivity(), BBCNewsContent.class);
                    intent.putExtra("position", position);
                    intent.putExtra("id", id);
                    getActivity().setResult(-1, intent);
                    getActivity().finish();
                } else {
                    Log.i("tag", "trying to delete a message: " + position);
                    BBCNewsContent cbc = (BBCNewsContent) getActivity();
                    cbc.deleteNews(position);
                    getFragmentManager().beginTransaction().remove(BBCNewsFragment.this).commit();
                }
            }
        });

        Log.i("news description","testing in fragment",null);
        return view;
    }


}
