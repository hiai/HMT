package cn.edu.scau.hometown.broadcastreceiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.edu.scau.hometown.bean.LatestVersionInfo;
import cn.edu.scau.hometown.tools.NewVersionUpdateUtil;


/**
 * Created by hiai on 2016/3/12.
 */
public class InstallNewVersionAppReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
           if (id==NewVersionUpdateUtil.downloadID&&!NewVersionUpdateUtil.cancelDownload){
               NewVersionUpdateUtil.installNewVersionApp(context);
           }
        }else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
            NewVersionUpdateUtil.cancelDownload();
            Toast.makeText(context, "你已取消下载，可以点击检查更新重新下载。", Toast.LENGTH_SHORT).show();
        }

    }
}
