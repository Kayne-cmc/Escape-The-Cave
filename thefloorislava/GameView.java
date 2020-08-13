package com.example.thefloorislava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{


    public static final int GAP = 300, GRAVITY = 15;
    public int velocity = 135, fallingVelocity = 0, score = 0, tempScore = 0;
    private SharedPreferences prefs;
    public int[] gapstart;
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Background background1, background2;
    private Button left_arrow, right_arrow, up_arrow;
    private Character character;
    private Bitmap characterBitmap;
    private Obstacle[] obstacles;
    private Random random;
    private FireFloor fire;
    private Platform[] platforms;
    private Platform initialPlatform;
    private GameActivity activity;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this.screenX = screenX;
        this.screenY = screenY;
        this.activity = activity;
        screenRatioX = 1080f / screenX;
        screenRatioY = 1794f / screenY;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        background1 = new Background(screenX, screenY, getResources(), R.drawable.background);
        background2 = new Background(screenX, screenY, getResources(), R.drawable.background_inverted);
        left_arrow = new Button(getResources(), R.drawable.left_arrow);
        right_arrow = new Button(getResources(), R.drawable.right_arrow);
        up_arrow = new Button(getResources(), R.drawable.up_arrow);
        character = new Character(screenX, screenY, getResources());
        paint = new Paint();
        paint.setTextSize(64);
        paint.setColor(Color.WHITE);
        random = new Random();
        obstacles = new Obstacle[3];
        for (int i = 0; i < obstacles.length; i++) {
            Obstacle o = new Obstacle(screenY, getResources());
            obstacles[i] = o;
        }
        fire = new FireFloor(screenX, screenY, getResources());
        platforms = new Platform[3];
        initialPlatform = new Platform(getResources());

        //platforms[i].y
        for (int i = 0; i < platforms.length ; i++) {
            Platform p = new Platform(getResources());
            if (i == 0) {
                p.y = -p.height;
            }
            else {
                p.y = platforms[i-1].y + (int)(600 * screenRatioY);
            }
            platforms[i] = p;
        }
        gapstart = new int[3];

        //platforms[i].rightx and platforms[i].leftx
        for (int i = 0; i < gapstart.length; i++) {
            gapstart[i] = random.nextInt(screenX - 350);
            if (gapstart[i] < character.width) {
                gapstart[i] = character.width;
            }
            if (i == 2) {
                platforms[i].leftx = -platforms[i].width;
                platforms[i].rightx = screenX;
            }
            else {
                platforms[i].leftx = gapstart[i] - platforms[i].width;
                platforms[i].rightx = gapstart[i] + GAP;
            }

        }
        initialPlatform.y = platforms[2].y;
        initialPlatform.leftx = 0;
        character.y = initialPlatform.y - character.height;
        background2.y = -screenY;
        left_arrow.x = (int)(80 * screenRatioX);
        right_arrow.x = (int)((left_arrow.x + left_arrow.width + 100) * screenRatioX);
        up_arrow.x = (int)(800 * screenRatioX);
    }

    @Override
    public void run() {
        while(isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        background1.y += 13 * screenRatioY;
        background2.y += 13 * screenRatioY;
        characterBitmap = character.getCharacter();

        // Running Background
        if (background1.y - background1.background.getHeight() > 0) {
            background1.y = -screenY;
        }
        if (background2.y - background2.background.getHeight() > 0) {
            background2.y = -screenY;
        }

        // Making Gaps and platforms
        for (int i = 0; i < platforms.length; i++) {
            platforms[i].y += 5 * screenRatioY;
            if (platforms[i].y + platforms[i].height > screenY) {
                platforms[i].y = -platforms[i].height;
                gapstart[i] = random.nextInt(screenX - 350);
                if (gapstart[i] < character.width) {
                    gapstart[i] = character.width;
                }
                platforms[i].leftx = gapstart[i] - platforms[i].width;
                platforms[i].rightx = gapstart[i] + GAP;
            }
        }

        if (character.isJumping) {
            character.y -= velocity;
            velocity -= GRAVITY;
        }

        for (int i = 0; i < platforms.length; i++) {
            if (character.getCollisionShape().intersect(platforms[i].getLeftCollisionShape()) || character.getCollisionShape().intersect(platforms[i].getRightCollisionShape())) {
                if (character.y < platforms[i].y) {
                    character.y = platforms[i].y - character.height;
                    character.isOnGround = true;
                    break;
                }
                if (character.y > platforms[i].y) {
                    character.y = platforms[i].y + platforms[i].height;
                    character.isFalling = true;
                    break;
                }
            }
            if ((character.y + character.height == platforms[i].y) && ((character.x + character.width/2) > platforms[i].rightx || (character.x + character.width/2) < gapstart[i])) {
                character.isOnGround = true;
                break;
            }
            if (character.getCollisionShape().intersect(initialPlatform.getLeftCollisionShape())) {
                if (character.y < initialPlatform.y) {
                    character.y = initialPlatform.y - character.height;
                    character.isOnGround = true;
                    break;
                }
                if (character.y > initialPlatform.y) {
                    character.y = initialPlatform.y + initialPlatform.height;
                    character.isFalling = true;
                    break;
                }
            }
            if (character.y + character.height == initialPlatform.y) {
                character.isOnGround = true;
                break;
            }
            character.isOnGround = false;
//            character.isFalling = !character.isJumping;
        }

        for (int i = 0; i < platforms.length; i++) {
            if (character.getCollisionShape().intersect(platforms[i].getLeftCollisionShape())) {
                if ((character.x + character.width) > (platforms[i].leftx + platforms[i].width)) {
                    character.x = platforms[i].leftx + platforms[i].width;
                    character.hasHitLeft = true;
                    break;
                }
            }
            character.hasHitLeft = false;
        }
        for (int i = 0; i < platforms.length; i++) {
            if(character.getCollisionShape().intersect(platforms[i].getRightCollisionShape())) {
                if (character.x < platforms[i].rightx) {
                    character.x = platforms[i].rightx - character.width;
                    character.hasHitRight = true;
                    break;
                }
            }
            character.hasHitRight = false;
        }

        if (character.isFalling || (!character.isOnGround && !character.isJumping)) {
            character.isJumping = false;
            velocity = 135;
            character.y += fallingVelocity;
            fallingVelocity += GRAVITY;
        }

        if (character.isOnGround) {
            character.isFalling = false;
            character.isJumping = false;
            velocity = 135;
            fallingVelocity = 0;
            character.y += 5 * screenRatioY;
        }
        initialPlatform.y += 5 * screenRatioY;

        // Going Left
        if (character.isGoingLeft) {
            if (character.characterDisplay < 7) {
                character.characterDisplay = 8;
            }
            if (character.x > 0 && !character.hasHitLeft) {
                character.x -= 30 * screenRatioX;
            }
        }

        // Going Right
        if (character.isGoingRight) {
            if(character.characterDisplay > 7) {
                character.characterDisplay = 1;
            }
            if (character.x < (background1.background.getWidth() - character.width) && !character.hasHitRight) {
                character.x += 30 * screenRatioX;
            }
        }

        // Character Top of Screen
        if (character.y < 0) {
            character.y = 5;
        }

        // Obstacles Falling
        for (Obstacle obstacle : obstacles) {
            obstacle.y += obstacle.speed;
            if (obstacle.y + obstacle.height > screenY) {
                obstacle.speed = random.nextInt((int)(40*screenRatioY));
                if (obstacle.speed < 20 * screenRatioY) {
                    obstacle.speed = (int)(20*screenRatioY);
                }
                obstacle.y = -obstacle.height;
                obstacle.x = random.nextInt(screenX - obstacle.width);
            }
            if(intersect(characterBitmap, character.x, character.y, obstacle.obstacle, obstacle.x, obstacle.y)) {
                isPlaying = false;
            }
        }

        if(Rect.intersects(fire.getCollisionShape(), character.getCollisionShape())) {
            isPlaying = false;
        }

        tempScore = Math.abs((character.y + character.height - initialPlatform.y)/20);
        if (tempScore > score) {
            score = tempScore;
        }
    }

    private void draw() {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            if (!isPlaying) {
                canvas.drawBitmap(character.getDeadCharacter(), (character.x - 38), character.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(initialPlatform.left, initialPlatform.leftx, initialPlatform.y, paint);
            for (int i = 0; i < platforms.length; i++) {
                if (platforms[i].y > initialPlatform.y) {
                    break;
                }
                canvas.drawBitmap(platforms[i].left, platforms[i].leftx, platforms[i].y, paint);
                canvas.drawBitmap(platforms[i].right, platforms[i].rightx, platforms[i].y, paint);
            }

            canvas.drawBitmap(characterBitmap, character.x, character.y, paint);
            for (Obstacle obstacle : obstacles) {
                canvas.drawBitmap(obstacle.obstacle, obstacle.x, obstacle.y, paint);
            }
            canvas.drawBitmap(fire.floor, fire.x, fire.y, paint);
            canvas.drawBitmap(left_arrow.button, left_arrow.x, left_arrow.y,paint);
            canvas.drawBitmap(right_arrow.button, right_arrow.x, right_arrow.y,paint);
            canvas.drawBitmap(up_arrow.button, up_arrow.x, up_arrow.y, paint);
            canvas.drawText("Score: " + score, (screenX - paint.measureText("Score: " + score))/2, paint.getTextSize() + 40, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() >= left_arrow.x && event.getX() <= (left_arrow.x + left_arrow.width) && event.getY() >= left_arrow.y && event.getY() <= (left_arrow.y + left_arrow.height)) {
                    character.isGoingLeft = true;
                }
                if (event.getX() >= right_arrow.x && event.getX() <= (right_arrow.x + right_arrow.width) && event.getY() >= right_arrow.y && event.getY() <= (right_arrow.y + right_arrow.height)) {
                    character.isGoingRight = true;
                }
                if (event.getX() >= up_arrow.x && event.getX() <= (up_arrow.x + up_arrow.width) && event.getY() >= up_arrow.y && event.getY() <= (up_arrow.y + up_arrow.height)) {
                    character.isJumping = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                character.isGoingLeft = false;
                character.isGoingRight = false;
        }
        return true;
    }

    public static boolean intersect(Bitmap bitmap1, int x1, int y1, Bitmap bitmap2, int x2, int y2) {
        Rect bounds1 = new Rect(x1, y1, x1 + bitmap1.getWidth(), y1 + bitmap1.getHeight());
        Rect bounds2 = new Rect(x2, y2, x2 + bitmap2.getWidth(), y2 + bitmap2.getHeight());

        if (bounds1.intersect(bounds2)) {
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = bitmap1.getPixel(i-x1, j-y1);
                    int bitmap2Pixel = bitmap2.getPixel(i-x2, j-y2);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = (int)Math.max(rect1.left, rect2.left);
        int right = (int)Math.min(rect1.right, rect2.right);
        int top = (int)Math.max(rect1.top, rect2.top);
        int bottom = (int)Math.min(rect1.bottom, rect2.bottom);
        return new Rect (left, top, right, bottom);
    }

    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }

    public void saveHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    public void waitBeforeExiting() {
        try {
            Thread.sleep(2000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}