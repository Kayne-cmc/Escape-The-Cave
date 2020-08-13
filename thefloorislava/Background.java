package com.example.thefloorislava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    public int x = 0, y = 0;
    public Bitmap background;

    Background(int screenX, int screenY, Resources res, int image) {
        background = BitmapFactory.decodeResource(res, image);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }

}
