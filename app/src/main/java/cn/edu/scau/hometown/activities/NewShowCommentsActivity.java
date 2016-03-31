package cn.edu.scau.hometown.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.umeng.analytics.MobclickAgent;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.adapter.NewInitCommentsViewAdapter;
import cn.edu.scau.hometown.bean.AllComment;

/**
 * Created by laisixiang on 2015/10/4.
 */
public class NewShowCommentsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewHeader header;

    private RecyclerViewHeader mCardView;

    private AllComment mAllComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_new);

        mAllComments = (AllComment) getIntent().getSerializableExtra("评论");

        initCardView();
        initRecyclerView();
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.partition_toolbar);
        toolbar.setTitle("查看评论");
        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_blue));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initCardView() {
        mCardView = RecyclerViewHeader.fromXml(this, R.layout.cardview_item_course_head);
        //mCardView = (CardView) findViewById(R.id.cardview_item_course);
        ((TextView) mCardView.findViewById(R.id.course_name_new)).setText(mAllComments.getCourse().getClass_Name());
        ((TextView) mCardView.findViewById(R.id.course_teacher_new)).setText(mAllComments.getCourse().getClass_Teacher());
        ((TextView) mCardView.findViewById(R.id.course_owner_new)).setText(mAllComments.getCourse().getClass_Sex());
        ((TextView) mCardView.findViewById(R.id.course_college_new)).setText(mAllComments.getCourse().getClass_Collage());
        ((TextView) mCardView.findViewById(R.id.course_score_new)).setText(" " + mAllComments.getCourse().getClass_Score() + "学分");
        ((TextView) mCardView.findViewById(R.id.course_count_new)).setText(mAllComments.getCommentSize() + " 条评论");
        mCardView.findViewById(R.id.button_writecommend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewShowCommentsActivity.this, PushCommendAcitivity.class);
                intent.putExtra("课程", mAllComments.getCourse().getID());
                startActivity(intent);


            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_commment_new);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCardView.attachTo(mRecyclerView);

        mAdapter = new NewInitCommentsViewAdapter(mAllComments);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

}
