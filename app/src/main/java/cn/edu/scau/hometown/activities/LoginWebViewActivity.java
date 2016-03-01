package cn.edu.scau.hometown.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.bean.HmtUserBasedInfo;
import cn.edu.scau.hometown.tools.HttpUtil;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

@SuppressLint("SetJavaScriptEnabled")
/**
 * app启动的第一个类
 */
public class LoginWebViewActivity extends SwipeBackActivity {
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private WebView webView;
    private String client_id = "client_id=11";
    private String redirect_url = "redirect_url=" + HttpUtil.getPsdnIp();
    private String state = "state=1ebUaX94z0QAdrX4G2t2eTidkI1jOfhi";
    private String response_type = "response_type=token";
    private String access_token;
    private String uid;
    private String expires_in;
    private String get_state;
    private String clickSpanUrl;
    private TextView login_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginweb_view);
        findViewById(R.id.back_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
        login_title = (TextView) findViewById(R.id.login_title);
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_container_web_view);
        mSwipeRefreshWidget.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipeRefreshWidget.setEnabled(false);
        initWebView();

        CookieSyncManager.createInstance(getApplicationContext());
        CookieManager.getInstance().removeAllCookie();//【清除Cookie，让用户每一次登录都是全新的登录状态，不保存用户登录信息】
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://hometown.scau.edu.cn/open/OAuth/authorize?"
                + client_id + "&" + "redirect_url=localhost" + "&" + state + "&"
                + "response_type=token" + "&scope=bbs");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.requestFocus();
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true); // 【支持缩放】
        webView.getSettings().setBuiltInZoomControls(true); // 【显示放大缩小】
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 【解决缓存问题】
        if (getIntent().getSerializableExtra("url") != null) {
            clickSpanUrl = (String) getIntent().getSerializableExtra("url");
            webView.loadUrl(clickSpanUrl);
            login_title.setText("查看图片");

        }
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mSwipeRefreshWidget.setRefreshing(true);


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mSwipeRefreshWidget.setRefreshing(false);
                if (url.contains("uid")) {
                    //【url包含uid，说明登陆成功了，可以获取用户的标识ID，再根据用户的标识ID，发出另一个请求从而获取到用户的所有基本信息】
                    access_token = url.substring(url.indexOf("#access_token") + 14, url.indexOf("&expires_in"));
                    saveAccessToken(LoginWebViewActivity.this,access_token);
                    getUserInfoTask(url);


                }

                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.contains("uid"))

                    view.loadUrl(url);          //【登陆成功后不显示页面】

                return true;
            }

        });
    }

    /**
     * 根据用户登录成功时从URL里截取出来得到的uid发出另一个请求从而获取到用户的基本信息
     *
     * @param url 用户成功登陆后到达的页面的url，【注意：该页面不予显示，只有页面的URL才包含我们所需要的信息】
     */
    private void getUserInfoTask(String url) {
        uid = url.substring(url.indexOf("&uid") + 5);
        access_token = url.substring(url.indexOf("#access_token") + 14, url.indexOf("&expires_in"));
        expires_in = url.substring(url.indexOf("&expires_in") + 12, url.indexOf("&scope"));
        try {
            String useruUrl = HttpUtil.GET_HMT_USER_BASE_INFORMATION_URL_BY_USER_ID + uid;
            JsonObjectRequest mJsonRequest = new JsonObjectRequest(useruUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String json = response.toString();
                            Gson gson = new Gson();
                            java.lang.reflect.Type type = new TypeToken<HmtUserBasedInfo>() {
                            }.getType();
                            HmtUserBasedInfo mHmtUserBasedInfo = gson.fromJson(json, type);
                            setLoginResult(mHmtUserBasedInfo);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(LoginWebViewActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
            mRequestQueue.add(mJsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理页面回退和前进，即新页面的打开
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;

        }
        return false;
    }


    /**
     * 设【置LoginWebViewActivity的回退结果】
     *
     * @param mHmtUserInfo 【包含用户所有信息的JavaBean类】
     */
    private void setLoginResult(HmtUserBasedInfo mHmtUserInfo) {
        Intent data = new Intent();
        data.putExtra("LoginResult", mHmtUserInfo);
        setResult(RESULT_OK, data);
        this.finish();

    }


    @Override
    public void onBackPressed() {

        super.scrollToFinishActivity();
    }

    private void saveAccessToken(Context context,String string){
        SharedPreferences sp = context.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("accessToken", string);
        editor.apply();
    }
}
