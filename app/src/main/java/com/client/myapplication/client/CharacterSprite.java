package com.client.myapplication.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;

import static com.client.myapplication.client.Constants.LIE_TEXT;
import static com.client.myapplication.client.Constants.LIE_TOLD;
import static com.client.myapplication.client.Constants.NEUTRALIZE;
import static com.client.myapplication.client.Constants.TRUE_TEXT;
import static com.client.myapplication.client.Constants.TRUTH_TOLD;
import static com.client.myapplication.client.MainActivity.receivedMessage;

/**
 * Created by Adhitha Dias on 23/06/2019.
 */

public class CharacterSprite {

    private Context context;
    private Bitmap image;
    private int x, y;
    private int xVelocity = 10;
    private int yVelocity = 5;
    static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private boolean timerStarted = false;
    private static int DURATION = 2000;
    private long activatedAt = Long.MAX_VALUE;

    private Paint textPaint;

    private String TAG = "CharacterSprite";

    public CharacterSprite(Bitmap bmp) {
        Log.d("CharacterSprite", "init with bitmap");
        image = bmp;
        x = screenWidth/2;
        y = screenHeight/2;

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(500);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public CharacterSprite(Context context) {

        Log.d("CharacterSprite", "init with context");

        this.context = context;

        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.avdgreen);
        x = screenWidth/2;
        y = screenHeight/2;

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(500);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    private void activate() {
        activatedAt = System.currentTimeMillis();
    }

    private boolean isActive() {
        long activeFor = System.currentTimeMillis() - activatedAt;

        return activeFor >= 0 && activeFor <= DURATION;
    }

    public void draw(Canvas canvas) {

//        Log.d(TAG, screenWidth + "  " + screenHeight);
//        Log.d(TAG, x + "  " + y);

//        if (TRUTH_TOLD.equals(receivedMessage)) {
//
//            if (!timerStarted) {
//                timerStarted = true;
//                activate();
//                // - execute one time sound function
//            }
//
//            if (isActive()) {
//                Log.d(TAG, "time: " + (System.currentTimeMillis() - activatedAt));
//                drawTruthOrLie(canvas, Color.GREEN, Color.WHITE, TRUE_TEXT);
//            } else {
//                timerStarted = false;
//
//                receivedMessage = "changed";
//            }
//
//        } else if (LIE_TOLD.equals(receivedMessage)) {
//
//            if (!timerStarted) {
//                timerStarted = true;
//                activate();
//                // - execute one time sound function
//            }
//
//            if (isActive()) {
//                Log.d(TAG, "time: " + (System.currentTimeMillis() - activatedAt));
//                drawTruthOrLie(canvas, Color.RED, Color.WHITE, LIE_TEXT);
//            } else {
//                timerStarted = false;
//
//                receivedMessage = "changed";
//            }
//
//        } else {
//            timerStarted = false;
//            drawDetectorScreen(canvas);
//        }


        switch (receivedMessage) {

            case TRUTH_TOLD:

                if (!timerStarted) {
                    timerStarted = true;
                    activate();
                    // - execute one time sound function
                    DURATION = 2500;
                    playTruthSound();
                }

                if (isActive()) {
                    Log.d(TAG, "time: " + (System.currentTimeMillis() - activatedAt));
                    drawTruthOrLie(canvas, Color.GREEN, Color.WHITE, TRUE_TEXT);
                } else {
                    timerStarted = false;

                    receivedMessage = "changed";
                }
                break;

            case LIE_TOLD:

                if (!timerStarted) {
                    timerStarted = true;
                    activate();
                    // - execute one time sound function
                    DURATION = 2500;
                    playLieSound();
                }

                if (isActive()) {
                    Log.d(TAG, "time: " + (System.currentTimeMillis() - activatedAt));
                    drawTruthOrLie(canvas, Color.RED, Color.WHITE, LIE_TEXT);
                } else {
                    timerStarted = false;

                    receivedMessage = "changed";
                }
                break;

            default:

                timerStarted = false;
                drawDetectorScreen(canvas);
                break;

        }

    }

    private void drawTruthOrLie(Canvas canvas, int backgroundColour, int textColour, String shownText) {
        canvas.drawColor(backgroundColour);

        textPaint.setColor(textColour);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText(shownText, xPos, yPos, textPaint);

    }

    private void playLieSound() {
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.game_sound_incorrect_with_delay);
        mediaPlayer.start();
    }

    private void playTruthSound() {
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.game_sound_correct);
        mediaPlayer.start();
    }

    private void drawDetectorScreen(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(250);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) (canvas.getHeight() * 0.90) ;

        Paint greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        canvas.drawRect(xPos - canvas.getWidth() / 2, yPos - (int) ((textPaint.descent() + textPaint.ascent()) * 0.5),
                xPos + canvas.getWidth() / 2, yPos + (int) ((textPaint.descent() + textPaint.ascent()) * 1.5), greenPaint);

        canvas.drawText("DETECTING", xPos, yPos, textPaint);

//        canvas.drawBitmap(image, x, y, null);
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
