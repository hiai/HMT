package cn.edu.scau.hometown.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyboard.EmoticonsKeyBoardPopWindow;
import com.keyboard.view.EmoticonsEditText;
import com.umeng.analytics.MobclickAgent;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.tools.EmoticonsUtils;

/**
 * Created by Administrator on 2015/10/4 0004.
 */
public class FocusFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }


}
