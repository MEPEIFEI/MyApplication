package com.lmapp.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Administrator on 2016/7/1 0001.
 */
public class Activity_main extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private TextView tv_liaoliao;
    private TextView tv_rourou;
    private TextView tv_jiyou;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setUpToolbar();
        mActionBar.setDisplayHomeAsUpEnabled(false);
        setTitle("撩了么");

        init_view();
        event_view();
        setSelect(0);
    }

    private void event_view() {
        tv_liaoliao.setOnClickListener(this);
        tv_rourou.setOnClickListener(this);
        tv_jiyou.setOnClickListener(this);
    }

    private void init_view() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tv_liaoliao = (TextView) findViewById(R.id.tv_liaoliao);
        tv_rourou = (TextView) findViewById(R.id.tv_rourou);
        tv_jiyou = (TextView) findViewById(R.id.tv_jiyou);

        mFragments = new ArrayList<>();

        mFragments.add(new LiaoliaoFragment());
        mFragments.add(new rourouFragment());
        mFragments.add(new jiyouFragment());

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_liaoliao:
                setSelect(0);
                break;
            case R.id.tv_rourou:
                setSelect(1);
                break;
            case R.id.tv_jiyou:
                setSelect(2);
                break;
            default:
                break;
        }
    }

    private void setSelect(int i) {
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    private void resetTextColor() {
        tv_liaoliao.setTextColor(0xff8e8e8e);
        tv_rourou.setTextColor(0xff8e8e8e);
        tv_jiyou.setTextColor(0xff8e8e8e);
    }

    private void setTab(int i) {
        resetTextColor();
        switch (i) {
            case 0:
                tv_liaoliao.setTextColor(0xff3F51B5);
                break;
            case 1:
                tv_rourou.setTextColor(0xff3F51B5);
                break;
            case 2:
                tv_jiyou.setTextColor(0xff3F51B5);
                break;
            default:
                break;
        }
    }
}
