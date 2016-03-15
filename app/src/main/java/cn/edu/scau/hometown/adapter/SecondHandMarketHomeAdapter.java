package cn.edu.scau.hometown.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.bean.SecondHandMarketHomeBean;

/**
 * Created by lenovo on 2015/11/15.
 */
public class SecondHandMarketHomeAdapter extends RecyclerView.Adapter<SecondHandMarketHomeAdapter.ViewHolder> {


    private List<SecondHandMarketHomeBean.GoodsEntity> datas;
    private DisplayImageOptions options;
    private View headView;
    private View footView;
    public static final int HEAD = 1;
    public static final int NORMAL = 2;
    public static final int FOOT = 3;
    public SecondHandMarketHomeAdapter( List<SecondHandMarketHomeBean.GoodsEntity> datas) {
        this.datas = datas;

         options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    public void setHeadView(View headView) {
        this.headView = headView;
        notifyItemInserted(0);
    }
    public void setFootView(View footView) {
        this.footView = footView;
        notifyItemInserted(datas.size()+1);
    }
    @Override
    public int getItemCount() {
        if(headView!=null&&footView!=null) return datas.size()+2;
        return datas.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(position == 0) return HEAD;
        if(position == datas.size()+1) return FOOT;
        return NORMAL;
    }

    //创建ViewHolder
    @Override
    public SecondHandMarketHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == HEAD) return new ViewHolder(headView);
        if(viewType == FOOT) return new ViewHolder(footView);
        View view = View.inflate(viewGroup.getContext(), R.layout.second_hand_market_home_recyclerview, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    //绑定数据
    @Override
    public void onBindViewHolder(SecondHandMarketHomeAdapter.ViewHolder holder, int position) {
        if(getItemViewType(position)==HEAD) return;
        if(getItemViewType(position)==FOOT) return;
        int realposition = getRealPosition(holder);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(datas.get(realposition).getSecondgoods_picture(), holder.iv_goodpic, options);
        holder.tv_goodsname.setText(datas.get(realposition).getSecondgoods_name());
        holder.tv_checkednum.setText(datas.get(realposition).getSecondgoods_views());
        holder.tv_overdueOrnot.setText("交易状态:"+datas.get(realposition).getSecondgoods_postdate());
        holder.tv_time.setText("发布于:"+getDate(datas.get(realposition).getSecondgoods_postdate()));

    }
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headView == null ? position : position - 1;
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
            if(itemView == headView) return;
            if(itemView == footView) return;
            iv_goodpic = (ImageView) itemView.findViewById(R.id.goods_pic);
            tv_goodsname = (TextView) itemView.findViewById(R.id.goods_name);
            tv_checkednum = (TextView) itemView.findViewById(R.id.goods_checkednum);
            tv_overdueOrnot = (TextView) itemView.findViewById(R.id.overdueOrnot);
            tv_time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}