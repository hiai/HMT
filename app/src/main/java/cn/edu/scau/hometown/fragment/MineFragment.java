package cn.edu.scau.hometown.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import cn.edu.scau.hometown.MyApplication;
import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.activities.LoginWebViewActivity;
import cn.edu.scau.hometown.activities.SearchCoursesActivity;
import cn.edu.scau.hometown.activities.SettingActivity;
import cn.edu.scau.hometown.bean.HmtUserBasedInfo;
import cn.edu.scau.hometown.tools.DataUtil;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by acer on 2015/7/24.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    //快速查课按钮
    private LinearLayout ll_search_course;
    private int LOGIN_REQUEST_CODE = 11;
    //用户信息的数据类
    private HmtUserBasedInfo hmtUserBasedInfo;
    //用户昵称
    private TextView tv_user_name;
    //用户邮箱
    private TextView tv_user_email;
    //用户头像
    private CircleImageView civ_user_icon;
    //用户魅力值
    private TextView tv_user_ml;
    //用户积分
    private TextView tv_user_credits;
    //用户薯片值
    private TextView tv_user_chips;
    //用户所在的用户组
    private TextView tv_user_groupName;
    //使用Volley网络通信框架会用到的请求队列对象
    private RequestQueue mRequestQueue;
    //用户设置
    private LinearLayout ll_setting;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        hmtUserBasedInfo=((MyApplication)(getActivity().getApplication())).getHmtUserBasedInfo();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        findViews(view);
        setListener();

        if (hmtUserBasedInfo != null) {

            LoginSuccess();
        }


        return view;
    }

    private void setListener() {
        ll_search_course.setOnClickListener(this);
        civ_user_icon.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
    }

    private void findViews(View view) {
        ll_search_course = (LinearLayout) view.findViewById(R.id.btn_search_course);
        civ_user_icon = (CircleImageView) view.findViewById(R.id.civ_user_icon);
        tv_user_name = (TextView) view.findViewById(R.id.me_message_name);
        tv_user_email = (TextView) view.findViewById(R.id.me_message_email);
        tv_user_chips = (TextView) view.findViewById(R.id.tv_user_chips);
        tv_user_credits = (TextView) view.findViewById(R.id.tv_user_credits);
        civ_user_icon = (CircleImageView) view.findViewById(R.id.civ_user_icon);
        tv_user_ml = (TextView) view.findViewById(R.id.tv_user_ml);
        tv_user_groupName = (TextView) view.findViewById(R.id.me_message_groupname);
        ll_setting=(LinearLayout) view.findViewById(R.id.ll_setting);
    }


    //设置用户登录成功后的事件
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        HmtUserBasedInfo   tempHmtUserBasedInfo = (HmtUserBasedInfo) data.getSerializableExtra("LoginResult");
        ((MyApplication)(getActivity().getApplication())).setHmtUserBasedInfo(tempHmtUserBasedInfo);

        LoginSuccess();
    }
  //成功登陆后利用获取到的用户信息类更新视图
    private void LoginSuccess() {
        MobclickAgent.onEvent(getActivity(), "Event_SignIn");
        hmtUserBasedInfo=((MyApplication)(getActivity().getApplication())).getHmtUserBasedInfo();
        String userName = hmtUserBasedInfo.getData().getUsername();
        String userEmail = hmtUserBasedInfo.getData().getEmail();
        String userML = hmtUserBasedInfo.getData().getMl();
        String userChips = hmtUserBasedInfo.getData().getSp();
        String userCredits = hmtUserBasedInfo.getData().getCredits();
        String userGroupName = DataUtil.getGroupNmae(hmtUserBasedInfo.getData().getGroupid());
        String userIconUrl=hmtUserBasedInfo.getData().getAvatar();
        setUserIconTask(userIconUrl);


        tv_user_name.setText(userName);
        tv_user_email.setText(userEmail);
        tv_user_ml.setText(userML);
        tv_user_chips.setText(userChips);
        tv_user_credits.setText(userCredits);
        tv_user_groupName.setText(userGroupName);
        if (hmtUserBasedInfo != null) {
            DataUtil.saveObject("登陆数据",hmtUserBasedInfo, getActivity());
        }
    }

  //利用用户ID获取并更新用户头像
    private void setUserIconTask(String url) {

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                civ_user_icon.setImageBitmap(response);


            }
        }, 300, 200, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mRequestQueue.add(imageRequest);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_user_icon:
                Intent i1 = new Intent(getActivity(), LoginWebViewActivity.class);
                startActivityForResult(i1, LOGIN_REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
                break;
            case R.id.btn_search_course:
                Intent i2 = new Intent(getActivity(),SearchCoursesActivity.class);
                startActivity(i2);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
                break;
            case R.id.ll_setting:
                Intent i3 = new Intent(getActivity(),SettingActivity.class);
                startActivity(i3);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
                break;
            default:
                break;

        }

    }

}
