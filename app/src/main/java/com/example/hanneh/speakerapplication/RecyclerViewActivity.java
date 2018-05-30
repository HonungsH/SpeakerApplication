package com.example.hanneh.speakerapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class RecyclerViewActivity extends BaseMenuActivity {

    private static final String TAG = "debug";
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recyclerview_activity);


        rv = findViewById(R.id.my_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        MyAdapter adapter = new MyAdapter(IpList.getSpeakerIPs());
        Log.e(TAG, "kom jag hit");
        rv.setAdapter(adapter);
    }
}
