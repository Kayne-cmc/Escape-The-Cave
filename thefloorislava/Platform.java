package com.example.thefloorislava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.thefloorislava.GameView.GAP;
import static com.example.thefloorislava.GameView.screenRatioX;
import static com.example.thefloorislava.GameView.screenRatioY;

public class Platform {
    public int leftx = 0, rightx, y, width, height;
    public Bitmap left, right;

    Platform(Resources res) {
        left = BitmapFactory.decodeResource(res, R.drawable.platform);
        right = BitmapFactory.decodeResource(res, R.drawable.platform);

        width = left.getWidth();
        height = left.getHeight();

        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        left = Bitmap.createScaledBitmap(left, width, height, false);
        right = Bitmap.createScaledBitmap(right, width, height, false);

        rightx = leftx + width + GAP;
    }

    public Rect getLeftCollisionShape() {
        return new Rect(leftx, y, leftx + width, y + height);
    }

    public Rect getRightCollisionShape() {
        return new Rect(rightx, y, rightx + width, y + height);
    }

    public Bitmap crop(Bitmap platform, int width) {
        return Bitmap.createBitmap(platform, 0, 0, width, height);
    }
}
