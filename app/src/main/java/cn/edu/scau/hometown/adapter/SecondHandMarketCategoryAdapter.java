package cn.edu.scau.hometown.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.bean.SecondHandMarketCategoryBean;



/**
 * Created by lenovo on 2016/1/24.
 */
public class SecondHandMarketCategoryAdapter extends RecyclerView.Adapter<SecondHandMarketCategoryAdapter.ViewHolder> {
    private List<SecondHandMarketCategoryBean.GoodsEntity> datas;
    private DisplayImageOptions options;

    public SecondHandMarketCategoryAdapter( List<SecondHandMarketCategoryBean.GoodsEntity> datas) {
        this.datas = datas;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    @Override
    public int getItemCount() {
        Log.i("size", String.valueOf(datas.size()));
        return datas.size();
    }

    //创建ViewHolder
    @Override
    public SecondHandMarketCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = View.inflate(viewGroup.getContext(), R.layout.second_hand_market_category_recyclerview, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //绑定ViewHolder
    @Override
    public void onBindViewHolder(SecondHandMarketCategoryAdapter.ViewHolder holder, int position) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(datas.get(position).getSecondgoods_picture(),holder.iv_goodpic,options);
        holder.tv_goodsname.setText(datas.get(position).getSecondgoods_name());
        holder.tv_checkednum.setText(datas.get(position).getSecondgoods_views());
        holder.tv_overdueOrnot.setText("交易状态:"+datas.get(position).getSecondgoods_efficiency());
        holder.tv_time.setText("发布于:"+getDate(datas.get(position).getSecondgoods_postdate()));

    }
    private String getDate(String time) {
        long i = Long.parseLong(time);
        Date date = new Date(i*1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_goodpic;
        TextView tv_goodsname;
        TextView tv_checkednum;
        TextView tv_overdueOrnot;
        TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_goodpic = (ImageView) itemView.findViewById(R.id.goods_pic);
            tv_goodsname = (TextView) itemView.findViewById(R.id.goods_name);
            tv_checkednum = (TextView) itemView.findViewById(R.id.goods_checkednum);
            tv_overdueOrnot = (TextView) itemView.findViewById(R.id.overdueOrnot);
            tv_time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
