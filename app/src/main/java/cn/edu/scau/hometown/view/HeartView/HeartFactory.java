package cn.edu.scau.hometown.view.HeartView;

import android.content.Context;
import android.graphics.Bitmap;


/**
 * Created by Administrator on 2016/3/6.
 */
public class HeartFactory {

    private HeartFactory() {

    }

    public static Heart createHeart(Bitmap originalBitmap, Context context) {
        Heart heart = new Heart(context);
        heart.resetXY();
        heart.resetXDirection();

        // 设置Y速度
        heart.setYSpeed(1f);
        // 设置X速度
        heart.resetXSpeed();
        // 设置初始旋转角度
        heart.setRotation((float) Math.random() * 90 - 45);

        if (heart.getRotation() > 0) {
            heart.setRotationDirection(Heart.LEFT);
        } else {
            heart.setRotationDirection(Heart.RIGHT);
        }
        float RotationSpeed = 0.3f;
        // 设置旋转速度
        heart.setRotationSpeed(heart.getRotationDirection() == Heart.LEFT ? -RotationSpeed : RotationSpeed);
        // 设置图片
        heart.setBitmap(originalBitmap);
        return heart;
    }

}