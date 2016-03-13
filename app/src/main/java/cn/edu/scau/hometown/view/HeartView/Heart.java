package cn.edu.scau.hometown.view.HeartView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/3/6.
 */
public class Heart {

    // x轴坐标
    private float x;
    // y轴坐标
    private float y;
    // 初始旋转角度
    private float rotation;
    // 方向下落速度
    private float Yspeed;
    //X方向平移速度
    private float Xspeed;//旋转方向
    private int rotationDirection;
    //平移方向，左或右
    private int direction;
    // 旋转速度
    private float rotationSpeed;

    private Bitmap bitmap;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private Context context;

    private int screenWidth;

    Heart(Context context) {
        this.context = context;
        initScreenWidth();
    }

    public float getXspeed() {
        return Xspeed;
    }

    private void initScreenWidth() {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
    }

    public void setXspeed(float xspeed) {
        Xspeed = xspeed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    public int getDirection() {
        return direction;
    }
    public void setRotationDirection(int rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    public int getRotationDirection() {
        return rotationDirection;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;

    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getYSpeed() {
        return Yspeed;
    }

    public void setYSpeed(float speed) {
        this.Yspeed = speed;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void resetXY() {
        setX((float) Math.random() * screenWidth);
        setY(0f);

    }

    public void resetXDirection() {
        if (getX() > screenWidth / 2) {
            setDirection(LEFT);
        } else {
            setDirection(RIGHT);
        }
    }

    public void resetXSpeed() {
        float Xspeed = 0.5f;
        setXspeed(getDirection() == LEFT ? -Xspeed : Xspeed);

    }

    public void reversalRotationSpeed() {
        setRotationSpeed(-getRotationSpeed());

    }

}