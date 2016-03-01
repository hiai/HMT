package cn.edu.scau.hometown.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
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

import org.json.JSONObject;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.adapter.InitDetailHmtForumListViewAdapter;
import cn.edu.scau.hometown.bean.HmtForumPostContent;
import cn.edu.scau.hometown.tools.HttpUtil;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2015/9/2 0002.
 * 展示详细红满堂论坛回帖信息的类
 */
public class DetialHmtPostThreadsActivity extends SwipeBackActivity implements View.OnClickListener {
    //展示回帖列表的listview
    private RecyclerView lv_detail_post_threads;
    //自定义的用于返回上一级的View
    private RelativeLayout back_1;
    //下拉刷新布局
    private SwipeBackLayout mSwipeBackLayout;
    //存放所有相关数据的类
    private HmtForumPostContent hmtForumPostContent;
    //采用Volley网络通信库时需创建的请求排队对象
    private RequestQueue requestQueue;
    //帖子的标题
    private TextView post_subject;
    private String tid;

    private int lastVisibleItem;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private int nextPage = 2;
    private CoordinatorLayout rootView;
    private InitDetailHmtForumListViewAdapter adapter;
    private int mReplies;
    boolean mFlag=false;
    private int isBottom =1;
    private String authorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial_hmt_post_threads);

        rootView= (CoordinatorLayout) findViewById(R.id.rootview2);
        requestQueue = Volley.newRequestQueue(DetialHmtPostThreadsActivity.this);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.Refreshing);
        mSwipeRefreshWidget.setEnabled(false);
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        tid = (String) getIntent().getSerializableExtra("tid");
        authorName= (String) getIntent().getSerializableExtra("author");
        hmtForumPostContent = (HmtForumPostContent) getIntent().getSerializableExtra("hmtForumPostContent");
        hmtForumPostContent.getPosts().get(0).setAuthor("匿名");

        mReplies = Integer.parseInt(hmtForumPostContent.getThread().getReplies());

        String subject = hmtForumPostContent.getThread().getSubject();
        post_subject = (TextView) findViewById(R.id.post_subject);
        post_subject.setText(subject);

        findViews();
        initPostThreadsListView();
        initSwipeBackLayout();
        setListener();

    }

    /**
     * 用于初始化回帖列表
     */
    private void initPostThreadsListView() {

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        lv_detail_post_threads.setLayoutManager(linearLayoutManager);
        adapter = new InitDetailHmtForumListViewAdapter(this, hmtForumPostContent, tid);
        lv_detail_post_threads.setAdapter(adapter);

        mSwipeRefreshWidget.setRefreshing(false);

        lv_detail_post_threads.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItem == adapter.getItemCount()-1) {
                    if (adapter.getItemCount() - 1 != mReplies) {
                        mSwipeRefreshWidget.setRefreshing(true);
                    } else {
                        mSwipeRefreshWidget.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "别拉了，到底了", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);

                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItem + 4 >= adapter.getItemCount() - 1 && mFlag == false) {
                        mFlag = true;
                        VolleyRequestString(HttpUtil.GET_HMT_FORUM_POSTS_CONTENT_BY_TID + tid + "&page=" + nextPage + "&limit=10");

                    }
                    if (lastVisibleItem + 4 < adapter.getItemCount() - 1) {
                        mFlag = false;
                    }


            }
        });
    }

    /**
     *
     * @param url
     */
    private void VolleyRequestString(String url) {
        mSwipeRefreshWidget.setRefreshing(true);
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mSwipeRefreshWidget.setRefreshing(false);
                        String json = response.toString();

                        Gson gson = new Gson();
                        java.lang.reflect.Type type = new TypeToken<HmtForumPostContent>() {
                        }.getType();

                        if (hmtForumPostContent == null){
                            hmtForumPostContent = gson.fromJson(json, type);
                            initPostThreadsListView();
                        }
                        else {
                            HmtForumPostContent newHmtForumPostContent = gson.fromJson(json, type);
                            if(newHmtForumPostContent.getPosts() != null){
                                hmtForumPostContent.getPosts().addAll(newHmtForumPostContent.getPosts());
                                adapter.notifyDataSetChanged();
                            }else{
                                mFlag = true;
                            }
                        }

                        nextPage++;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSwipeRefreshWidget.setRefreshing(false);
                        Snackbar sb = Snackbar.make(rootView, "发送失败", Snackbar.LENGTH_SHORT);
                        sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        Boolean connected1 = HttpUtil.isNetworkConnected(getApplicationContext());
                        Boolean connected2 = HttpUtil.isWifiConnected(getApplicationContext());
                        if (connected1 == false && connected2 == false)
                            sb.setText( "请检查网络！");
                        else
                            sb.setText("(*@ο@*) 哇～  很抱歉！服务器出问题了～");
                        sb.show();
                    }
                }
        );
      mJsonRequest.setTag(true);
        requestQueue.add(mJsonRequest);
    }

    /**
     * 用于初始化向右滑动销毁Activity用到的操作
     */
    private void initSwipeBackLayout() {

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
    }

    /**
     * 是指各种监听事件
     */
    private void setListener() {
        back_1.setOnClickListener(this);
    }

    /**
     * 视图控件初始化
     */
    private void findViews() {
        lv_detail_post_threads = (RecyclerView) findViewById(R.id.lv_detail_post_threads);
        back_1 = (RelativeLayout) findViewById(R.id.back_1);
    }

    /**
     * 监听实际触发了什么事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_1:
                this.finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.clean();
        requestQueue.stop();
        requestQueue.cancelAll(true);
    }

    /**
     * 向右滑动销毁Activity用到的操作
     */
    @Override
    public void onBackPressed() {

        scrollToFinishActivity();
    }


}

