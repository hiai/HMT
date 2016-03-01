package cn.edu.scau.hometown.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.activities.HmtPartitionActivity;
import cn.edu.scau.hometown.activities.PublishPostActivity;
import cn.edu.scau.hometown.listener.RecyclerItemClickListener;
import fab.FloatingActionButton;

/**
 * Created by Administrator on 2015/10/3 0003.
 */
public class PartitionFragment extends Fragment {
    private RecyclerView rcv_partition;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Integer> icon;
    private ArrayList<String> iconName;
    private Uri uri;
    private int imageWidth, imageHeight;
    private ImageRequest request;
    private PipelineDraweeController controller;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partition, null);
        rcv_partition = (RecyclerView) view.findViewById(R.id.rcv_partition);
        rcv_partition.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rcv_partition.setAdapter(new PartitionAdapter());
        rcv_partition.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), HmtPartitionActivity.class);
                        intent.putExtra("title", iconName.get(position));
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
                    }
                })
        );

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.attachToRecyclerView(rcv_partition);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(((MyApplication)(getActivity().getApplication())).getHmtUserBasedInfo()==null){
//                    Toast.makeText(getActivity(), "发帖前请先登录", Toast.LENGTH_SHORT).show();
//                } else {
                Intent intent = new Intent(getActivity(), PublishPostActivity.class);
                startActivity(intent);
//                }

            }
        });

        return view;
    }

    private void initData() {
        icon = new ArrayList<Integer>();
        icon.add(R.drawable.partition_work);
        icon.add(R.drawable.user_course_icon);
        icon.add(R.drawable.partition_sea);
        icon.add(R.drawable.partition_tiyu);
        icon.add(R.drawable.partition_music);
        icon.add(R.drawable.partition_movie);
        icon.add(R.drawable.partition_motion);
        icon.add(R.drawable.partition_literatal);
        icon.add(R.drawable.partition_why);
        icon.add(R.drawable.user_group_icon);
        icon.add(R.drawable.partition_advertise);
        icon.add(R.drawable.partition_weixiu);
        icon.add(R.drawable.partition_game);
        icon.add(R.drawable.partition_part_time);
        icon.add(R.drawable.partition_forum);
        icon.add(R.drawable.partition_think);
        icon.add(R.drawable.partition_secound_market);
        icon.add(R.drawable.partition_recruit);




        iconName = new ArrayList<>();
        iconName.add("职场交流");
        iconName.add("课程交流");
        iconName.add("海洋馆");
        iconName.add("体坛风云");
        iconName.add("音乐坊");
        iconName.add("影视分享");
        iconName.add("情感宣泄");
        iconName.add("文学社");
        iconName.add("生活百科");
        iconName.add("同道堂");
        iconName.add("广而告之");
        iconName.add("电脑维修");
        iconName.add("游戏版");
        iconName.add("兼职地带");
        iconName.add("论坛站务");
        iconName.add("深度思考");
        iconName.add("二手市场");
        iconName.add("社团组织招新");



    }


    class PartitionAdapter extends RecyclerView.Adapter<PartitionAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.partition_gridview_item, parent,
                    false));
            CalculationView(holder);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
           holder.partition_tv.setText(iconName.get(position));
           holder.partition_image.setImageDrawable(getResources().getDrawable(icon.get(position)));
             holder.partition_tv.setText(iconName.get(position));
//            缩放并显示图片图片
            uri =Uri.parse("res://cn.edu.scau.hometown/" + icon.get(position));
              request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(imageWidth, imageHeight))
                    .build();

            controller = (PipelineDraweeController)Fresco.newDraweeControllerBuilder()
                    .setOldController(holder.partition_image.getController())
                    .setImageRequest(request)
                    .build();
             holder.partition_image.setController(controller);




        }

        @Override
        public int getItemCount() {
            return iconName.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView partition_tv;
            SimpleDraweeView partition_image;

            public MyViewHolder(View view) {
                super(view);
                partition_tv = (TextView) view.findViewById(R.id.partition_text);
                partition_image = (SimpleDraweeView) view.findViewById(R.id.partition_image);
            }
        }

        private void CalculationView(MyViewHolder holder){

            int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

            int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

            holder.partition_image.measure(width,height);


            imageWidth= holder.partition_image.getMeasuredHeight();
            imageHeight=holder.partition_image.getMeasuredWidth();

        }
    }
}
