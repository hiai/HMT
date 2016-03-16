package cn.edu.scau.hometown.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.adapter.SecondHandMarketCategoryAdapter;
import cn.edu.scau.hometown.bean.SecondHandMarketCategoryBean;
import cn.edu.scau.hometown.bean.SecondHandMarketGoodsDetailBean;
import cn.edu.scau.hometown.listener.RecyclerItemClickListener;
import cn.edu.scau.hometown.tools.HttpUtil;

public class SecondHandMarketSearchResultActivity extends ActionBarActivity {
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;

    private List<SecondHandMarketCategoryBean.GoodsEntity> datas;
    private SecondHandMarketCategoryBean secondHandMarketCategoryBean;
    private SecondHandMarketCategoryAdapter secondHandMarketCategoryAdapter;
    private SecondHandMarketGoodsDetailBean secondHandMarketGoodsDetailBean;
    private int page = 1;
    private boolean pageEnd;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private LinearLayoutManager linearLayoutManager;
    private String goodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second_hand_market_search_result);

        Intent intent = getIntent();
        goodName = intent.getStringExtra("keyword");
        try {
            goodName = URLEncoder.encode(goodName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(SecondHandMarketSearchResultActivity.this);
        initView();
        getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_BY_KEY_WORD + page + "?name=" + goodName + "&limit=" + 1, "homeData");
    }

    private void initView() {
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.setRefreshing);
        mSwipeRefreshWidget.setEnabled(false);
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        recyclerView = (RecyclerView) findViewById(R.id.second_home_category_recycle);
        linearLayoutManager = new LinearLayoutManager(SecondHandMarketSearchResultActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    /**
     * 得到json
     *
     * @param url
     */
    private void getJsonData(String url, final String type) {
        mSwipeRefreshWidget.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                switch (type) {
                    case "homeData":
                        addPage();
                        secondHandMarketCategoryBean = parseJsonData(json, SecondHandMarketCategoryBean.class);
                        datas = secondHandMarketCategoryBean.getGoods();

                        secondHandMarketCategoryAdapter = new SecondHandMarketCategoryAdapter(datas);
                        recyclerView.setAdapter(secondHandMarketCategoryAdapter);
                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(SecondHandMarketSearchResultActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_BY_GID + datas.get(position).getSecondgoods_id(), "detailData");
                            }
                        }));
                        mSwipeRefreshWidget.setRefreshing(false);
                        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView,
                                                             int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    int lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                                    Log.i("item", String.valueOf(lastVisibleItem));
                                    if (lastVisibleItem + 1 == secondHandMarketCategoryAdapter.getItemCount()) {
                                        mSwipeRefreshWidget.setRefreshing(true);
                                        if (pageEnd == false) {

                                            getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_BY_KEY_WORD + page + "?name=" + goodName + "&limit=" + 1, "homeDataMore");

                                        } else {
                                            Toast.makeText(getApplicationContext(), "到底了", Toast.LENGTH_SHORT).show();
                                        }

                                        mSwipeRefreshWidget.setRefreshing(false);
                                    }
                                }
                            }

                        });

                        break;
                    case "homeDataMore":
                        addPage();
                        secondHandMarketCategoryBean = parseJsonData(json, SecondHandMarketCategoryBean.class);
                        if (secondHandMarketCategoryBean.getGoods().isEmpty()) {
                            pageEnd = true;
                            Toast.makeText(getApplicationContext(), "到底了", Toast.LENGTH_SHORT).show();
                        } else {
                            datas.addAll(secondHandMarketCategoryBean.getGoods());
                            secondHandMarketCategoryAdapter.notifyDataSetChanged();
                            mSwipeRefreshWidget.setRefreshing(false);
                        }
                        break;
                    case "detailData":
                        secondHandMarketGoodsDetailBean = parseJsonData(json, SecondHandMarketGoodsDetailBean.class);
                        Intent intent = new Intent(SecondHandMarketSearchResultActivity.this, SecondHandMarketGoodsDetailActivity.class);
                        intent.putExtra("secondHandMarketGoodsDetailBean", secondHandMarketGoodsDetailBean);
                        startActivity(intent);
                        mSwipeRefreshWidget.setRefreshing(false);
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * 泛型解析json
     *
     * @param json
     * @param classParam
     * @param <T>
     * @return
     */
    private <T> T parseJsonData(String json, Class<T> classParam) {
        //TODO Class<T>?
        Gson gson = new Gson();
        T t = gson.fromJson(json, classParam);
        return t;
    }

    private void addPage() {
        page++;
    }

}
