package cn.edu.scau.hometown.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.keyboard.EmoticonsKeyBoardPopWindow;
import com.keyboard.view.EmoticonsEditText;
import com.umeng.analytics.MobclickAgent;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.tools.EmoticonsUtils;

/**
 * Created by Admin on 2015/10/28.
 */
public class PublishPostActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EmoticonsEditText et_title;
    private EmoticonsEditText et_content;
    //发表情
    private ImageView iv_expression;
    //发图片，需要检查权限
    private ImageView iv_picture;
    //是否匿名，需要检测权限
    private CheckBox cb_Anonymous;
    private EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishpost);
        InitView();
        InitToolBar();
        initKeyBoardPopWindow();

        iv_expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyBoardPopWindow.showPopupWindow();
            }
        });

    }

    private void InitToolBar(){

        toolbar.setTitle("发布帖子");
        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_purple));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initKeyBoardPopWindow() {
        mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(this);
        mKeyBoardPopWindow.setBuilder(EmoticonsUtils.getSimpleBuilder(this));
        mKeyBoardPopWindow.setEditText(et_content);
    }

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

    private void InitView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_title= (EmoticonsEditText) findViewById(R.id.et_title);
        et_content = (EmoticonsEditText) findViewById(R.id.et_content);
        iv_expression = (ImageView) findViewById(R.id.iv_expression);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        cb_Anonymous = (CheckBox) findViewById(R.id.cb_Anonymous);

    }

    public void publish_post(View view){
        //点击发表按钮触发
        this.finish();
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
