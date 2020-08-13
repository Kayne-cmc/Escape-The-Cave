package com.example.thefloorislava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.shapes.RoundRectShape;

import static com.example.thefloorislava.GameView.screenRatioX;
import static com.example.thefloorislava.GameView.screenRatioY;


public class Obstacle {
    public int x, y, width, height, speed;
    public Bitmap obstacle;

    Obstacle(int screenY, Resources res) {
        obstacle = BitmapFactory.decodeResource(res, R.drawable.obstacle);

        width = obstacle.getWidth()/5;
        height = obstacle.getHeight()/5;

        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        obstacle = Bitmap.createScaledBitmap(obstacle, width, height, false);

        x = -width;
        y = screenY;
    }

    public RoundRectShape getCollisionShape() {
        float[] array = new float[]{(float) width, (float) width, (float) width, (float) width, (float) width, (float) width, (float) width, (float) width};
        return new RoundRectShape(array, null, null);
    }
}
