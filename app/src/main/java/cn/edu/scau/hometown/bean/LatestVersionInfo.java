package cn.edu.scau.hometown.bean;

/**
 * Created by hiai on 2016/3/8.
 */
public class LatestVersionInfo {
    private String appName;
    private String apkName;
    private String versionName;
    private String versionCode;
    private String downloadUrl;
    private String describe;
    private boolean isRegisterReceiver=false;

    public void setIsRegisterReceiver(boolean isRegisterReceiver) {
        this.isRegisterReceiver = isRegisterReceiver;
    }

    public boolean isRegisterReceiver() {
        return isRegisterReceiver;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getAppName() {
        return appName;
    }

    public String getApkName() {
        return apkName;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public String toString() {
        return "LatestVersionInfo{" +
                "appName='" + appName + '\'' +
                ", apkName='" + apkName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
