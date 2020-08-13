package com.example.thefloorislava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.thefloorislava.GameView.screenRatioY;

public class FireFloor {
    public int x = 0, y, width, height;
    public Bitmap floor;

    FireFloor (int screenX, int screenY, Resources res) {
        floor = BitmapFactory.decodeResource(res, R.drawable.fire_floor);

        width = screenX;
        height = (int)(floor.getHeight()/12 * screenRatioY);

        y = screenY - height;

        floor = Bitmap.createScaledBitmap(floor, width, height, false);
    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
