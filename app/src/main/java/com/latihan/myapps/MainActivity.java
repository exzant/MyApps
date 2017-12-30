package com.latihan.myapps;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button btnSet;
    HorizontalScrollView hsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        imageView = (ImageView) findViewById(R.id.imgFullscreen);
        hsv = (HorizontalScrollView) findViewById(R.id.hScrollView);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL("http://themarshmallowz.890m.com/images/kitkat.jpg");
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            imageView.setImageBitmap(bmp);
            adjustImageAspect(width, height);
            Log.d("MyCheck", "SW : "+String.valueOf(getScreenWidth())+"iw : "+String.valueOf(width)+"ih : "+String.valueOf(height));
//            int scrollX = (imageView.getLeft() - (getScreenWidth() / 2)) + (imageView.getWidth() / 2);
//            hsv.smoothScrollTo(scrollX, 0);
            hsv.scrollTo(10000000, 0);

        } catch (IOException e) {
            Log.e("MyCheck", e.getMessage());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hsv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        hsv.scrollTo(10000, 0);
                    }
                }
        );
    }

    /**
     * Adjusting the image aspect ration to scroll horizontally, Image height
     * will be screen height, width will be calculated respected to height
     * */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void adjustImageAspect(int bWidth, int bHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if (bWidth == 0 || bHeight == 0)
            return;

        int sHeight = 0;

        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sHeight = size.y;
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            sHeight = display.getHeight();
        }

        int new_width = (int) Math.floor((double) bWidth * (double) sHeight
                / (double) bHeight);
        params.width = new_width;
        params.height = sHeight;

        Log.d("MyCheck", "Fullscreen image new dimensions: w = " + new_width
                + ", h = " + sHeight);

        imageView.setLayoutParams(params);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
