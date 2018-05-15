package com.santhosh.onboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.novoda.spritz.Spritz;
import com.novoda.spritz.SpritzStep;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    MyViewPagerAdapter adapter;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    private int dotsCount;
    private ImageView[] dots;

    private Spritz spritz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        spritz = Spritz
                .with(animationView)
                .withSteps(
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .withSwipeDuration(500, TimeUnit.MILLISECONDS)
                                .build(),
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .withSwipeDuration(500, TimeUnit.MILLISECONDS)
                                .build(),
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .build()
                )
                .build();


        List<String> list = new ArrayList<>();
        list.add(getString(R.string.lorum));
        list.add(getString(R.string.lorum));
        list.add(getString(R.string.lorum));

        adapter = new MyViewPagerAdapter(this, list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        addBottomDots();

    }


    @Override
    protected void onStart() {
        super.onStart();
        spritz.attachTo(viewPager);
        spritz.startPendingAnimations();
    }

    @Override
    protected void onStop() {
        spritz.detachFrom(viewPager);
        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_unselected));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_selected));

        viewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void addBottomDots() {
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];
        if (dotsCount == 0)
            return;

        layoutDots.removeAllViews();

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 4, 4, 4);

            layoutDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_selected));
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        List<String> list;
        Context context;

        MyViewPagerAdapter(Context context, List<String> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item, container, false);
            String walk = list.get(position);

            TextView description = (TextView) itemView.findViewById(R.id.description);

            description.setText(walk);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
