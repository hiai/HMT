package cn.edu.scau.hometown.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.bean.SecondHandMarketGoodsDetailBean;

public class GoodsDetailFragment extends Fragment {
    private  View view;
    private TextView publisher,howNew,tradeType,payType,describe,date;
    private   Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods_detail,container,false);
        initViews();

         bundle = getArguments();
         if(bundle != null)
        {

            initGoodsData();
        }
        return view;
    }

    private void initGoodsData() {
        publisher.setText(bundle.getString("publisher"));
        howNew.setText(bundle.getString("howNew"));
        tradeType.setText(bundle.getString("tradeType"));
        payType.setText(bundle.getString("payType"));
        describe.setText(bundle.getString("goodsdescribe"));
        date.setText(bundle.getString("publishdate")+"至"+bundle.getString("pastdate"));


    }

    private void initViews() {
        publisher = (TextView) view.findViewById(R.id.goods_publisher);
        howNew = (TextView) view.findViewById(R.id.goods_howNew);
        tradeType = (TextView) view.findViewById(R.id.goods_tradeType);
        payType = (TextView) view.findViewById(R.id.goods_payType);
        describe = (TextView) view.findViewById(R.id.goods_descibe);
        date = (TextView) view.findViewById(R.id.goods_date);
    }

    /**
     *调用Fragment的setArguments()传递参数
     */
    public static GoodsDetailFragment newInstance(Bundle bundle)
    {
        GoodsDetailFragment goodsDetailFragment = new GoodsDetailFragment();
        goodsDetailFragment.setArguments(bundle);
        return goodsDetailFragment;
    }

}
