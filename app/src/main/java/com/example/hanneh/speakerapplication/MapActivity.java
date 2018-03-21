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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MapActivity extends BaseMenuActivity {

    private static final String TAG = "Bajs" ;
    private Paint mPaint;
    private float [] points;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private ImageView mImageView;
    private TextView textView;
    private Button btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        points = new float[] {150f, 200f, 30f, 130f};

        textView = findViewById(R.id.coolText);

        mImageView = (findViewById(R.id.myimageview));

        onDraw(mImageView);

        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Packet packet = ServerRunLocalization.run(IpList.getIpList());
                Log.e(TAG, String.valueOf(packet.getSize()));
            }
        });


    }


    public float[] getArrayCoordinates(){
        return points = Coordinates.getPoints();
    }


    protected void onDraw(ImageView view){
        int vWidth = view.getMaxWidth();
        int vHeight = view.getMaxHeight();
        Log.e(TAG, String.valueOf(vWidth));
        Log.e(TAG, String.valueOf(vHeight));


        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);


        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);

        mCanvas.drawColor(Color.parseColor("#ffffff"));

        mPaint = new Paint();
        Log.e(TAG, "cANVAS CREATED");
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(30);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mCanvas.drawPoints(points, mPaint);
        textView.setText(("Each dot represents a speaker in the room. Press button \"Draw\" to redraw"));



    }






    }








