package com.example.hanneh.speakerapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.sqrt;

public class MapActivity extends BaseMenuActivity {

    private static final String TAG = "Bajs" ;
    private Paint mPaint;
    private float [] points;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private ImageView mImageView;
    private TextView textView;
    private Button btn, settingsButton;
    public float x, y;
    private int color = Color.BLACK;
    ArrayList<Float> coords;
    int imheight, imwidth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        points = new float[] {150f, 200f, 400f, 130f};
        mImageView = (findViewById(R.id.myimageview));

        textView = findViewById(R.id.coolText);
        imheight = mImageView.getMaxHeight();
        imwidth = mImageView.getMaxWidth();
        Log.e(TAG, String.valueOf(imheight) +" "+ String.valueOf(imwidth));



        settingsButton = findViewById(R.id.settings);


        btn = findViewById(R.id.btn);
        settingsButton.setVisibility(View.GONE);

        mImageView.setOnTouchListener(handleTouch);
        int vWidth = mImageView.getMaxWidth();
        int vHeight = mImageView.getMaxHeight();

        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);


        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.parseColor("#ffffff"));
        onDraw(mCanvas);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coords = new ArrayList<>();
                ServerContact contact = new ServerContact();

                Packet packet = contact.request(contact.createLocalization(IpList.getSpeakerIPs()));
                packet.getByte();

                int speakers = packet.getInt();

                for (int i = 0; i < speakers; i++){
                    packet.getString();

                    int dims = packet.getInt();

                    for (int j = 0; j < dims; j++) {
                        coords.add(packet.getFloat());
                    }

                    int z = packet.getInt();

                    for (int k = 0; k < z; k++){
                        packet.getString();
                        packet.getFloat();
                    }
                }
            }
        });
    }


    public float[] getArrayCoordinates(){
        return points = Coordinates.getPoints();
    }

    public void onDraw(Canvas canvas){


        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setStrokeWidth(120);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

       canvas.drawPoints(points, mPaint);

        textView.setText(("Each dot represents a speaker in the room. Press button \"Draw\" to redraw"));
    }

    public int compareFloatDistance(float x, float y){
        double bestDistance = 10000000;
        int id = -1;

        for (int i = 0; i < points.length ; i+=2){
            double distance = (sqrt  ((x - points[i])*(x - points[i])) + ((y - points[i+1])*(y-points[i+1])));

            Log.e(TAG, "X = " + String.valueOf(x));
            Log.e(TAG, "Y = " + String.valueOf(y));
            Log.e(TAG, "Xpoints = " + String.valueOf(points[i]));
            Log.e(TAG, "Ypoints = " + String.valueOf(points[i+1]));
            Log.e(TAG, "distance == " + String.valueOf(distance));
                if(distance < bestDistance){
                    bestDistance = distance;
                    id = i / 2;

            }

        }
        Log.e(TAG, "length ==" + String.valueOf(points.length));
        Log.e(TAG, "id == " + String.valueOf(id));
        return id;


    }

    public float getDecimalFormat(float number){
        return (float)Math.round(number * 100) / 100;

    }
/*
    public boolean onTouchEvent(MotionEvent event){
        textView.setText("TEST");
        return super.onTouchEvent(event);

    }
    */
    public View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

           float  x =  event.getX();
           float y =  event.getY();

           int id;

            id = compareFloatDistance(x, y);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    Log.e("TAG", "p" + x + y);

                    settingsButton.setVisibility(View.VISIBLE);

                    //Hämta IP för den jag klickat närmast
                   final String ip = IpList.getSpeakerIPs().get(id);

                   MyAdapter.createSpeakerList(IpList.getSpeakerIPs());


                    settingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MapActivity.this, SpeakerActivity.class);
                            intent.putExtra("ip", ip);
                            startActivity(intent);


                        }
                    });

                    textView.setText(ip + " " + '\n' + "(" + getDecimalFormat(x) + ", " + getDecimalFormat(y)+ ")");

                    mPaint.setColor(Color.GREEN);
                    mCanvas.drawPoint(points[id * 2], points[id*2 + 1], mPaint);

                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("TAG", "moving: (" + x + ", " + y + ")");

                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("TAG", "testing");
                    mPaint.setColor(color);
                    mCanvas.drawPoint(points[id * 2], points[id*2 + 1], mPaint);
                    break;
            }

            return true;
        }
    };



    }








