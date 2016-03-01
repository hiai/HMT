package cn.edu.scau.hometown.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.ImageRequest;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.Executors;

import cn.edu.scau.hometown.MyApplication;
import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.tools.ImageBuffer;
import cn.edu.scau.hometown.view.CircularProgress;
import uk.co.senab.photoview.PhotoViewAttacher;

/**created by hiai
 *
 *查看图片的activity
 */
public class PhotoViewActivity extends AppCompatActivity implements OnClickListener{

    private String url;
    private  ImageView imageView;
    private  PhotoViewAttacher mAttacher;
    private  CircularProgress progressView;
    private Toolbar toolbar;
    private   CloseableReference<CloseableStaticBitmap> imageReference;
    private Button savePhoto;
    private static Bitmap bitmap;
    private AlertDialog alertDialog;
    private ViewGroup viewGroup;
    private ProgressDialog progressDialog;
    private  Handler mhandler;
    private  CloseableStaticBitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_photo_view);
        initView();
        initToolbar();
        url=(String)getIntent().getSerializableExtra("url");
        getImage();
        mhandler=new Handler();

    }


    private void  initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView=(ImageView)findViewById(R.id.imgview);
        progressView = (CircularProgress)findViewById(R.id.Progress_view);
        savePhoto=(Button)findViewById(R.id.savePhoto);
        viewGroup=(ViewGroup)findViewById(R.id.rl_parent);
        savePhoto.setOnClickListener(this);

    }
    private void initToolbar(){

        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_blue));
        setSupportActionBar(toolbar);


    }
    private void getImage(){
       Uri uri = Uri.parse(url);
        ImageRequest request=ImageRequest.fromUri(uri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(request, this);

        DataSubscriber dataSubscriber =
                new BaseDataSubscriber<CloseableReference<CloseableStaticBitmap>>() {
                    @Override
                    public void onNewResultImpl(
                            DataSource<CloseableReference<CloseableStaticBitmap>> dataSource) {

                        if (!dataSource.isFinished()) {

                        }

                        CloseableReference<CloseableStaticBitmap> imageReference = dataSource.getResult();
                        if (imageReference != null) {

                            Message message = new Message();
                            message.obj=imageReference;
                            message.arg1=1;
                            myHandler.sendMessage(message);

                        }
                    }
                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                        Throwable throwable = dataSource.getFailureCause();
                        // handle failure
                    }
                };
        dataSource.subscribe(dataSubscriber, Executors.newSingleThreadExecutor());

    }

    private  Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.arg1){
            case 1:
            {
            super.handleMessage(msg);
            imageReference=(CloseableReference<CloseableStaticBitmap>)msg.obj;

            image = imageReference.get();
            bitmap=image.getUnderlyingBitmap();

            imageView.setImageBitmap(bitmap);

            mAttacher = new PhotoViewAttacher(imageView);
            progressView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.invalidate();


            }
            break;
                case 2:{
                    Toast.makeText(PhotoViewActivity.this, "成功保存图片至/hongmantang/红满堂",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }

        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.savePhoto:

                SavePhoto();

                break;

        }
    }


    private void SavePhoto() {
        if (bitmap != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File picFileDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "hongmantang" + File.separator + "红满堂");
                    if (!picFileDir.exists()) {
                        picFileDir.mkdir();
                    }

                    String photoName = ImageBuffer.convertUrlToFileName(url);
                    File file = new File(picFileDir + "/" + photoName);
                    try {
                        if (file.createNewFile()) {

                            OutputStream outStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            outStream.flush();
                            outStream.close();

                        }
                    } catch (FileNotFoundException e) {

                    } catch (IOException e) {
                        Log.w("test----->", e.toString());
                    }
                    Message message = Message.obtain();
                    message.arg1 = 2;
                    myHandler.sendMessage(message);

                }
            }).start();


        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher= MyApplication.getRefWatcher(this);
        refWatcher.watch(imageReference);
        if(imageReference!=null)  {
            imageReference.close();
        }
        image.close();


    }

}






