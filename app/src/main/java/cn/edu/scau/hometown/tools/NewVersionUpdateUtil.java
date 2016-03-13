package cn.edu.scau.hometown.tools;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Config;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;

import cn.edu.scau.hometown.MyApplication;
import cn.edu.scau.hometown.bean.HmtForumPostList;
import cn.edu.scau.hometown.bean.LatestVersionInfo;
import cn.edu.scau.hometown.broadcastreceiver.InstallNewVersionAppReceiver;
import cn.edu.scau.hometown.view.CustomDialog;
import retrofit2.http.PUT;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hiai on 2016/3/8.
 * APP检查更新类
 */
public class NewVersionUpdateUtil {

    public static long downloadID = -1;

    private static String TAG = "NewVersionUpdateUtil";

    private static String snewVersionAppName;

    private static LatestVersionInfo latestVersionInfo;
    private static DownloadManager downManager;
    private static Context context;
    private static InstallNewVersionAppReceiver receiver;
    private static boolean IsshowDialogIfNoNeedUpdate = false;
    public static boolean cancelDownload=false;

    public static LatestVersionInfo getLatestVersionInfo() {
        latestVersionInfo = null;
        try {
            String result = HttpUtil.getRequest(HttpUtil.LATEST_VERSION_INFORMATION_URL);
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<LatestVersionInfo>() {
            }.getType();

            latestVersionInfo = gson.fromJson(result, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return latestVersionInfo;
    }

    public static int getLocalVersionCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    "cn.edu.scau.hometown", PackageManager.GET_CONFIGURATIONS).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    public static void installNewVersionApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(context.getExternalCacheDir(), latestVersionInfo.getApkName())),
                "application/vnd.android.package-archive");

        context.startActivity(intent);

    }

    public static void download(Context context, LatestVersionInfo latestVersionInfo) {
        Log.i("downurl", latestVersionInfo.getDownloadUrl());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(latestVersionInfo.getDownloadUrl()));
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载");
        request.setDescription(latestVersionInfo.getAppName() + "正在下载");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationUri(Uri.fromFile(new File(context.getExternalCacheDir(), latestVersionInfo.getApkName())));
        downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID = downManager.enqueue(request);

    }

    public static void checkUpdate(final Context context, boolean flag) {
        cancelDownload=false;
        IsshowDialogIfNoNeedUpdate = flag;
        Observable.create(new Observable.OnSubscribe<LatestVersionInfo>() {
            @Override
            public void call(Subscriber<? super LatestVersionInfo> subscriber) {
                LatestVersionInfo info = NewVersionUpdateUtil.getLatestVersionInfo();
                subscriber.onNext(info);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LatestVersionInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "-->checkupdate completed.");

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LatestVersionInfo latestVersionInfo) {

                         boolean needUpdate=Integer.parseInt(latestVersionInfo.getVersionCode()) > NewVersionUpdateUtil.getLocalVersionCode(context.getApplicationContext());
                        if (needUpdate) {
                            showDialog(context, latestVersionInfo);
                        }
                        if (IsshowDialogIfNoNeedUpdate&&!needUpdate){
                            showDialogNoNeedUpdate(context);
                        }
                    }
                });

    }

    public static void showDialogNoNeedUpdate(Context context){
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage("现在已是最新版本");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public static void showDialog(final Context context, final LatestVersionInfo info) {
        Log.i(TAG, "-->show dialog.");
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage(info.getDescribe());
        builder.setTitle("新版本:" + info.getAppName() + info.getVersionName());
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downLoadAndUpdate(context, info);
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }

    public static void downLoadAndUpdate(Context context, LatestVersionInfo info) {
        Log.i(TAG, "-->download and update.");
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        receiver = new InstallNewVersionAppReceiver();
        context.registerReceiver(receiver, filter);
        info.setIsRegisterReceiver(true);
        NewVersionUpdateUtil.download(context, info);

    }

    public static void cancelDownload() {
      cancelDownload=true;
        downManager.remove(downloadID);
    }

    public static void unregisterReceiver(Context context) {
        if (latestVersionInfo.isRegisterReceiver()) {
            context.unregisterReceiver(receiver);
        }
    }
}
