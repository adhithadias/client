package com.client.myapplication.client;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import static com.client.myapplication.client.MainActivity.receivedMessage;

/**
 * Created by Adhitha Dias on 23/06/2019.
 */

public class CharacterSprite {

    private Bitmap image;
    private int x, y;
    private int xVelocity = 10;
    private int yVelocity = 5;
    static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private String TAG = "CharacterSprite";

    public CharacterSprite(Bitmap bmp) {
        Log.d("CharacterSprite", "init");
        image = bmp;
        x = screenWidth/2;
        y = screenHeight/2;
    }

    public void draw(Canvas canvas) {

//        Log.d(TAG, screenWidth + "  " + screenHeight);
//        Log.d(TAG, x + "  " + y);

        canvas.drawBitmap(image, x, y, null);

    }

    public void update() {

//        System.out.println(screenWidth + "  " + screenHeight);
//        System.out.println("receivedMessage: " + receivedMessage);

        x += xVelocity;
        y += yVelocity;
        if ((x > screenWidth - image.getWidth()) || (x < 0)) {
            xVelocity = xVelocity*-1;
        }
        if ((y > screenHeight - image.getHeight()) || (y < 0)) {
            yVelocity = yVelocity*-1;
        }

    }


}
