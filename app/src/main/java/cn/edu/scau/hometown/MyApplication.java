package cn.edu.scau.hometown;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import cn.edu.scau.hometown.bean.HmtUserBasedInfo;
import cn.edu.scau.hometown.tools.DataUtil;
import cn.edu.scau.hometown.tools.EmoticonsUtils;

/**
 * Created by Administrator on 2015/10/2 0002.
 * 初始化一些全局变量，例如用户信息【HmtUserBasedInfo】
 */
public class MyApplication extends Application {
    //用户信息的数据类
    private HmtUserBasedInfo hmtUserBasedInfo;
    private RefWatcher refWatcher;

    public static RequestQueue requestQueue;

    private static MyApplication instance;



    public  HmtUserBasedInfo getHmtUserBasedInfo() {
        return hmtUserBasedInfo;
    }

    public void setHmtUserBasedInfo(HmtUserBasedInfo hmtUserBasedInfo) {
        this.hmtUserBasedInfo = hmtUserBasedInfo;
    }


    public static RefWatcher getRefWatcher(Context context){

    MyApplication application=(MyApplication)context.getApplicationContext();
    return application.refWatcher;

}

    @Override
    public void onCreate() {
        super.onCreate();
        EmoticonsUtils.initEmoticonsDB(this);
        ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        setHmtUserBasedInfo((HmtUserBasedInfo) DataUtil.getObject("登陆数据", this));
         refWatcher = LeakCanary.install(this);

        //避免创建多个requestqueue和造成内存泄漏
        requestQueue = Volley.newRequestQueue(this);

        instance = this;
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode( true );

    }


    public static MyApplication getInstance() {

        return instance;

    }
}
