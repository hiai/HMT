package cn.edu.scau.hometown.listener;

import com.android.volley.VolleyError;

import java.util.List;

/**
 * Created by Administrator on 2015/10/14.
 */
public interface UsedMarketBaseSearchListener {

    //public void success(List<UsedGoodsBaseData> datas);

    public void error(VolleyError error);

    public void failor(Exception e);

}
