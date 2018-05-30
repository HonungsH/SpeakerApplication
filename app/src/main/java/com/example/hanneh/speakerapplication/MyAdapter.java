package com.example.hanneh.speakerapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hanneh on 3/20/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PersonViewHolder>{

    public static SpeakerSettings speakers = new SpeakerSettings();
    private static final String TAG = "debug";
    ArrayList<String> ips;

    MyAdapter(ArrayList<String> ips) {
        this.ips = ips;

        createSpeakerList(ips);
    }

    public static void createSpeakerList(ArrayList<String> ips){
        if(!speakers.speakers.isEmpty()) {
            return;
        }

        for (int i = 0; i < ips.size(); i++) {
            speakers.addSpeaker(ips.get(i));
        }
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView displayIp;
        View view;


        PersonViewHolder(View itemView) {

            super(itemView);

            view = itemView;
            cv =  itemView.findViewById(R.id.cv);
            displayIp = itemView.findViewById(R.id.info_text);


        }
    }

    @Override
    public MyAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        Log.e(TAG, parent.getContext().toString());
        PersonViewHolder pvh = new PersonViewHolder(v);

        return pvh;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void onBindViewHolder(MyAdapter.PersonViewHolder holder, final int position) {

      final String ip = ips.get(position);
       holder.displayIp.setText(ip);


        //final int[] mExpandedPosition = {-1};

       // final boolean isExpanded = position== mExpandedPosition[0];
        //holder.displayIp.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        //holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(), SpeakerActivity.class);
                myIntent.putExtra("ip", ip );
               // myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(myIntent);
            }
        });


        }

    @Override
    public int getItemCount() {
        Log.e(TAG, String.valueOf(ips.size()));
        return ips.size();
    }

}