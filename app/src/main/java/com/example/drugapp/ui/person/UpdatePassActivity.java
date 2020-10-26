package com.example.drugapp.ui.person;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.ui.login.LoginActivity;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;

import org.json.JSONObject;

import java.io.IOException;

import cn.bmob.v3.http.I;
import okhttp3.Call;

public class UpdatePassActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView headFinish;
    private TextView headTitle;
    private EditText pass1;
    private EditText pass2;
    private RoundButton submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);
        initView();
    }

    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        submit = (RoundButton) findViewById(R.id.submit);

        headTitle.setText("修改密码");
        headFinish.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_finish:
                finish();
                break;
            case R.id.submit:
                try {
                    updatePass();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void updatePass() throws Exception{
        if (TextUtils.isEmpty(pass1.getText()) || TextUtils.isEmpty(pass2.getText())){
            ToastUtil.toast(this,"密码不能为空");
            return;
        }
        if (!pass1.getText().toString().equals(pass2.getText().toString())){
            ToastUtil.toast(this,"俩次密码不一致");
            return;
        }
        JSONObject req = new JSONObject();
        req.put("account", ShapeUtil.INSTANCE.getUser(this).getAccount());
        req.put("password",pass1.getText());
        HttpUtil.getInstance().POST(Apis.updatePass, req.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(UpdatePassActivity.this,rep.getString("msg"));
                if (rep.getInt("code") == 200){
                    ShapeUtil.INSTANCE.clearUser(UpdatePassActivity.this);
                    Intent intent = new Intent(UpdatePassActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
