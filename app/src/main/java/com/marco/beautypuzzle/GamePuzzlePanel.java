package com.marco.beautypuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.*;

/**
 * User: KdMobiB
 * Date: 2016/7/4
 * Time: 11:11
 */
public class GamePuzzlePanel extends RelativeLayout implements View.OnClickListener {
    private int mColumn  = 3;
    private int mPadding = 3;
    private int mMargin  = 3;
    private ImageView[] itemImageViews;
    private int         itemWidth;
    private Bitmap      mBitmap;//界面图片
    private List<ImagePiece> itemPieces = new ArrayList<>();
    private boolean          once       = false;
    private int mWidth;//游戏面板宽度

    private GamePuzzleListener mListener;

    public GamePuzzlePanel(Context context) {
        this(context, null);
    }

    public GamePuzzlePanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePuzzlePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化对象
     */
    public void init() {
        mMargin = dip2Px(3);
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingBottom(), getPaddingRight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //取宽和高之间的最小值
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!once) {
            initBitmap();
            initItem();
        } once = true;

    }

    /**
     * 切图以及排序
     */
    public void initBitmap() {
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_21);
        }
        itemPieces = ImageSpliterUtil.spiteImage(mBitmap, mColumn);

        //乱序
        Collections.sort(itemPieces, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 设置ImageViewItem的宽高属性
     */
    private void initItem() {
        itemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        itemImageViews = new ImageView[mColumn * mColumn];

        //生成ImageViewItem
        for (int i = 0; i < itemImageViews.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(itemPieces.get(i).getBitmap());

            itemImageViews[i] = item;
            item.setId(i + 1);

            // 在Item的tag中存储了index
            item.setTag(i + "_" + itemPieces.get(i).getIndex());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    itemWidth, itemWidth);

            // 设置Item间横向间隙，通过rightMargin
            // 不是最后一列
            if ((i + 1) % mColumn != 0) {
                lp.rightMargin = mMargin;
            }
            // 不是第一列
            if (i % mColumn != 0) {
                lp.addRule(
                        RelativeLayout.RIGHT_OF,
                        itemImageViews[i - 1].getId()
                          );
            }
            // 如果不是第一行 , 设置topMargin和rule
            if ((i + 1) > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(
                        RelativeLayout.BELOW,
                        itemImageViews[i - mColumn].getId()
                          );
            }
            addView(item, lp);
        }
    }

    private ImageView mFirst, mSecond;

    @Override
    public void onClick(View v) {
        if (isaniming || isPause) return;

        //两次点击高亮
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }

        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55000000"));
        } else {
            mSecond = (ImageView) v;
            exchangeView();
        }
    }

    /**
     * 交换图片
     */
    private void exchangeView() {
        mFirst.setColorFilter(null);
        setUpAnimationLayout();

        final String firstTag = (String) mFirst.getTag();
        final String secondTag = (String) mSecond.getTag();

        String[] firstParams = firstTag.split("_");
        String[] secondParams = secondTag.split("_");

        final Bitmap firstBitmap = itemPieces.get(Integer.valueOf(firstParams[0])).getBitmap();
        final Bitmap secondBitmap = itemPieces.get(Integer.valueOf(secondParams[0])).getBitmap();

        final ImageView first = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(itemWidth, itemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp);
        first.setImageBitmap(firstBitmap);
        animLayout.addView(first);

        final ImageView second = new ImageView(getContext());
        LayoutParams lp2 = new LayoutParams(itemWidth, itemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        second.setImageBitmap(secondBitmap);
        animLayout.addView(second);

        TranslateAnimation animFirst = new TranslateAnimation(0, mSecond.getLeft() - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        animFirst.setDuration(200);
        animFirst.setFillAfter(true);
        first.startAnimation(animFirst);

        TranslateAnimation animSecond = new TranslateAnimation(0, mFirst.getLeft() - mSecond.getLeft(), 0, mFirst.getTop() - mSecond.getTop());
        animSecond.setDuration(200);
        animSecond.setFillAfter(true);
        second.startAnimation(animSecond);

        animFirst.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isaniming = true;
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFirst.setImageBitmap(secondBitmap);
                mSecond.setImageBitmap(firstBitmap);

                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst = mSecond = null;

                first.setVisibility(GONE);
                second.setVisibility(GONE);
                isaniming = false;

                checkFinish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public boolean isGameOver, isGameSuccess, isPause = false;

    private void checkFinish() {
        isGameSuccess = true;
        for (int i = 0; i < itemImageViews.length; i++) {
            String tag = (String) itemImageViews[i].getTag();
            if (getImageIndexByTag(tag) != i) {
                isGameSuccess = false;
            }
        }

        if (isGameSuccess) {
            isGameSuccess = true;
            mHandler.removeMessages(TIME_CHANGE);
            if (mListener != null) {
                mListener.gamesuccess();
            }
        }
    }

    /**
     * 根据Tag获取ImagePiece的index
     *
     * @param tag
     * @return
     */
    private int getImageIndexByTag(String tag) {
        String[] spite = tag.split("_");
        return Integer.valueOf(spite[1]);
    }

    /**
     * 根据Tag获取ImagePiece的index
     *
     * @param tag
     * @return
     */
    private int getImageIdByTag(String tag) {
        String[] spite = tag.split("_");
        return Integer.valueOf(spite[0]);
    }


    /**
     * 获取多个参数的最小值
     *
     * @param parames
     */
    public int min(int... parames) {
        int min = parames[0];
        for (int parame : parames) {
            if (parame < min) {
                min = parame;
            }
        }
        return min;
    }

    /**
     * dp转化成px
     *
     * @param value
     * @return
     */
    public int dip2Px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    /**
     * 设置动画层
     */
    private RelativeLayout animLayout;
    private boolean isaniming = false;

    private void setUpAnimationLayout() {
        if (animLayout == null) {
            animLayout = new RelativeLayout(getContext());
            animLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(animLayout);
        } else {
            if (animLayout.getChildCount() > 0) {
                animLayout.removeAllViews();
            }
        }
    }

    private boolean timeEnable = true;
    private int mTime;

    public GamePuzzleListener getmListener() {
        return mListener;
    }

    public void setmListener(GamePuzzleListener mListener) {
        this.mListener = mListener;
    }

    public static final int     TIME_CHANGE = 0X110;
    public static final int     NEXT_LEVEL  = 0X111;
    public              int     level       = 0;
    public              Handler mHandler    = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_CHANGE:
                    if (isGameOver || isGameSuccess || isPause || mListener == null || !timeEnable) {
                        return;
                    }

                    if (mTime == 0) {
                        isGameOver = true;
                        mListener.gameover();
                        return;
                    }

                    mTime--;
                    mListener.timechanged(mTime);
                    break;
                case NEXT_LEVEL:
                    level++;
                    if (mListener != null) {
                        mHandler.removeMessages(TIME_CHANGE);
                        mListener.nextLevel(level);
                    } else {
                        nextLevel();
                    }
                    break;
            }
        }
    };

    /**
     * 下一关
     */
    public void nextLevel() {
        timer.cancel();
        this.removeAllViews();
        animLayout = null;
        level++;
        setmBitmap(BitmapFactory.decodeResource(getResources(), Contants.imgIds[level % Contants.imgIds.length]));
        double count = Math.random();
        if (count > 0.7) {
            mColumn = 5;
        } else if (count > 0.2) {
            mColumn = 4;
        } else {
            mColumn = 3;
        }

        isGameSuccess = isGameOver = isPause = false;
        checkTimeEnable();
        initBitmap();
        initItem();
    }

    private void checkTimeEnable() {
        if (timeEnable) {
            // 根据当前等级设置时间
            countTimeBaseLevel();
            initTimer();
        }
    }

    public boolean isTimeEnable() {
        return timeEnable;
    }

    public void setTimeEnable(boolean timeEnable) {
        this.timeEnable = timeEnable;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    private void countTimeBaseLevel() {
        mTime = (int) Math.pow(2, mColumn - 1) * 60;
    }

    /**
     * 暂停游戏
     */
    public void pause() {
        isPause = true;
    }

    /**
     * 恢复游戏
     */
    public void resume() {
        if (isPause) {
            isPause = false;
        }
    }

    /**
     * 重新开始这一关
     */
    public void restart() {
        this.removeAllViews();
        timer.cancel();
        animLayout = null;
        isGameOver = false;
        isGameSuccess = false;
        isPause = false;
        timeEnable = true;
        checkTimeEnable();
        initItem();
    }

    /**
     * 启动
     */
    public void start() {
        checkTimeEnable();
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    private Timer timer = new Timer();

    /**
     * 重置定时器
     */
    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPause) {
                    mHandler.sendEmptyMessage(TIME_CHANGE);
                }
            }
        }, 0, 1000);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
