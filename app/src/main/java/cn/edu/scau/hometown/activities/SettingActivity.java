package cn.edu.scau.hometown.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import cn.edu.scau.hometown.R;
/*
*
* create by hiai 2015.10.29
*
*
*
* */


public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Switch swt_loadPicture;
    private LinearLayout ll_exitAccount;
    private LinearLayout ll_checkVersion;
    private SharedPreferences isLoadingPicture;
    private boolean isLoading=true;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initSharedPreferences();
        initView();
        initSetting();
        initToolbar();
        setListener();


    }


private void initSetting(){
    isLoading=isLoadingPicture.getBoolean("isLoadingPicture",true);

}

private void initToolbar(){

    toolbar.setBackgroundColor(getResources().getColor(R.color.tab_blue));
    toolbar.setTitle("设置");
    toolbar.setTitleTextColor(0xFFFFFFFF);

    setSupportActionBar(toolbar);
    toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_36dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    swt_loadPicture.setChecked(isLoading);
}

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swt_loadPicture=(Switch)findViewById(R.id.isLoadingPicture);
        ll_exitAccount=(LinearLayout)findViewById(R.id.ll_exitAccount);
        ll_checkVersion=(LinearLayout)findViewById(R.id.ll_checkVersion);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.ll_exitAccount:
           break;
            case  R.id.ll_checkVersion:
                break;

        }
    }
    /**
     * 是指各种监听事件
     */
    private void setListener() {
        ll_exitAccount.setOnClickListener(this);
        ll_checkVersion.setOnClickListener(this);
        swt_loadPicture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                isLoading=isChecked;

            }
        });

    }
    private void initSharedPreferences(){
        isLoadingPicture=getSharedPreferences("setting", Context.MODE_APPEND);
        editor=isLoadingPicture.edit();
    }

    @Override
    protected void onDestroy() {
         editor.putBoolean("isLoadingPicture",isLoading);
         editor.commit();
         super.onDestroy();

    }
}

