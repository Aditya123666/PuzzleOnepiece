package com.marco.beautypuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * User: KdMobiB
 * Date: 2016/7/5
 * Time: 17:38
 */
public class WelcomeActivity extends Activity {
    private ViewPager vp;
    private ArrayList<ImageView> pages = new ArrayList<>();
    int[] imgIds = new int[]{R.mipmap.banner_1, R.mipmap.banner_2, R.mipmap.banner_3};
    private Button       btnSub;
    private AnimationSet set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        vp = (ViewPager) findViewById(R.id.vp);
        btnSub = (Button) findViewById(R.id.btnSub);
        initPages();
        initAdapter();
        initBtnAnimation();
    }

    private void initBtnAnimation() {
        set = new AnimationSet(true);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        RotateAnimation rotate = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(alpha);
        set.addAnimation(rotate);
        set.setDuration(1000);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btnSub.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initPages() {
        pages.clear();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(imgIds[i]);
            pages.add(image);
        }
    }

    private void initAdapter() {
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pages.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pages.get(position));
                return pages.get(position);
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == pages.size() - 1) {
                    btnSub.startAnimation(set);
                } else {
                    btnSub.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        });
    }
}
