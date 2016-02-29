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
import android.widget.RadioGroup;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;


import java.util.Collection;
import java.util.List;

import cn.edu.scau.hometown.R;

import cn.edu.scau.hometown.adapter.SecondHandMarketCategoryAdapter;

import cn.edu.scau.hometown.bean.SecondHandMarketCategoryBean;
import cn.edu.scau.hometown.bean.SecondHandMarketGoodsDetailBean;

import cn.edu.scau.hometown.listener.RecyclerItemClickListener;
import cn.edu.scau.hometown.tools.HttpUtil;

public class SecondHandMarketMoreGoodsActivity extends ActionBarActivity {

    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private List<SecondHandMarketCategoryBean.GoodsEntity> datas;
    private MyHandler handler;
    private Message msg;
    private SecondHandMarketCategoryBean secondHandMarketCategoryBean;
    private SecondHandMarketCategoryAdapter secondHandMarketCategoryAdapter;
    private SecondHandMarketGoodsDetailBean secondHandMarketGoodsDetailBean;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private LinearLayoutManager linearLayoutManager;
    private int page = 1;
    private int flag = 1;
    private RadioGroup radioGroup;
    private boolean pageEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_hand_market_more_goods);

        handler = new MyHandler();
        requestQueue = Volley.newRequestQueue(SecondHandMarketMoreGoodsActivity.this);
        initViews();
        getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_SALE_PAGE + page+ "?limit="+ 3, "homeData");

    }

    private void initViews() {
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.setRefreshing);
        mSwipeRefreshWidget.setEnabled(false);
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        recyclerView = (RecyclerView) findViewById(R.id.second_home_category_recycle);
        linearLayoutManager = new LinearLayoutManager(SecondHandMarketMoreGoodsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);



        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                page = 1;
                switch (i) {
                    case R.id.sale:
                        flag = 1;
                        pageEnd = false;
                        getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_SALE_PAGE + page+ "?limit="+ 3, "homeDataClicked");
                        break;
                    case R.id.buy:
                        flag = 2;
                        pageEnd = false;
                        getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_PURCHASE_PAGE + page+ "?limit="+ 3, "homeDataClicked");
                        break;
                }
            }
        });
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                   addPage();
                    datas = (List<SecondHandMarketCategoryBean.GoodsEntity>) msg.obj;
                    secondHandMarketCategoryAdapter = new SecondHandMarketCategoryAdapter(datas);
                    recyclerView.setAdapter(secondHandMarketCategoryAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(SecondHandMarketMoreGoodsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
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
                                if(lastVisibleItem +1== secondHandMarketCategoryAdapter.getItemCount()) {
                                    mSwipeRefreshWidget.setRefreshing(true);
                                   if(pageEnd==false) {
                                       if (flag == 1) {
                                           Log.i("page", String.valueOf(page));
                                           getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_SALE_PAGE + page+ "?limit="+ 3, "homeDataMore");

                                       } else {
                                           Log.i("page", String.valueOf(page));
                                           getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_PURCHASE_PAGE + page+ "?limit="+ 3, "homeDataMore");
                                       }
                                   }
                                    else{
                                       Toast.makeText(getApplicationContext(),"到底了",Toast.LENGTH_SHORT).show();
                                   }

                                    mSwipeRefreshWidget.setRefreshing(false);
                                }
                            }
                        }

                    });
                    break;
                case 2:
                    addPage();
                    datas.clear();
                    datas.addAll((Collection<? extends SecondHandMarketCategoryBean.GoodsEntity>) msg.obj);
                    secondHandMarketCategoryAdapter.notifyDataSetChanged();
                    mSwipeRefreshWidget.setRefreshing(false);
                    break;
                case 3:
                    addPage();
                    datas.addAll((List<SecondHandMarketCategoryBean.GoodsEntity>) msg.obj);
                    secondHandMarketCategoryAdapter.notifyDataSetChanged();
                    mSwipeRefreshWidget.setRefreshing(false);
                    break;

            }


        }
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
                        msg = handler.obtainMessage();
                        secondHandMarketCategoryBean = parseJsonData(json, SecondHandMarketCategoryBean.class);
                        msg.obj = secondHandMarketCategoryBean.getGoods();
                        msg.what = 1;
                        handler.sendMessage(msg);
                        break;
                    case "homeDataClicked":
                        msg = handler.obtainMessage();
                        secondHandMarketCategoryBean = parseJsonData(json, SecondHandMarketCategoryBean.class);
                        msg.obj = secondHandMarketCategoryBean.getGoods();
                        msg.what = 2;
                        handler.sendMessage(msg);
                        break;
                    case "homeDataMore":
                            msg = handler.obtainMessage();
                            secondHandMarketCategoryBean = parseJsonData(json, SecondHandMarketCategoryBean.class);
                           if(secondHandMarketCategoryBean.getGoods().isEmpty()){
                               pageEnd = true;
                               Toast.makeText(getApplicationContext(),"到底了",Toast.LENGTH_SHORT).show();
                            }
                        else {
                               msg.obj = secondHandMarketCategoryBean.getGoods();
                               msg.what = 3;
                               handler.sendMessage(msg);
                            }
                        break;
                    case "detailData":
                        secondHandMarketGoodsDetailBean = parseJsonData(json, SecondHandMarketGoodsDetailBean.class);
                        Intent intent = new Intent(SecondHandMarketMoreGoodsActivity.this, SecondHandMarketGoodsDetailActivity.class);
                        intent.putExtra("secondHandMarketGoodsDetailBean", secondHandMarketGoodsDetailBean);
                        startActivity(intent);
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
    private void addPage()
    {
        page++;
    }
}
