package cn.edu.scau.hometown.activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;


import cn.bingoogolapple.bgabanner.BGABanner;
import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.bean.SecondHandMarketGoodsDetailBean;
import cn.edu.scau.hometown.fragment.GoodsDetailCommentFragment;
import cn.edu.scau.hometown.fragment.GoodsDetailFragment;


public class SecondHandMarketGoodsDetailActivity extends ActionBarActivity implements View.OnClickListener {

    private RadioGroup radioGroup;
    private GoodsDetailFragment goodsDetailFragment;
    private GoodsDetailCommentFragment goodsDetailCommentFragment;
    private TextView name,price,view,state;
    private SecondHandMarketGoodsDetailBean secondHandMarketGoodsDetailBean;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_hand_market_goods_detail);

        secondHandMarketGoodsDetailBean = (SecondHandMarketGoodsDetailBean) getIntent().getSerializableExtra("secondHandMarketGoodsDetailBean");

        initViews();
        initGoodsData();
        setDefaultFragment();
        initBanner();
    }


    private void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnClickListener(this);
        name = (TextView) findViewById(R.id.goods_name);
        price = (TextView) findViewById(R.id.goods_price);
        view = (TextView) findViewById(R.id.goods_view);
        state = (TextView) findViewById(R.id.goods_state);
        iv_back = (ImageView) findViewById(R.id.back);
        iv_back.setOnClickListener(this);
    }
    private void initGoodsData() {
        name.setText(secondHandMarketGoodsDetailBean.getSecondgoods_name());
        price.setText(secondHandMarketGoodsDetailBean.getSecondgoods_price());
        view.setText(secondHandMarketGoodsDetailBean.getSecondgoods_views());
        state.setText(secondHandMarketGoodsDetailBean.getSecondgoods_efficiency());

    }
    private void setDefaultFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("publisher", secondHandMarketGoodsDetailBean.getSecondgoods_linkman().getMembers_username());
        bundle.putString("howNew",secondHandMarketGoodsDetailBean.getSecondgoods_hownew());
        bundle.putString("tradeType",secondHandMarketGoodsDetailBean.getSecondgoods_tradetype());
        bundle.putString("payType",secondHandMarketGoodsDetailBean.getSecondgoods_paidtype());
        bundle.putString("publishdate", secondHandMarketGoodsDetailBean.getSecondgoods_postdate());
        bundle.putString("pastdate", secondHandMarketGoodsDetailBean.getSecondgoods_pastdate());
        bundle.putString("goodsdescribe",secondHandMarketGoodsDetailBean.getSecondgoods_bewrite());

        goodsDetailFragment = goodsDetailFragment.newInstance(bundle);
        getFragmentManager().beginTransaction().replace(R.id.f_container,goodsDetailFragment).commit();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.radiobtn1:
                getFragmentManager().beginTransaction().replace(R.id.f_container, goodsDetailFragment).commit();
                break;
            case R.id.radiobtn2:
                goodsDetailCommentFragment = new GoodsDetailCommentFragment();
                getFragmentManager().beginTransaction().replace(R.id.f_container,goodsDetailCommentFragment).commit();
                break;
            case R.id.back:
                SecondHandMarketGoodsDetailActivity.this.finish();
        }
    }


    /**
     * 初始化轮播图
     */

    private void initBanner() {
        List<String> pics = secondHandMarketGoodsDetailBean.getSecondgoods_picture();
        int n = pics.size();
         DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        BGABanner banner = (BGABanner) findViewById(R.id.banner_splash_pager);
        banner.setTransitionEffect(BGABanner.TransitionEffect.Default);
        banner.setPageChangeDuration(1000);
        List<View> views = new ArrayList<>();

        for(int i =0;i<n;i++) {
            ImageView imageView = new ImageView(SecondHandMarketGoodsDetailActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(pics.get(i), imageView, options);
            views.add(imageView);
        }
        if(n<3){
            for(int a =0;a<3-n;a++)
            views.add(getPageView(R.drawable.markethomepic));
        }

        banner.setViews(views);
        banner.setVisibility(View.VISIBLE);


    }

    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(SecondHandMarketGoodsDetailActivity.this);
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

}
