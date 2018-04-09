package com.example.hanneh.speakerapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class EqualizerTab1 extends Fragment {
    TextView text;

    public Bitmap mBitmap;
    public Paint mPaint = new Paint();
    public static int[] redHistogram = new int[256];
    public static int[] greenHistogram = new int[256];
    public static int[] blueHistogram = new int[256];
    public Path mHistoPath = new Path();
    public Canvas mCanvas;
    ImageView mImageView;

    public EqualizerTab1(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle savedstate){

        View rootView = inflater.inflate(R.layout.fragment_tab1, group, false);

        text = rootView.findViewById(R.id.section_label);
        text.setText("is this different");

        mImageView = (ImageView)rootView.findViewById(R.id.im2);
        int vWidth = mImageView.getMaxWidth();
        int vHeight = mImageView.getMaxHeight();

        mBitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);

       int [] test = ComputeHistogramTask();
       //redHistogram = ComputeHistogramTask(redHistogram);
       //greenHistogram = ComputeHistogramTask(greenHistogram);
       //blueHistogram = ComputeHistogramTask(blueHistogram);
      drawHistogram(mCanvas, test, Color.BLUE, PorterDuff.Mode.SCREEN);
        onDraw(mCanvas);
        return rootView;
    }



     public int[] ComputeHistogramTask(){

                int[] histo = new int[256 * 3];

                int w = mBitmap.getWidth();
                int h = mBitmap.getHeight();
                int[] pixels = new int[w * h];
                mBitmap.getPixels(pixels, 0, w, 0, 0, w, h);

                for (int i = 0; i < w; i++) {
                    for (int j = 0; j < h; j++) {
                        int index = j * w + i;
                        int r = Color.red(pixels[index]);
                        int g = Color.green(pixels[index]);
                        int b = Color.blue(pixels[index]);
                        histo[r]++;
                        histo[256 + g]++;
                        histo[512 + b]++;
                    }
                }
                return histo;
            }


        public void drawHistogram(Canvas canvas, int[] histogram, int color, PorterDuff.Mode mode) {
            int max = 0;
            for (int i = 0; i < histogram.length; i++) {
                if (histogram[i] > max) {
                    max = histogram[i];
                }
            }
            float w = mBitmap.getWidth(); // - Spline.curveHandleSize();
            float h = mBitmap.getHeight(); // - Spline.curveHandleSize() / 2.0f;
            float dx = 0; // Spline.curveHandleSize() / 2.0f;
            float wl = w / histogram.length;
            float wh = h / max;
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setARGB(100, 255, 255, 255);
            mPaint.setStrokeWidth((int) Math.ceil(wl));
            // Draw grid
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(dx, 0, dx + w, h, mPaint);
            canvas.drawLine(dx + w / 3, 0, dx + w / 3, h, mPaint);
            canvas.drawLine(dx + 2 * w / 3, 0, dx + 2 * w / 3, h, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(color);
            mPaint.setStrokeWidth(6);
            mPaint.setXfermode(new PorterDuffXfermode(mode));
            mHistoPath.reset();
            mHistoPath.moveTo(dx, h);
            boolean firstPointEncountered = false;
            float prev = 0;
            float last = 0;
            for (int i = 0; i < histogram.length; i++) {
                float x = i * wl + dx;
                float l = histogram[i] * wh;
                if (l != 0) {
                    float v = h - (l + prev) / 2.0f;
                    if (!firstPointEncountered) {
                        mHistoPath.lineTo(x, h);
                        firstPointEncountered = true;
                    }
                    mHistoPath.lineTo(x, v);
                    prev = l;
                    last = x;
                }
            }
            mHistoPath.lineTo(last, h);
            mHistoPath.lineTo(w, h);
            mHistoPath.close();
            canvas.drawPath(mHistoPath, mPaint);
            mPaint.setStrokeWidth(2);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setARGB(255, 200, 200, 200);
            canvas.drawPath(mHistoPath, mPaint);
        }


        public void onDraw(Canvas canvas) {
            canvas.drawARGB(0, 0, 0, 0);
            drawHistogram(canvas, redHistogram, Color.RED, PorterDuff.Mode.SCREEN);
            drawHistogram(canvas, greenHistogram, Color.GREEN, PorterDuff.Mode.SCREEN);
            drawHistogram(canvas, blueHistogram, Color.BLUE, PorterDuff.Mode.SCREEN);
        }
    }



