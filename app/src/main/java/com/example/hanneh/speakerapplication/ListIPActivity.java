package com.example.hanneh.speakerapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListIPActivity extends BaseMenuActivity {
    private TextView mtextView;
    private String[] _ips;
    private CardView mcardView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ip);

        Intent myintent = new Intent(ListIPActivity.this, RecyclerViewActivity.class);
        ListIPActivity.this.startActivity(myintent);

        mtextView = findViewById(R.id.info_text);






    }
}
