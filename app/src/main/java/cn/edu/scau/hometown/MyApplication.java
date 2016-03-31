package cn.edu.scau.hometown;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
    private static MyApplication instance;


    public HmtUserBasedInfo getHmtUserBasedInfo() {
        return hmtUserBasedInfo;
    }

    public void setHmtUserBasedInfo(HmtUserBasedInfo hmtUserBasedInfo) {
        this.hmtUserBasedInfo = hmtUserBasedInfo;
    }

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        EmoticonsUtils.initEmoticonsDB(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        setHmtUserBasedInfo((HmtUserBasedInfo) DataUtil.getObject("登陆数据", this));
        refWatcher = LeakCanary.install(this);
        instance = this;
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(true);

    }


    public static MyApplication getInstance() {

        return instance;
    }

    /*
    当应用里有组件使用自定义的进程时，application类会被重新调用，而在application里有些
    初始化行为应该只被调用一次，这时要根据进程名称来区分是否为默认进程从而决定是否初始化
    某些组件
     */
    public static String getProcessName() {

        try {
            File file = new File("proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void initInMainProcess(){
        String processName=getProcessName();
        //判断进程名，保证只有主进程运行
        if (!TextUtils.isEmpty(processName)&&processName.equals(this.getPackageName())){
            //主进程初始化逻辑

        }
    }
}
