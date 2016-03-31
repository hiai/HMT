package cn.edu.scau.hometown.activities;

import android.animation.ArgbEvaluator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.*;
import android.os.Process;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.bean.LatestVersionInfo;

import cn.edu.scau.hometown.broadcastreceiver.InstallNewVersionAppReceiver;
import cn.edu.scau.hometown.fragment.FocusFragment;
import cn.edu.scau.hometown.fragment.HmtForumFragment;
import cn.edu.scau.hometown.fragment.PartitionFragment;
import cn.edu.scau.hometown.fragment.SecondHandMarketFragment;

import cn.edu.scau.hometown.tools.NewVersionUpdateUtil;


/**
 * Created by Administrator on 2015/7/26 0026.
 * 程序已启动时展示的主界面
 */
public class MainActivity extends AppCompatActivity {

    private String TAG ="MainActivity";
    //计算是否退出应用所需的时间
    private long firstTime;
    //退出时弹出snackBar用到的父级容器
    private CoordinatorLayout ll_main;
    //判断在不同的Fragment中显示不同的snackBar背景颜色
    private String snackBarBackGroupColor = "Tab_red";
    private Toolbar toolbar;
    private ImageView iv_search;
    //抽屉式布局
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private List<Fragment> fragments;
    private FragAdapter mAdapter;

    private LatestVersionInfo info;
    private InstallNewVersionAppReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        ll_main = (CoordinatorLayout) findViewById(R.id.main);

        fragments=new ArrayList<Fragment>();
        fragments.add(new SecondHandMarketFragment());

        fragments.add(new HmtForumFragment());
        fragments.add(new PartitionFragment());
        fragments.add(new FocusFragment());

        InitToolBar();
        InitTabLayout();
        NewVersionUpdateUtil.checkUpdate(new WeakReference<Context>(this),false);

    }





    private void InitToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("华农人的红满堂");
        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_red));
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        NewVersionUpdateUtil.unregisterReceiver(new WeakReference<Context>(this));
        super.onDestroy();
    }

    private void InitTabLayout() {

        mTabLayout = (TabLayout) findViewById(R.id.tl);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                ArgbEvaluator evaluator = new ArgbEvaluator();
                if (position == 0) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_red), getResources().getColor(R.color.tab_blue));
                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }
                if (0 < position && position < 1) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_blue));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_blue), getResources().getColor(R.color.tab_red));
                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }

                if (position == 1) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_purple));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_purple));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_blue), getResources().getColor(R.color.tab_purple));

                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }

                if (1 < position && position < 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_purple));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_purple));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_purple), getResources().getColor(R.color.tab_blue));
                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }


                if (position == 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_pink));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_pink));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_purple), getResources().getColor(R.color.tab_pink));
                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }

                if (2 < position && position < 3) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_pink));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_pink));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_pink), getResources().getColor(R.color.tab_purple));
                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }

                if (position == 3) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_brown));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_brown));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_pink), getResources().getColor(R.color.tab_brown));
                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }

                if (3 < position && position < 4) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_brown));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_brown));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_brown), getResources().getColor(R.color.tab_pink));
                    mTabLayout.setBackgroundColor(evaluate);
                    toolbar.setBackgroundColor(evaluate);
                }


            }

            @Override
            public void onPageSelected(int position) {


                if (position == 0) {
                    snackBarBackGroupColor = "Tab_red";
                    MobclickAgent.onEvent(MainActivity.this,"Page_SecondHand");
                }

                else if (position == 1) {
                    snackBarBackGroupColor = "Tab_blue";
                    MobclickAgent.onEvent(MainActivity.this,"Page_Recommend");
                }
                else if (position == 2){
                    snackBarBackGroupColor = "Tab_purple";
                    MobclickAgent.onEvent(MainActivity.this,"Page_Partition");
                }
                else if (position == 3){
                    snackBarBackGroupColor = "Tab_pink";
                    MobclickAgent.onEvent(MainActivity.this,"Page_Follow");
                }
                else if (position == 4){
                    snackBarBackGroupColor = "Tab_brown";
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAdapter = new FragAdapter(getSupportFragmentManager(), fragments);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        viewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {

            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Snackbar sb = Snackbar.make(ll_main, "再按一次退出", Snackbar.LENGTH_SHORT);
                if (snackBarBackGroupColor.equals("Tab_blue"))
                    sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_blue));
                else if (snackBarBackGroupColor.equals("Tab_red"))
                    sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_red));
                else if (snackBarBackGroupColor.equals("Tab_purple"))
                    sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_purple));
                else if (snackBarBackGroupColor.equals("Tab_pink"))
                    sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_pink));
                else if (snackBarBackGroupColor.equals("Tab_brown"))
                    sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_brown));
                sb.show();
                firstTime = secondTime;
            } else {
                MobclickAgent.onKillProcess(this);
                android.os.Process.killProcess(Process.myPid());

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView sv = (SearchView) menu.findItem(R.id.action_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, ResultOfSearchActivity.class);
                intent.putExtra("keyWord", query);
                startActivity(intent);
                sv.setQuery("", false);
                sv.clearFocus();
                sv.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}

class FragAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;


    public FragAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "二手";
        else if (position == 1) return "推荐";
        else if (position == 2) return "分区";
        else if (position == 3) return "关注";
        else return "其它";
    }

}
