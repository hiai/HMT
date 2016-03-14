package cn.edu.scau.hometown.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.activities.DetialUsedMarketActivity;
import cn.edu.scau.hometown.adapter.InitUsedMarketListAdapter;
import cn.edu.scau.hometown.bean.GoodsInfo;
import cn.edu.scau.hometown.bean.UsedGoodsBaseData;
import cn.edu.scau.hometown.listener.RecyclerItemClickListener;
import cn.edu.scau.hometown.listener.UsedMarkGoodDetialSearchListener;
import cn.edu.scau.hometown.listener.UsedMarketBaseSearchListener;
import cn.edu.scau.hometown.listener.UsedMarketMianImageListener;
import cn.edu.scau.hometown.tools.UsedMarketHttpUtil;
import fab.FloatingActionButton;


/**
 * Created by acer on 2015/7/24.
 *
 * @author simple
 */
public class SecondaryMarketFragment extends Fragment {
    private TextView tv_classify, tv_track, tv_collection;
    private Dialog dialog;
    private SwipeRefreshLayout lo_swiper;
    private ArrayList<Integer> icon;
    private ArrayList<String> iconName;
    private View layout;
    private View contentview;
    private List<UsedGoodsBaseData> datas = new ArrayList<>();
    private View view;
    private RecyclerView listView;
    private RequestQueue mRequestQueue;
    private InitUsedMarketListAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private PopupWindow pwMyPopWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        initClassifyDialogData();
        initMainData();

    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_secondary_market, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.dismiss();
                return true;
            }
        });
        iniButtonPopupWindow();
        initLayoutSwiper();
        initListView();
        initDialog();
        contentview = view.findViewById(R.id.nu);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.attachToRecyclerView(listView);
        floatingActionButton.setFocusable(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwMyPopWindow.isShowing()) {

                    pwMyPopWindow.dismiss();// 关闭
                } else {
                    pwMyPopWindow.showAsDropDown(contentview, 0, 0);// 显示
                }
            }
        });

        return view;
    }

    @SuppressLint("InflateParams")
    private void iniButtonPopupWindow() {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
        tv_classify = (TextView) layout.findViewById(R.id.tv_classify);
        tv_classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwMyPopWindow.dismiss();
                dialog.show();

            }
        });
        tv_collection = (TextView) layout.findViewById(R.id.tv_collection);
        tv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwMyPopWindow.dismiss();
            }
        });
        tv_track = (TextView) layout.findViewById(R.id.tv_track);
        tv_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwMyPopWindow.dismiss();
            }
        });


        pwMyPopWindow = new PopupWindow(layout);
        pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件


        layout.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        pwMyPopWindow.setWidth(layout.getMeasuredWidth());
        pwMyPopWindow.setHeight((layout.getMeasuredHeight()));


        pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
                R.drawable.bg_popub_window));// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
    }

    private void initListView() {

        listView = (RecyclerView) view.findViewById(R.id.lv_used_maket);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mRequestQueue.add(UsedMarketHttpUtil.getUMKGoodDetialInfo(datas.get(position).getSecondgoods_id(), new UsedMarkGoodDetialSearchListener() {
                    @Override
                    public void succee(GoodsInfo data) {

                        Intent intent = new Intent(getActivity(), DetialUsedMarketActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("GoodInfo", data);
                        intent.putExtras(b);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
                    }

                    @Override
                    public void failor(Exception e) {
                        Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error(VolleyError error) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    }
                }));

            }
        }));
        adapter = new InitUsedMarketListAdapter(datas, new UsedMarketMianImageListener() {
            @Override
            public void setImage(ImageView image, String url) {
                if (!url.equals("") && url != null) {
                    mRequestQueue.add(UsedMarketHttpUtil.getUMKMainImage(url, 300, 300, Bitmap.Config.RGB_565, image));
                }
            }
        });
        listView.setAdapter(adapter);
    }

    private void initClassifyDialogData() {
        icon = new ArrayList<>();
        icon.add(R.drawable.user_group_icon);
        icon.add(R.drawable.partition_work);
        icon.add(R.drawable.user_course_icon);
        icon.add(R.drawable.partition_sea);
        icon.add(R.drawable.partition_tiyu);
        icon.add(R.drawable.partition_music);
        icon.add(R.drawable.partition_movie);


        iconName = new ArrayList<>();
        iconName.add("数码类");
        iconName.add("招聘兼职");
        iconName.add("房屋租赁");
        iconName.add("日常用品");
        iconName.add("二手单车");
        iconName.add("书摊");
        iconName.add("其他");

    }

    private void initMainData() {
        mRequestQueue.add(UsedMarketHttpUtil.getMainData(new UsedMarketBaseSearchListener() {
            @Override
            public void success(List<UsedGoodsBaseData> datas) {
                SecondaryMarketFragment.this.datas.clear();
                for (UsedGoodsBaseData data : datas) {
                    SecondaryMarketFragment.this.datas.add(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failor(Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.classify_dialog, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv_classify);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(new RcvDialogAdater());


        dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
    }


    private void initLayoutSwiper() {
        lo_swiper = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_used_market);
        lo_swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                initMainData();
                lo_swiper.setRefreshing(false);
            }
        });

        lo_swiper.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    /**
     * 分类查询面板内的RecycleView 的 适配器
     */
    public class RcvDialogAdater extends RecyclerView.Adapter<RcvDialogAdater.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.gridview_item, parent,
                    false));
            return holder;
        }

        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.partition_tv.setText(iconName.get(position));
            holder.partition_image.setImageDrawable(getResources().getDrawable(icon.get(position)));
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    lo_swiper.setRefreshing(true);
                    mRequestQueue.add(UsedMarketHttpUtil.getClassifyData(position + 8, new UsedMarketBaseSearchListener() {
                        @Override
                        public void success(List<UsedGoodsBaseData> datas) {
                            SecondaryMarketFragment.this.datas.clear();
                            for (UsedGoodsBaseData data : datas) {
                                SecondaryMarketFragment.this.datas.add(data);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void error(VolleyError error) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failor(Exception e) {
                            Toast.makeText(getActivity(), "failor", Toast.LENGTH_SHORT).show();

                        }
                    }));
                    lo_swiper.setRefreshing(false);
                }
            });
        }

        @Override
        public int getItemCount() {
            return iconName.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView partition_tv;
            ImageView partition_image;
            View view;

            public MyViewHolder(View view) {
                super(view);
                this.view = view;
                partition_tv = (TextView) view.findViewById(R.id.partition_text);
                partition_image = (ImageView) view.findViewById(R.id.partition_image);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }
}
