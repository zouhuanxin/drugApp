package com.example.drugapp.ui.person;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drugapp.R;
import com.example.drugapp.model.bean.UserInfo;
import com.example.drugapp.ui.base.BaseFragment;
import com.example.drugapp.ui.login.LoginActivity;
import com.example.drugapp.util.ShapeUtil;
import com.kproduce.roundcorners.RoundImageView;
import com.kproduce.roundcorners.RoundTextView;

public class PersonFragment extends BaseFragment implements View.OnClickListener {

    private RoundTextView personImage;
    private TextView personName;
    private LinearLayout personUpdatepass;
    private LinearLayout personUpdatename;
    private LinearLayout personCancellogin;
    private RoundImageView adimage;

    private Intent intent;
    private LinearLayout personUpdateemail;

    @Override
    protected void init() {
        initView(view);
        initData();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_person;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView(View view) {
        personImage = (RoundTextView) view.findViewById(R.id.person_image);
        personName = (TextView) view.findViewById(R.id.person_name);
        personUpdatepass = (LinearLayout) view.findViewById(R.id.person_updatepass);
        personUpdatename = (LinearLayout) view.findViewById(R.id.person_updatename);
        personCancellogin = (LinearLayout) view.findViewById(R.id.person_cancellogin);
        adimage = (RoundImageView) view.findViewById(R.id.adimage);
        personUpdateemail = (LinearLayout) view.findViewById(R.id.person_updateemail);

        personUpdateemail.setOnClickListener(this);
        personName.setOnClickListener(this);
        personUpdatename.setOnClickListener(this);
        personUpdatepass.setOnClickListener(this);
        personCancellogin.setOnClickListener(this);
    }

    private void initData() {
        personImage.setText("姓");
        personName.setText("前往登陆");
        UserInfo userInfo = ShapeUtil.INSTANCE.getUser(getContext());
        if (userInfo != null) {
            personImage.setText(userInfo.getUsername().substring(0, 1));
            personName.setText(userInfo.getUsername());
        }
        Glide.with(getContext())
                .load("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4154168667,3021172379&fm=11&gp=0.jpg")
                .into(adimage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_updateemail:
                if (ShapeUtil.INSTANCE.getUser(getContext()) != null) {
                    intent = new Intent(getActivity(), UpdateEmailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.person_name:
                if (ShapeUtil.INSTANCE.getUser(getContext()) == null) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                break;
            case R.id.person_updatename:
                if (ShapeUtil.INSTANCE.getUser(getContext()) != null) {
                    intent = new Intent(getActivity(), UpdateUsernameActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.person_updatepass:
                if (ShapeUtil.INSTANCE.getUser(getContext()) != null) {
                    intent = new Intent(getActivity(), UpdatePassActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.person_cancellogin:
                if (ShapeUtil.INSTANCE.getUser(getContext()) != null) {
                    showCancelLogin();
                }
                break;
        }
    }

    private void showCancelLogin() {
        AlertDialog.Builder adBd = new AlertDialog.Builder(getActivity());
        adBd.setTitle("提示");
        adBd.setMessage("确认退出登陆吗？");
        adBd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShapeUtil.INSTANCE.clearUser(getContext());
                initData();
                dialog.dismiss();
            }
        });
        adBd.create().show();
    }
}
