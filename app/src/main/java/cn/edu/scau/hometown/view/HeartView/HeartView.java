package cn.edu.scau.hometown.view.HeartView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.scau.hometown.R;

/**
 * Created by hiai on 2016/3/6.
 * 红心散落动画,根据需要加载
 */
public class HeartView extends View implements Runnable {


    private Bitmap mBitmap;

    List<Heart> hearts = new ArrayList();

    private Paint mPaint = new Paint();

    private static final int heartNumber = 15;

    private static final String TAG = "HeartView";

    private Matrix mMatrix = new Matrix();

    private boolean isCompleted = false;

    private int[] appearOrder;

    private int order = 0;

    private Timer timer;

    private Context context;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        initAppearOrder();
        createHearts();
        timer = new Timer(true);
        timer.schedule(task, 1000, 1000);
        new Thread(this).start();
    }

    private void createHearts() {
        for (int i = 0; i < heartNumber; ++i) {
            hearts.add(HeartFactory.createHeart(mBitmap, context));
        }

    }

    private void initAppearOrder() {
        int min = Integer.MAX_VALUE;
        appearOrder = new int[heartNumber];
        for (int i = 0; i < heartNumber; i++) {
            appearOrder[i] = (int) (Math.random() * heartNumber);
            if (appearOrder[i] < min) {
                min = appearOrder[i];
            }
        }
        order = min;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (!isCompleted) {
            for (int j = 0; j < heartNumber; j++) {
                if (appearOrder[j] == -1) {
                    toAppear(canvas, j);
                }
            }
        } else {
            for (int i = 0; i < heartNumber; i++) {
                toAppear(canvas, i);
            }
        }
        canvas.restore();
    }

    private void toAppear(Canvas canvas, int i) {
        Heart heart = hearts.get(i);

        mMatrix.setTranslate(0, 0);
        mMatrix.postRotate(heart.getRotation());
        mMatrix.postTranslate(heart.getX(), heart.getY());

        canvas.drawBitmap(heart.getBitmap(), mMatrix, mPaint);

        heart.setY(heart.getY() + heart.getYSpeed());
        //超出屏膜，重新进入
        if (heart.getY() > getHeight()) {
            heart.resetXY();
            heart.resetXDirection();
            heart.resetXSpeed();
        }

        heart.setX(heart.getX() + heart.getXspeed());
        //超出屏膜，重新进入
        if (heart.getX() < 0 || heart.getX() > getWidth()) {
            heart.resetXY();
            heart.resetXDirection();
            heart.resetXSpeed();
        }

        heart.setRotation(heart.getRotation() + heart.getRotationSpeed());

        if (heart.getRotation() < -45 || heart.getRotation() > 45) {
            heart.reversalRotationSpeed();
        }
    }


    @Override
    public void run() {

        while (true) {

            this.post(new Runnable() {
                @Override
                public void run() {
                    if (isCompleted) {
                        timer.cancel();
                    }
                    invalidate();
                }
            });
            try {
                //每秒更新60次
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    TimerTask task = new TimerTask() {
        public void run() {
            HeartView.this.post(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < heartNumber; j++) {
                        if (appearOrder[j] == order) {
                            appearOrder[j] = -1;
                        }

                    }
                    order++;
                    if (order == heartNumber) {
                        isCompleted = true;
                    }
                }
            });
        }
    };


}