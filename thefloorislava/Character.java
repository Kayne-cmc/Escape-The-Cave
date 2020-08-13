package com.example.thefloorislava;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

import static com.example.thefloorislava.GameView.screenRatioX;
import static com.example.thefloorislava.GameView.screenRatioY;

public class Character {

    public int x, y, width, height, characterDisplay = 1;
    public boolean isGoingLeft = false, isGoingRight = false, isJumping = false, isFalling = false, isOnGround = false, hasHitLeft = false, hasHitRight = false;
    public Bitmap character1, character2, character3, character4, character5, character6, character7, character8, character9, character10, character11, character12, character13, character14, character_dead;

    Character(int screenX, int screenY, Resources res) {
        character1 = BitmapFactory.decodeResource(res, R.drawable.character1);
        character2 = BitmapFactory.decodeResource(res, R.drawable.character2);
        character3 = BitmapFactory.decodeResource(res, R.drawable.character3);
        character4 = BitmapFactory.decodeResource(res, R.drawable.character4);
        character5 = BitmapFactory.decodeResource(res, R.drawable.character5);
        character6 = BitmapFactory.decodeResource(res, R.drawable.character6);
        character7 = BitmapFactory.decodeResource(res, R.drawable.character7);
        character8 = BitmapFactory.decodeResource(res, R.drawable.character8);
        character9 = BitmapFactory.decodeResource(res, R.drawable.character9);
        character10 = BitmapFactory.decodeResource(res, R.drawable.character10);
        character11 = BitmapFactory.decodeResource(res, R.drawable.character11);
        character12 = BitmapFactory.decodeResource(res, R.drawable.character12);
        character13 = BitmapFactory.decodeResource(res, R.drawable.character13);
        character14 = BitmapFactory.decodeResource(res, R.drawable.character14);
        character_dead = BitmapFactory.decodeResource(res, R.drawable.character_dead);

        width = character1.getWidth();
        height = character1.getHeight() - 13;

        character1 = Bitmap.createScaledBitmap(character1, width, height, false);
        character2 = Bitmap.createScaledBitmap(character2, width, height, false);
        character3 = Bitmap.createScaledBitmap(character3, width, height, false);
        character4 = Bitmap.createScaledBitmap(character4, width, height, false);
        character5 = Bitmap.createScaledBitmap(character5, width, height, false);
        character6 = Bitmap.createScaledBitmap(character6, width, height, false);
        character7 = Bitmap.createScaledBitmap(character7, width, height, false);
        character8 = Bitmap.createScaledBitmap(character8, width, height, false);
        character9 = Bitmap.createScaledBitmap(character9, width, height, false);
        character10 = Bitmap.createScaledBitmap(character10, width, height, false);
        character11 = Bitmap.createScaledBitmap(character11, width, height, false);
        character12 = Bitmap.createScaledBitmap(character12, width, height, false);
        character13 = Bitmap.createScaledBitmap(character13, width, height, false);
        character14 = Bitmap.createScaledBitmap(character14, width, height, false);
        character_dead = Bitmap.createScaledBitmap(character_dead, width, height, false);

        x = screenX/2;

        width *= (int) screenRatioX;
        height *= (int) screenRatioY;
    }

    Bitmap getCharacter() {
        switch(characterDisplay) {
            case 1:
                characterDisplay++;
                return character1;
            case 2:
                characterDisplay++;
                return character2;
            case 3:
                characterDisplay++;
                return character3;
            case 4:
                characterDisplay++;
                return character4;
            case 5:
                characterDisplay++;
                return character5;
            case 6:
                characterDisplay++;
                return character6;
            case 7:
                characterDisplay = 1;
                return character7;
            case 8:
                characterDisplay++;
                return character8;
            case 9:
                characterDisplay++;
                return character9;
            case 10:
                characterDisplay++;
                return character10;
            case 11:
                characterDisplay++;
                return character11;
            case 12:
                characterDisplay++;
                return character12;
            case 13:
                characterDisplay++;
                return character13;
            case 14:
                characterDisplay = 8;
                return character14;
            default:
                return character1;
        }
    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }

    public Bitmap getDeadCharacter() {
        return character_dead;
    }
}
