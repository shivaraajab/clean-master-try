package com.balaganovrocks.yourmasterclean.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.balaganovrocks.yourmasterclean.base.BaseActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.balaganovrocks.yourmasterclean.R;
import com.balaganovrocks.yourmasterclean.base.ActivityTack;
import com.balaganovrocks.yourmasterclean.base.BaseActivity;
import com.balaganovrocks.yourmasterclean.fragment.MainFragment;
import com.balaganovrocks.yourmasterclean.fragment.NavigationDrawerFragment;
import com.balaganovrocks.yourmasterclean.fragment.RelaxFragment;
import com.balaganovrocks.yourmasterclean.fragment.SettingsFragment;
import com.balaganovrocks.yourmasterclean.utils.SystemBarTintManager;
import com.balaganovrocks.yourmasterclean.utils.T;
import com.balaganovrocks.yourmasterclean.utils.UIElementsHelper;

import java.util.Date;

import butterknife.InjectView;


public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    @InjectView(R.id.container)
    FrameLayout container;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    ActionBar ab;
    private InterstitialAd mInterstitialAd;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;
    NavigationDrawerFragment mNavigationDrawerFragment;
    private View mFragmentContainerView;

    MainFragment mMainFragment;
    RelaxFragment mRelaxFragment;

    public static final long TWO_SECOND = 2 * 1000;
    long preTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentContainerView = (View) findViewById(R.id.navigation_drawer);
        mTitle = getTitle();
       applyKitKatTranslucency();

        onNavigationDrawerItemSelected(0);
        initDrawer();
        MobileAds.initialize(this, "ca-app-pub-4369038195513432/2986510514");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4369038195513432/2986510514");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MyApp myApp = new MyApp();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private void initDrawer() {
        // TODO Auto-generated method stub
        ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);// ???home icon????????????????????????????????????
        ab.setHomeButtonEnabled(true);     // ??????api level 14 ??????home-icon ?????????

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
//        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
//                mDrawerLayout);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            } else {
                mDrawerLayout.openDrawer(mFragmentContainerView);
            }
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            // mTintManager.setTintColor(0xF00099CC);

            mTintManager.setTintDrawable(UIElementsHelper
                    .getGeneralActionBarBackground(this));

            getActionBar().setBackgroundDrawable(
                    UIElementsHelper.getGeneralActionBarBackground(this));

        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // ????????????Fragment??????
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // ?????????????????????Fragment?????????????????????Fragment???????????????????????????
        hideFragments(transaction);

        switch (position) {
            case 0:
                closeDrawer();
                if (mMainFragment == null) {
                    mMainFragment = new MainFragment();
                    transaction.add(R.id.container, mMainFragment);
                } else {
                    transaction.show(mMainFragment);
                }
                transaction.commit();

                break;
            case 1:
                closeDrawer();
                if (mRelaxFragment == null) {
                    mRelaxFragment = new RelaxFragment();
                    transaction.add(R.id.container, mRelaxFragment);
                } else {
                    transaction.show(mRelaxFragment);
                }
                transaction.commit();

                break;
            case 2:

                closeDrawer();
              //  SettingsFragment.launch(MainActivity.this);
                break;

            // fragment = new SettingsFragment();
            // break;
        }


    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mMainFragment != null) {
            transaction.hide(mMainFragment);
        }
        if (mRelaxFragment != null) {
            transaction.hide(mRelaxFragment);
        }

    }


    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // ???????????????
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = new Date().getTime();

            // ????????????????????????2???, ?????????
            if ((currentTime - preTime) > TWO_SECOND) {
                // ????????????
                T.showShort(mContext,R.string.back);

                // ????????????
                preTime = currentTime;

                // ????????????,????????????
                return true;
            } else {
                ActivityTack.getInstanse().exit(mContext);
            }
        }

        return super.onKeyDown(keyCode, event);
    }
    public void listener(){


    }
}
