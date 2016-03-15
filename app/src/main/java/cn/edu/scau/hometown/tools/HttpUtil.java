package cn.edu.scau.hometown.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.android.volley.RequestQueue;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    public static OkHttpClient client = new OkHttpClient();
    public static final String BASE_URL_KEY_WORD = "http://hometown.scau.edu.cn/course/index.php?s=/Api&keyword=";
    public static final String BASE_URL_COURSE_ID = "http://hometown.scau.edu.cn/course/index.php?s=/Api&course=";
    public static final String GET_HMT_USER_BASE_INFORMATION_URL_BY_USER_ID = "http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:userinfo&uid=";
    public static final String GET_USER_ICON_BY_USER_ID="http://hometown.scau.edu.cn/bbs/uc_server/avatar.php?uid=";
    public static final String GET_HMT_FORUM_POSTS_CONTENT_BY_TID="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:post&tid=";
    public static final String GET_HMT_FORUM_POSTS_CONTENT_BY_FID="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:thread&fid=";
    public static final String GET_PICTURES_GUIDE_TO_THREADS="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:xshow&action=image";
    public static final String GET_POST_THREADS_ATTACHMENT_BY_TID_AND_AID="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:attachment&action=view&tid=";
    public static final String GET_SECOND_MARKET_DATA="http://192.168.253.73/hometown_market/hometownMarket/index.php/Home/Api";
    public static final String GET_SECOND_MARKET_GOOD_BY_GID="http://192.168.253.73/hometown_market/hometownMarket/index.php/Home/Api/good/id/";
    public static final String GET_SECOND_MARKET_GOOD_BY_DIRECTORY_ID="http://192.168.253.73/hometown_market/hometownMarket/index.php/Home/Api/catalog/cate/";
    public static final String GET_SECOND_MARKET_GOOD_BY_KEY_WORD="http://192.168.253.73/hometown_market/hometownMarket/index.php/Home/Api/search/name/";

    //图片服务器地址，APP给出需要的图片宽度，服务器返回一定宽度的图片，与给出的宽度不一定相等，只是接近
    public static final String GET_POST_THREADS_ATTACHMENT_SCALED_BY_TID_AND_AID="http://202.116.162.17:8200/image/getImage?id=iltc_open:attachment&action=view&tid=";
    //获取最新APP的信息，用于更新版本
    public static final String  LATEST_VERSION_INFORMATION_URL="http://1.hiai.sinaapp.com/TestTheAppUpdate";

    /**
     * @param url 發送請求的url
     * @return 服務器響應請求發送的字符串
     * @throws Exception 拋出網絡連接時的異常
     */
    public static String getRequest(final String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){

            return response.body().string();
        }

        return null;

    }


    /**
     * @return 返回手機的IP地址，在Oauth登录时需要填写的redirect_uri的值
     */
    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }


    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static  void setUserIconTask(RequestQueue requestQueue,String url, final ImageView imageView) {

        ImageRequest imageRequest = new ImageRequest(url, new com.android.volley.Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);


            }
        }, 300, 200, Bitmap.Config.ARGB_4444,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        imageRequest.setTag(true);
        requestQueue.add(imageRequest);


    }
}
