package com.example.thefloorislava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.thefloorislava.GameView.screenRatioX;
import static com.example.thefloorislava.GameView.screenRatioY;

public class Button {
    public int x, y, width, height;
    public Bitmap button;

    Button(Resources res, int image) {
        button = BitmapFactory.decodeResource(res, image);

        width = button.getWidth()/4;
        height = button.getHeight()/4;

        width *= screenRatioX;
        height *= screenRatioY;

        button = Bitmap.createScaledBitmap(button, width, height, false);

        y = (int)(1500 * screenRatioX);
    }
}
