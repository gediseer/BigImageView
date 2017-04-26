package cn.dream.mao.bigimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;


public class BigImageView extends ImageView implements GestureDetector.OnGestureListener {
    BitmapRegionDecoder bitmapRegionDecoder;
    Rect showRect;
    Rect imgRect;
    Rect ensuranceRect;
    String srcName;
    InputStream srcStream;

    int srcWid;
    int srcHei;
    float destWid;
    float destHei;

    // private Bitmap currentBitmap;
    private BitmapFactory.Options opt;

    GestureDetectorCompat gestureDetector;

    public BigImageView(Context context) {
        this(context, null);

    }

    public BigImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        gestureDetector = new GestureDetectorCompat(getContext(), this);

        try {
            srcStream = context.getAssets().open(srcName);
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(srcStream, false);
        } catch (IOException e) {
            Log.e("test", "读取assets文件出错");
            e.printStackTrace();
        }
        getDimension();
        showSrc();


    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray typedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.BigImageView, defStyleAttr, 0);
            srcName = typedArray.getString(R.styleable.BigImageView_srcImage);
            destHei = (int) typedArray.getDimension(R.styleable.BigImageView_destHei, 50);
            destWid = (int) typedArray.getDimension(R.styleable.BigImageView_destWid, 50);
            l.i("srcName=" + srcName);

            typedArray.recycle();
        }
    }

    private void getDimension() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;//只解析，不分配内存
        BitmapFactory.decodeStream(srcStream, null, opt);
        srcWid = opt.outWidth;
        srcHei = opt.outHeight;
        l.i("解析到的图片大小是" + srcWid + " 高：" + srcHei);
        showRect = new Rect(0, 0, (int) destWid, (int) destHei);//先把高设置为800
        imgRect = new Rect(0, 0, srcWid, srcHei);

        l.i("centerX=" + showRect.centerX() + " centerY" + showRect.centerY());

    }

    private void showSrc() {
//        l.i("绘制新区域");
        // currentBitmap = bitmapRegionDecoder.decodeRegion(showRect, opt);
        setImageBitmap(bitmapRegionDecoder.decodeRegion(showRect, opt));

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //  l.i("action=" + event.getAction());
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // showSrc();
        super.onDraw(canvas);
    }

    //手势监听方法
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        l.i(" onDown " + motionEvent.toString());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        l.i(" onShowPress " + motionEvent.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        l.i(" onSingleTapUp " + motionEvent.toString());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        // l.i(" onScroll " + motionEvent.toString());
        isOutsetImage((int) v, (int) v1 * 2);

        showSrc();
        invalidate();
        return false;
    }

    private void isOutsetImage(int dx, int dy) {
        int x = dx, y = dy;
        if ((showRect.centerX() + dx > imgRect.right - showRect.width() / 2) || (showRect.centerX() + dx < imgRect.left + showRect.width() / 2)) {//从右边超
            l.i("水平方向上超过了");
            x = 0;
        }
        if ((showRect.centerY() + dy > imgRect.bottom - showRect.height() / 2) || (showRect.centerY() + dy < imgRect.top + showRect.height() / 2)) {
            l.i("竖直方向上超过了");
            y = 0;
        }
        showRect.offset(x, y);

    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        l.i(" onLongPress " + motionEvent.toString());

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        l.i(" onFling " + motionEvent.toString());
        return false;
    }
}
