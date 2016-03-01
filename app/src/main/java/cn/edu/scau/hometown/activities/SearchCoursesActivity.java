package cn.edu.scau.hometown.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.adapter.InitCoursesViewAdapter;
import cn.edu.scau.hometown.bean.AllComment;
import cn.edu.scau.hometown.bean.AllCourses;
import cn.edu.scau.hometown.bean.AllCourses.DataEntity;
import cn.edu.scau.hometown.interfac.SearchMethod;
import cn.edu.scau.hometown.library.com.tjerkw.slideexpandable.SlideExpandableListAdapter;
import cn.edu.scau.hometown.tools.HttpUtil;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 用于查课的类
 */
public class SearchCoursesActivity extends SwipeBackActivity implements SearchMethod, View.OnClickListener {
    private EditText edtTxt_inputKeyword;             //用于输入搜索课程关键字的搜索框
    private AllCourses mAllCourse;                   //存放关键字搜索结果信息的类
    private ListView lv_showCourses;                 //用于展示课程数据的下拉列表
    private ListAdapter adapter;                     //下拉列表的适配器
    private ListAdapter adapter_slideExpandable;    //在原有适配器的基础上再包装的适配器
    private Button btn_searchCourse;               //搜索按钮
    private SwipeRefreshLayout lo_swiper;
    private AccelerateInterpolator accelerator = new AccelerateInterpolator();
    private ObjectAnimator oa;
    private RequestQueue mRequestQueue;
    private ImageView backHome;

    private Button bt_course_A;
    private Button bt_course_public;  //公共类选修
    private Button bt_course_professional;  //专业类选修
    private Button bt_course_language; //语言类选修
    private RelativeLayout relativeLayout;  //推荐面板

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);


        mRequestQueue = Volley.newRequestQueue(this);

        findViews();
        setListener();
        initSwipeRefresh();


    }

    /**
     * 初始化试图控件
     */
    private void findViews() {
        backHome = (ImageView) findViewById(R.id.back_home);
        lo_swiper = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        btn_searchCourse = (Button) findViewById(R.id.search_course_btn);
        edtTxt_inputKeyword = (EditText) findViewById(R.id.keyword_search);
        edtTxt_inputKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchCourseByKeywordTask();
                return true;

            }
        });
        lv_showCourses = (ListView) findViewById(R.id.listview);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout_recommend);
        bt_course_A = (Button) findViewById(R.id.bt_course_A);
        bt_course_public = (Button) findViewById(R.id.bt_course_public);
        bt_course_language = (Button) findViewById(R.id.bt_course_language);
        bt_course_professional = (Button) findViewById(R.id.bt_course_professional);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_home:
                this.finish();
                break;
            case R.id.search_course_btn:
                onClickSearchCourse();
                break;
            case R.id.bt_course_A:
                recommend_bt_course_A();
                break;
            case R.id.bt_course_public:
                recommend_bt_course_public();
                break;
            case  R.id.bt_course_professional:
                recommend_bt_course_professional();
                break;
            case  R.id.bt_course_language:
                recommend_bt_course_language();
                break;
        }
    }

    private void setListener() {
        backHome.setOnClickListener(this);
        btn_searchCourse.setOnClickListener(this);
        bt_course_A.setOnClickListener(this);

    }


    /**
     * 点击查课按钮后触发的事件
     */
    private void onClickSearchCourse() {
        relativeLayout.setVisibility(View.GONE);
        btn_searchCourse.setEnabled(true);
        searchCourseByKeywordTask();
    }

    /**
     * 初始化下拉刷新设置
     */
    private void initSwipeRefresh() {
        lo_swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchCourseByKeywordTask();
            }
        });
        // 设置进度条的颜色主题，最多能设置四种
        lo_swiper.setColorScheme(android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        oa = ObjectAnimator.ofFloat(lo_swiper, "rotationY", 0f, -180f);
        oa.setDuration(600);
        oa.setRepeatCount(1);
        oa.setRepeatMode(ObjectAnimator.REVERSE);
        oa.setInterpolator(accelerator);
    }

    /**
     * @category 以下方法用于初始化显示搜索结果的ListView的适配器，以及设置每个ListItem项的视图
     */
    private void initCoursesListView() {
        adapter = new InitCoursesViewAdapter(this, mAllCourse, this);
        adapter_slideExpandable = new SlideExpandableListAdapter(adapter, R.id.expand,
                R.id.hide_view);

        lv_showCourses.setAdapter(adapter_slideExpandable);

    }


    /**
     * @function 下拉刷新或者點擊搜索按鈕時執行的搜索操作
     */
    @Override
    public void searchCourseByKeywordTask() {
        lo_swiper.setRefreshing(true);


        if (edtTxt_inputKeyword.getText().toString().equals("")) {
            Toast.makeText(SearchCoursesActivity.this, "关键字不能为空",
                    Toast.LENGTH_SHORT).show();
            lo_swiper.setRefreshing(false);
            relativeLayout.setVisibility(View.VISIBLE);
            return;
        }


        String url = null;
        try {
            url = HttpUtil.BASE_URL_KEY_WORD + URLEncoder.encode("" + edtTxt_inputKeyword.getText(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lo_swiper.setRefreshing(true);

                        try {
                            if (response.getString("status").equals("fail")) {
                                Toast.makeText(SearchCoursesActivity.this, "抱歉，关键字无搜索结果",
                                        Toast.LENGTH_SHORT).show();
                                lo_swiper.setRefreshing(false);
                                return;

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        String json = response.toString();
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<AllCourses>() {
                        }.getType();
                        mAllCourse = gson.fromJson(json, type);
                        initCoursesListView();


                        lo_swiper.setRefreshing(false);
                        btn_searchCourse.setEnabled(true);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if ((HttpUtil.isNetworkConnected(SearchCoursesActivity.this) == false) && (HttpUtil.isWifiConnected(SearchCoursesActivity.this) == false))
                            Toast.makeText(SearchCoursesActivity.this, "请检查网络！", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SearchCoursesActivity.this, "(*@ο@*) 哇～  很抱歉！服务器出问题了～", Toast.LENGTH_LONG).show();
                        lo_swiper.setRefreshing(false);
                        btn_searchCourse.setEnabled(true);
                    }
                });

        mRequestQueue.add(mJsonRequest);

    }

    /**
     * @param course
     * @function 查看具體評論時是執行的獲取數據任務，並且攜帶數據跳轉到CommentActivity
     */
    public void searchCommentTask(DataEntity course) {
        String url = HttpUtil.BASE_URL_COURSE_ID + course.getID();

        JsonObjectRequest mJsonRequest = new JsonObjectRequest(url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String json = response.toString();
                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<AllComment>() {
                        }.getType();
                        AllComment mAllComment = gson.fromJson(json, type);

                        Intent intent = new Intent(SearchCoursesActivity.this,
                                NewShowCommentsActivity.class);
                        intent.putExtra("评论", mAllComment);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.keep);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SearchCoursesActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                        lo_swiper.setRefreshing(false);
                    }
                });
        mJsonRequest.setTag(true);

        mRequestQueue.add(mJsonRequest);
    }

    private void recommend_bt_course_A(){
        Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
    }

    private void recommend_bt_course_public(){
        Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
    }

    private void recommend_bt_course_professional(){
        Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
    }

    /**
     * 向右滑动销毁Activity用到的操作
     */
    
    @Override
    public void onBackPressed() {

        scrollToFinishActivity();
    }

    private void recommend_bt_course_language(){
        Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRequestQueue.stop();
        mRequestQueue.cancelAll(true);
    }
}
