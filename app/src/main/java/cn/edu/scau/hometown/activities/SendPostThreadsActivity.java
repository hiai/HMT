package cn.edu.scau.hometown.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.keyboard.EmoticonsKeyBoardPopWindow;
import com.keyboard.view.EmoticonsEditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.tools.EmoticonsUtils;

public class SendPostThreadsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String author;
    private String tid;
    private String posthread;
    private EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;
    private EmoticonsEditText et_content;
    private ImageView inputFace;
    private Button send_post;
    private RequestQueue requestQueue;

    private CoordinatorLayout rootView;
    private Button clear_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post_threads);
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        getIntentData();
        findViews();
        initKeyBoardPopWindow();
        InitToolBar();

    }

    private void findViews() {

        rootView= (CoordinatorLayout) findViewById(R.id.rootView);
        et_content = (EmoticonsEditText) findViewById(R.id.et_content);
        inputFace = (ImageView) findViewById(R.id.s);
        clear_et= (Button) findViewById(R.id.clear_et);
        clear_et.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                et_content.setText("");
            }
        });


        inputFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyBoardPopWindow.showPopupWindow();
            }
        });
        send_post = (Button) findViewById(R.id.send_post);
        send_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //检查用户是否登陆
                if(getAccessToken().equals("尚未授权"))
                    Toast.makeText(SendPostThreadsActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                else
                    sendPostTask();
            }
        });
    }

    private void getIntentData() {
        tid=(String)getIntent().getSerializableExtra("tid");
        author = (String) getIntent().getSerializableExtra("author");
        posthread= (String) getIntent().getSerializableExtra("posthread");
    }

    public void initKeyBoardPopWindow() {
        mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(this);
        mKeyBoardPopWindow.setBuilder(EmoticonsUtils.getSimpleBuilder(this));
        mKeyBoardPopWindow.setEditText(et_content);

    }

    private void InitToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("回复 " + "《"+posthread+"》");
        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_blue));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getAccessToken() {
        SharedPreferences sp = SendPostThreadsActivity.this.getSharedPreferences("config", MODE_PRIVATE);
        return sp.getString("accessToken", "尚未授权").toString();
    }

    private void sendPostTask() {

        String url="http://hometown.scau.edu.cn/bbs/plugin.php";
       String message="";
        try {
            message=URLEncoder.encode(et_content.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String  postContent=message;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       Toast.makeText(SendPostThreadsActivity.this, response, Toast.LENGTH_LONG).show();
                        //Toast.makeText(SendPostThreadsActivity.this,et_content.getText().toString(),Toast.LENGTH_LONG).show();
                       // SendPostThreadsActivity.this.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar sb = Snackbar.make(rootView, "发送失败", Snackbar.LENGTH_LONG);
                sb.getView().setBackgroundColor(getResources().getColor(R.color.tab_blue));
                sb.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("id","iltc_open:post");
                map.put("access_token", getAccessToken());
                map.put("message", et_content.getText().toString());
                map.put("tid", "857127");

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
           //     headers.put("Charset", "utf-8");
                return headers;
            }

        };

        requestQueue.add(stringRequest);
    }
}
