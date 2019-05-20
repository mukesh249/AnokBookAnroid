package com.anokbook.Activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.anokbook.R;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.security_toolbar)
    Toolbar securityToolbar;
    private PreferenceManager preferenceManager;
    //    LinearLayout dots;
    TextView[] bottomBars;
    int[] screens;
    TextView Skip, Next;
    ViewPager vp;
    MyViewPagerAdapter myvpAdapter;
    private int dotscount;
    private ImageView[] dots;
//    LinearLayout dotsIndicator;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        unbinder = ButterKnife.bind(this,this);
        setSupportActionBar(securityToolbar);

        getSupportActionBar().setTitle("");

        //toolbar back button color and icon change
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(Color.parseColor("#2f3e9e"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        vp = (ViewPager) findViewById(R.id.view_pager);
        WormDotsIndicator dotsIndicator = (WormDotsIndicator) findViewById(R.id.dots_indicator);
//        dotsIndicator = (LinearLayout) findViewById(R.id.dots_indicator);
        Skip = (TextView) findViewById(R.id.skip);
        Next = (TextView) findViewById(R.id.next);


        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, Login.class);
                startActivity(intent);
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Next.getText().toString().compareTo(getResources().getString(R.string.let_s_start))==0){
                    Intent intent = new Intent(IntroActivity.this, Login.class);
                    startActivity(intent);
                }else{
                    vp.setCurrentItem(myvpAdapter.getItem(+1), true);
                }
            }
        });

//        preferenceManager = new PreferenceManager(this);
        vp.addOnPageChangeListener(viewPagerPageChangeListener);
//        if (!preferenceManager.FirstLaunch()) {
//            launchMain();
//            finish();
//        }
        screens = new int[]{
                R.layout.intro_screen1,
                R.layout.intro_screen2,
                R.layout.intro_screen3,
                R.layout.intro_screen4,
                R.layout.intro_screen5,
                R.layout.intro_screen6
        };

        myvpAdapter = new MyViewPagerAdapter();
        vp.setAdapter(myvpAdapter);
        dotsIndicator.setViewPager(vp);
        dotscount = myvpAdapter.getCount();
        dots = new ImageView[dotscount];

    }




    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            nextButtonBehaviour(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @SuppressWarnings("PointlessBooleanExpression")
    private void nextButtonBehaviour(final int position) {
//        boolean hasPermissionToGrant = fragment.hasNeededPermissionsToGrant();
         if (myvpAdapter.isLastSlide(position)) {
            Next.setText(getResources().getString(R.string.let_s_start));
        } else {
             Next.setText(getResources().getString(R.string.next));
             Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Next.getText().toString().compareTo(getResources().getString(R.string.let_s_start))==0){
                        Intent intent = new Intent(IntroActivity.this, Login.class);
                        startActivity(intent);
                    }else{
                        vp.setCurrentItem(myvpAdapter.getItem(+1), true);
                    }
                }
            });
        }
    }

//    private void performFinish() {
//        onFinish();
//        finish();
//    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }
        private int getItem(int i) {
            return vp.getCurrentItem() + i;
        }

        @Override
        public int getCount() {
            return screens.length;
        }

//        public void addItem(SlideFragment fragment) {
//            fragments.add(getCount(), fragment);
//            notifyDataSetChanged();
//        }

        public int getLastItemPosition() {
            return getCount() - 1;
        }

        public boolean isLastSlide(int position) {
            return position == getCount() - 1;
        }

//        public boolean shouldFinish(int position) {
//            return position == getCount() && getItem(getCount() - 1).canMoveFurther();
//        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(View v, Object object) {
            return v == object;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
