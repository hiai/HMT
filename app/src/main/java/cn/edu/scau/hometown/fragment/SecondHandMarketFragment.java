package cn.edu.scau.hometown.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.edu.scau.hometown.MyApplication;
import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.activities.BikeSaleActivity;
import cn.edu.scau.hometown.activities.BookSaleActivity;
import cn.edu.scau.hometown.activities.DailySuppliesActivity;
import cn.edu.scau.hometown.activities.DigitalSaleActivity;
import cn.edu.scau.hometown.activities.FindPartTimeAcitivity;
import cn.edu.scau.hometown.activities.HouseRentActivity;
import cn.edu.scau.hometown.activities.LoginWebViewActivity;
import cn.edu.scau.hometown.activities.SecondHandMarketGoodsDetailActivity;
import cn.edu.scau.hometown.activities.SecondHandMarketMoreGoodsActivity;
import cn.edu.scau.hometown.activities.SecondHandMarketSearchActivity;
import cn.edu.scau.hometown.adapter.SecondHandMarketHomeAdapter;
import cn.edu.scau.hometown.bean.SecondHandMarketGoodsDetailBean;
import cn.edu.scau.hometown.bean.SecondHandMarketHomeBean;
import cn.edu.scau.hometown.bean.SecondHandMarketPicBean;
import cn.edu.scau.hometown.listener.RecyclerItemClickListener;
import cn.edu.scau.hometown.tools.HttpUtil;


public class SecondHandMarketFragment extends Fragment implements View.OnClickListener {

    private SecondHandMarketHomeBean secondHandMarketHomeBean;
    private SecondHandMarketGoodsDetailBean secondHandMarketGoodsDetailBean;
    private SwipeRefreshLayout mSwipeRefreshWidget;

    private RecyclerView recyclerView;
    private List<SecondHandMarketHomeBean.GoodsEntity> datas;
    private SecondHandMarketHomeAdapter secondHandMarketHomeAdapter;
    private View view;
    private Button digital, parttime, daily, bike, house, book,login,more;
    private RadioGroup radioGroup;
    private SecondHandMarketPicBean secondHandMarketPicBean;
    private LinearLayout linearLayout;
    private  View headView,footView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second_hand_market, container, false);
        headView = inflater.inflate(R.layout.recycler_headview,container,false);
        footView = inflater.inflate(R.layout.recycler_footview,container, false);
        initViews();
        getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_PIC, "homePic");
        getJsonData(HttpUtil.GET_SECOND_MARKET_DATA, "homeData");
        return view;
    }


    private void initViews() {
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_hmt_forum);
        mSwipeRefreshWidget.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipeRefreshWidget.setEnabled(false);
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources()
                        .getDisplayMetrics()));
        digital = (Button) headView.findViewById(R.id.digital_sale);
        parttime = (Button) headView.findViewById(R.id.find_parttime);
        daily = (Button) headView.findViewById(R.id.daily_supplies);
        bike = (Button) headView.findViewById(R.id.bike_specialsale);
        house = (Button) headView.findViewById(R.id.house_renting);
        book = (Button) headView.findViewById(R.id.book_sale);
        more = (Button) footView.findViewById(R.id.more_goods);
        login = (Button) view.findViewById(R.id.login);
        linearLayout = (LinearLayout) view.findViewById(R.id.market_searchbar);

        digital.setOnClickListener(this);
        parttime.setOnClickListener(this);
        daily.setOnClickListener(this);
        bike.setOnClickListener(this);
        house.setOnClickListener(this);
        book.setOnClickListener(this);
        more.setOnClickListener(this);
        login.setOnClickListener(this);
        linearLayout.setOnClickListener(this);

        radioGroup = (RadioGroup) headView.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.sale:
                        getJsonData(HttpUtil.GET_SECOND_MARKET_DATA, "homeDataClicked");
                        break;
                    case R.id.buy:
                        getJsonData(HttpUtil.GET_SECOND_MAEKET_GOOD_PURCHASE, "homeDataClicked");
                        break;
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.second_home_goods_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.market_searchbar:
              startActivity(new Intent(getActivity(), SecondHandMarketSearchActivity.class));
                break;
            case R.id.login:
                startActivity(new Intent(getActivity(), LoginWebViewActivity.class));
                break;
            case R.id.digital_sale:
                startActivity(new Intent(getActivity(), DigitalSaleActivity.class));
                break;
            case R.id.find_parttime:
                startActivity(new Intent(getActivity(), FindPartTimeAcitivity.class));
                break;
            case R.id.daily_supplies:
                startActivity(new Intent(getActivity(), DailySuppliesActivity.class));
                break;
            case R.id.bike_specialsale:
                startActivity(new Intent(getActivity(), BikeSaleActivity.class));
                break;
            case R.id.house_renting:
                startActivity(new Intent(getActivity(), HouseRentActivity.class));
                break;
            case R.id.book_sale:
                startActivity(new Intent(getActivity(), BookSaleActivity.class));
                break;
            case R.id.more_goods:
                startActivity(new Intent(getActivity(), SecondHandMarketMoreGoodsActivity.class));
             break;
        }
    }

    /**
     * 初始化轮播图
     */

    private void initBanner() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        BGABanner banner = (BGABanner) headView.findViewById(R.id.banner_splash_pager);
        banner.setTransitionEffect(BGABanner.TransitionEffect.Default);
        banner.setPageChangeDuration(1000);
        List<View> views = new ArrayList<>();
//        ArrayList<String> picDate = new ArrayList<>();
//        ArrayList<String> picName = new ArrayList<>();
//
//        for(int i =0;i<2;i++){
//            picDate.add(secondHandMarketPicBean.getPics().get(i).getSeconduploads_location());
//            picName.add(secondHandMarketPicBean.getPics().get(i).getSeconduploads_bewrite());
//        }
//        for(int i =0;i<2;i++) {
//            ImageView imageView = new ImageView(getActivity());
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("http://market.h.jaylin.me/Uploads/" + picDate.get(i) + picName.get(i), imageView, options);
//            Log.i("tree", "http://market.h.jaylin.me/Uploads/" + picDate.get(i) + picName.get(i));
//            views.add(imageView);
//        }
       for(int i = 0;i<3;i++) {
           views.add(getPageView(R.drawable.markethomepic));
       }

        banner.setViews(views);
        banner.setVisibility(View.VISIBLE);
        mSwipeRefreshWidget.setRefreshing(false);

    }

    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
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
                        secondHandMarketHomeBean = parseJsonData(json, SecondHandMarketHomeBean.class);
                        datas = secondHandMarketHomeBean.getGoods();
                        secondHandMarketHomeAdapter = new SecondHandMarketHomeAdapter(datas);
                        secondHandMarketHomeAdapter.setHeadView(headView);
                        secondHandMarketHomeAdapter.setFootView(footView);

                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(position!=0&&position!=datas.size()+1)
                                    getJsonData(HttpUtil.GET_SECOND_MARKET_GOOD_BY_GID + datas.get(position - 1).getSecondgoods_id(), "detailData");
                            }
                        }));
                        recyclerView.setAdapter(secondHandMarketHomeAdapter);
                        mSwipeRefreshWidget.setRefreshing(false);
                        break;
                    case "homeDataClicked":
                        secondHandMarketHomeBean = parseJsonData(json, SecondHandMarketHomeBean.class);
                        datas.clear();
                        datas.addAll(secondHandMarketHomeBean.getGoods());
                        secondHandMarketHomeAdapter.notifyDataSetChanged();
                        mSwipeRefreshWidget.setRefreshing(false);
                        break;
                    case "detailData":
                        secondHandMarketGoodsDetailBean = parseJsonData(json, SecondHandMarketGoodsDetailBean.class);
                        Intent intent = new Intent(getActivity(), SecondHandMarketGoodsDetailActivity.class);
                        intent.putExtra("secondHandMarketGoodsDetailBean", secondHandMarketGoodsDetailBean);
                        mSwipeRefreshWidget.setRefreshing(false);
                        startActivity(intent);
                        mSwipeRefreshWidget.setRefreshing(false);
                        break;
                    case "homePic":
                        secondHandMarketPicBean = parseJsonData(json,SecondHandMarketPicBean.class);
                        initBanner();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshWidget.setRefreshing(false);

            }
        });

        MyApplication.requestQueue.add(stringRequest);
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
}

