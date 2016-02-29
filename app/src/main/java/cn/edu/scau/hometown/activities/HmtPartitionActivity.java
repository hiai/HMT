package cn.edu.scau.hometown.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.adapter.InitHmtForumListViewAdapter;
import cn.edu.scau.hometown.bean.HmtForumPostContent;
import cn.edu.scau.hometown.bean.HmtForumPostList;
import cn.edu.scau.hometown.listener.RecyclerItemClickListener;
import cn.edu.scau.hometown.tools.HttpUtil;

/**
 * Created by Administrator on 2015/10/4 0004.
 */
public class HmtPartitionActivity extends AppCompatActivity {


    private String tid;
    private String title;
    private int nextPage = 1;
    private Toolbar toolbar;
    private Integer lastVisibleItem;
    private RequestQueue mRequestQueue;
    private HmtForumPostList hmtForumPostList;
    private CoordinatorLayout rootView;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private InitHmtForumListViewAdapter initHmtForumListViewAdapter;
    private RecyclerView rcv_hmt_forum;
    private boolean isClick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partition);
        initToolBar();

        rootView= (CoordinatorLayout) findViewById(R.id.rootview2);
        mRequestQueue = Volley.newRequestQueue(HmtPartitionActivity.this);
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.setRefreshing);
        mSwipeRefreshWidget.setEnabled(false);
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        VolleyRequestString(HttpUtil.GET_HMT_FORUM_POSTS_CONTENT_BY_FID + getFidByPartitionName(title) + "&page=" + nextPage + "&limit=30", 1);


    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.partition_toolbar);
        title = (String) getIntent().getSerializableExtra("title");
        toolbar.setTitle(title);
        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_blue));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String getFidByPartitionName(String name) {
        if (name.equals("新生专区")) return "74";
        else if (name.equals("职场交流")) return "54";
        else if (name.equals("课程交流")) return "11";
        else if (name.equals("海洋馆")) return "7";
        else if (name.equals("体坛风云")) return "44";
        else if (name.equals("音乐坊")) return "79";
        else if (name.equals("影视分享")) return "73";
        else if (name.equals("情感宣泄")) return "36";
        else if (name.equals("文学社")) return "47";
        else if (name.equals("生活百科")) return "12";
        else if (name.equals("同道堂")) return "136";
        else if (name.equals("广而告之")) return "101";
        else if (name.equals("电脑维修")) return "125";
        else if (name.equals("兼职地带")) return "142";
        else if (name.equals("论坛站务")) return "39";
        else if (name.equals("深度思考")) return "55";
        else if (name.equals("二手市场")) return "95";
        else if (name.equals("游戏版"))   return "110";
        else if (name.equals("社团组织招新")) return "145";
        return "71";

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param url
     * @param searchType  1、表示获取帖子列表 ； 2、表示获取点击帖子的详情、评论
     */
    private void VolleyRequestString(String url, final int searchType) {
        mSwipeRefreshWidget.setRefreshing(true);
        JsonObjectRequest mJsonRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();

                        switch (searchType) {
                            case 1:
                                SearchHmtForumDataTask(json);
                                nextPage++;
                                break;
                            case 2:
                                SearchPostContentTask(json);
                                break;
                            default:
                                break;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSwipeRefreshWidget.setRefreshing(false);
                        Snackbar sb = Snackbar.make(rootView, "发送失败", Snackbar.LENGTH_SHORT);
                        sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_blue));
                        Boolean connected1 = HttpUtil.isNetworkConnected(HmtPartitionActivity.this);
                        Boolean connected2 = HttpUtil.isWifiConnected(HmtPartitionActivity.this);
                        if (connected1 == false && connected2 == false)
                            sb.setText( "                                                              请检查网络！");
                        else
                        sb.setText("(*@ο@*) 哇～  很抱歉！服务器出问题了～");
                        sb.show();
                    }
                }
        );

        mRequestQueue.add(mJsonRequest);
    }

    /**
     * 获取热门帖子列表数据任务
     */
    private void SearchHmtForumDataTask(String json) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HmtForumPostList>() {
        }.getType();

        if (hmtForumPostList == null){
            hmtForumPostList = gson.fromJson(json, type);
            initRecycleView();
        }

        else {
            HmtForumPostList newHmtForumPostList = gson.fromJson(json, type);
            hmtForumPostList.getThreads().addAll(newHmtForumPostList.getThreads());
            initHmtForumListViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取指定的帖子的回帖数据的任务
     */
    private void SearchPostContentTask(String json) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HmtForumPostContent>() {
        }.getType();
        HmtForumPostContent hmtForumPostContent = gson.fromJson(json, type);

        Log.i("laisx","Intent");

        Intent intent = new Intent(HmtPartitionActivity.this, DetialHmtPostThreadsActivity.class);
        intent.putExtra("hmtForumPostContent", hmtForumPostContent);
        intent.putExtra("tid", tid);
        mSwipeRefreshWidget.setRefreshing(false);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        isClick=false;
        super.onResume();
    }

    private void initRecycleView() {

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rcv_hmt_forum = (RecyclerView) findViewById(R.id.rcv_detail_partition);
        rcv_hmt_forum.setLayoutManager(linearLayoutManager);
        initHmtForumListViewAdapter = new InitHmtForumListViewAdapter(hmtForumPostList, getApplication());
        rcv_hmt_forum.setAdapter(initHmtForumListViewAdapter);
        rcv_hmt_forum.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(!isClick){
                            mSwipeRefreshWidget.setRefreshing(true);
                            tid = hmtForumPostList.getThreads().get(position).getTid();
                            VolleyRequestString(HttpUtil.GET_HMT_FORUM_POSTS_CONTENT_BY_TID + tid + "&page=1&limit=10", 2);
                        }
                        isClick=true;
                    }
                })
        );


        boolean tag = true;
        rcv_hmt_forum.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == initHmtForumListViewAdapter.getItemCount()) {
                    mSwipeRefreshWidget.setRefreshing(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (lastVisibleItem + 4 == initHmtForumListViewAdapter.getItemCount()) {
                    Log.i("test", "yes");
                    VolleyRequestString(HttpUtil.GET_HMT_FORUM_POSTS_CONTENT_BY_FID + getFidByPartitionName(title) + "&page=" + nextPage + "&limit=30", 1);
                    initHmtForumListViewAdapter.notifyDataSetChanged();
                    mSwipeRefreshWidget.setRefreshing(false);
                }

            }
        });
        mSwipeRefreshWidget.setRefreshing(false);
    }
}
