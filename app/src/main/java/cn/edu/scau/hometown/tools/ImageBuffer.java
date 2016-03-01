package cn.edu.scau.hometown.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hiai on 2015/10/31.
 * 把图片保存到SD卡中，优先从SD卡中取图片，没有则从网络上下载
 *
 *
 */
public class ImageBuffer {
   // 计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE
   // 或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE
   //那么删除40%最近没有被使用的文件,（MB）
     static   int CACHE_SIZE=15;

     static  int FREE_SD_SPACE_NEEDED_TO_CACHE=5;

     static   int MB=1024;
    //缓存图片前缀标识
    static    CharSequence WHOLESALE_CONV="ImageBuffer_";
   // 设置图片有效天数
     static int day=5;

     static long mTimeDiff=1000*60*60*24*day;


     static String TAG="TAG";
    //软引用把图片存在内存中
    static Map<String, SoftReference<Bitmap>>  map=new HashMap<>();

  public static boolean isExist(String url){

      String filename =convertUrlToFileName(url);
      String dir = getDirectory(filename);
      File file = new File(dir +"/" + filename);

      return  file.exists();

  }
/*
* 软引用Bitmap对象
*
* 获取Bitmap
*
*
* */
    public static Bitmap getBitmap(String url){
        Bitmap bitmap;
        String filename =convertUrlToFileName(url);
        String dir = getDirectory(filename);
        SoftReference<Bitmap> reference=map.get(filename);

        if(reference!=null){

            bitmap=reference.get();

           if(bitmap!=null){


                updateFileTime(dir,filename);
                return bitmap;
            }
        }


        String pathName=dir +"/" + filename;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        updateFileTime(dir,filename);
        bitmap=BitmapFactory.decodeFile(pathName,options);
        reference=new SoftReference<>(bitmap);
        map.put(filename, reference);

        return  bitmap;

    }
    //获取缩略图
    public static Bitmap getScaledBitmap(String url){


        //前缀Scaled表明是缩略图
        url="Scaled"+url;
        Bitmap bitmap;
        String filename =convertUrlToFileName(url);
        String dir = getDirectory(filename);
        SoftReference<Bitmap> reference=map.get(filename);

        if(reference!=null){

            bitmap=reference.get();

            if(bitmap!=null){
                  updateFileTime(dir,filename);
                return bitmap;
            }
        }


        String pathName=dir +"/" + filename;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        updateFileTime(dir,filename);
        bitmap=BitmapFactory.decodeFile(pathName,options);
        reference=new SoftReference<>(bitmap);
        map.put(filename, reference);

        return  bitmap;



    }
   public static void saveBmpToSd(Bitmap bm, String url) {
       String filename =convertUrlToFileName(url);
       String dir = getDirectory(filename);
        if (bm == null) {
            Log.w(TAG, " trying to savenull bitmap");
            return;
        }
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE >freeSpaceOnSd()) {

            removeCache(dir);
            Log.w(TAG, "Low free space onsd, do not cache");
           // return;
        }

       //软引用，再次打开页面时快速获取图片

       SoftReference<Bitmap> reference=new SoftReference<>(bm);
       map.put(filename, reference);



        File file = new File(dir +"/"+filename);

        try {
           if(file.createNewFile()) {
               OutputStream outStream = new FileOutputStream(dir +"/" + filename);
               bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
               outStream.flush();
               outStream.close();

           }
        } catch (FileNotFoundException e) {
            Log.w(TAG,"FileNotFoundException**");
        } catch (IOException e) {
            Log.w("test----->", e.toString());
        }
    }

public static void saveScaledBmpToSd(Bitmap bm,String url){
    //前缀Scaled表明是缩略图
    url="Scaled"+url;
    String filename =convertUrlToFileName(url);
    String dir = getDirectory(filename);
    if (bm == null) {
        Log.w(TAG, " trying to savenull bitmap");
        return;
    }
    //判断sdcard上的空间
    if (FREE_SD_SPACE_NEEDED_TO_CACHE >freeSpaceOnSd()) {

        removeCache(dir);
        Log.w(TAG, "Low free space onsd, do not cache");
        // return;
    }


    //软引用，再次打开页面时快速获取图片

    SoftReference<Bitmap> reference=new SoftReference<>(bm);
    map.put(filename, reference);
     File file = new File(dir +"/"+filename);

    try {
        if(file.createNewFile()) {

            OutputStream outStream = new FileOutputStream(dir +"/" + filename);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        }
    } catch (FileNotFoundException e) {
        Log.w(TAG,"FileNotFoundException**");
    } catch (IOException e) {
        Log.w("test----->", e.toString());
    }


}

    /**
     * 计算sdcard上的剩余空间
     * @return
     */
    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory() .getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize())/MB;
        return (int) sdFreeMB;
    }

    /**
     * 修改文件的最后修改时间
     * @param dir
     * @param fileName
     */
    public static void updateFileTime(String dir,String fileName) {
        File file = new File(dir,fileName);
        long newModifiedTime =System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }


    /*
    * 当容量大于设定值时，把最近很少用到的40%的文件删除
    *
    *
    * */

    public static void removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        int dirSize = 0;
        for(int i = 0; i < files.length;i++) {
            if(files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
        if (dirSize > CACHE_SIZE * MB ||FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 *files.length) + 1);

            Arrays.sort(files, new FileLastModifSort());

            Log.i(TAG, "Clear some expiredcache files ");

            for (int i = 0; i <removeFactor; i++) {

                if(files[i].getName().contains(WHOLESALE_CONV)) {

                    files[i].delete();

                }

            }

        }

    }
    /**
     * 删除过期文件
     * @param dirPath
     * @param filename
     */
    public  static void removeExpiredCache(String dirPath, String filename) {

        File file = new File(dirPath,filename);

        if (System.currentTimeMillis() -file.lastModified() > mTimeDiff) {

            Log.i(TAG, "Clear some expiredcache files ");

            file.delete();

        }

    }

    /**
     * TODO 根据文件的最后修改时间进行排序 *
     */
    public static class FileLastModifSort implements Comparator<File>

    {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() >arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() ==arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /*
    * 文件名就是WHOLESALE_CONV标识+url
    *
    * */
    public  static   String convertUrlToFileName(String url){

       return WHOLESALE_CONV.toString() + createHash.createEncrypHash(url )+ ".jpg";

    }

    /*
    * 图片缓存在"hongmantang/ImageBuffer/"下面
    *
    *
    * */

 public static  String getDirectory(String filename){

     File picFileDir = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"hongmantang"+File.separator+"ImageBuffer");
    //创建路径

      if(!picFileDir.exists()){

         picFileDir.mkdir();

     }
  return Environment.getExternalStorageDirectory().getPath()+File.separator+"hongmantang"+File.separator+"ImageBuffer";
    }

}
