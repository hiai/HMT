package cn.edu.scau.hometown.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.apache.http .HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class HttpUtil {
    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL_KEY_WORD = "http://hometown.scau.edu.cn/course/index.php?s=/Api&keyword=";
    public static final String BASE_URL_COURSE_ID = "http://hometown.scau.edu.cn/course/index.php?s=/Api&course=";
    public static final String GET_HMT_USER_BASE_INFORMATION_URL_BY_USER_ID = "http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:userinfo&uid=";
    public static final String GET_USER_ICON_BY_USER_ID="http://hometown.scau.edu.cn/bbs/uc_server/avatar.php?uid=";
    public static final String GET_HMT_FORUM_POSTS_CONTENT_BY_TID="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:post&tid=";
    public static final String GET_HMT_FORUM_POSTS_CONTENT_BY_FID="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:thread&fid=";
    public static final String GET_PICTURES_GUIDE_TO_THREADS="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:xshow&action=image";
    public static final String GET_POST_THREADS_ATTACHMENT_BY_TID_AND_AID="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:attachment&action=view&tid=";
    public static final String GET_SECOND_MARKET_DATA="http://202.116.162.17/index.php/Home/Api";
    public static final String GET_SECOND_MARKET_GOOD_BY_GID="http://202.116.162.17/index.php/Home/Api/good/id/";
    public static final String GET_SECOND_MARKET_GOOD_BY_DIRECTORY_ID="http://202.116.162.17/index.php/Home/Api/catalog/cate/";
    public static final String GET_SECOND_MAEKET_GOOD_PURCHASE = "http://202.116.162.17/index.php/Home/Api/purchase";
    public static final String GET_SECOND_MARKET_GOOD_BY_KEY_WORD="http://202.116.162.17/index.php/Home/Api/search/p/";
    public static final String GET_SECOND_MARKET_GOOD_PIC="http://202.116.162.17/index.php/Home/Api/pics";
    public static final String GET_SECOND_MARKET_GOOD_SALE_PAGE= "http://market.h.jaylin.me/index.php/Home/Api/index/p/";
    public static final String GET_SECOND_MARKET_GOOD_PURCHASE_PAGE= "http://202.116.162.17/index.php/Home/Api/purchase/index/p/";
    /**
     * @param url 發送請求的url
     * @return 服務器響應請求發送的字符串
     * @throws Exception 拋出網絡連接時的異常
     */
    public static String getRequest(final String url) throws Exception {

        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {

                    @Override
                    public String call() throws Exception {

                        HttpGet get = new HttpGet(url);
                        HttpResponse httpResponse = httpClient.execute(get);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {

                            String result = EntityUtils.toString(httpResponse
                                    .getEntity());
                            return result;
                        }
                        return null;

                    }

                });

        new Thread(task).start();
        return task.get();

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

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);


            }
        }, 300, 200, Bitmap.Config.ARGB_4444,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(imageRequest);


    }
}
