package com.example.drugapp.ui.person;

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
import com.example.drugapp.model.bean.UserInfo;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class UpdateEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView headFinish;
    private TextView headTitle;
    private EditText email;
    private RoundButton submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateemail);
        initView();
        initData();
    }

    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        email = (EditText) findViewById(R.id.email);
        submit = (RoundButton) findViewById(R.id.submit);

        headTitle.setText("修改邮箱");
        headFinish.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initData(){
        UserInfo userInfo = ShapeUtil.INSTANCE.getUser(this);
        if (userInfo != null) {
            email.setText(userInfo.getEmail());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_finish:
                finish();
                break;
            case R.id.submit:
                try {
                    updateEmail();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void updateEmail() throws Exception{
        if (TextUtils.isEmpty(email.getText())){
            ToastUtil.toast(this,"邮箱不能为空");
            return;
        }
        JSONObject req = new JSONObject();
        req.put("account", ShapeUtil.INSTANCE.getUser(this).getAccount());
        req.put("email",email.getText());
        HttpUtil.getInstance().POST(Apis.updateEmail, req.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(UpdateEmailActivity.this,rep.getString("msg"));
                if (rep.getInt("code") == 200){
                    UserInfo userInfo = ShapeUtil.INSTANCE.getUser(UpdateEmailActivity.this);
                    userInfo.setEmail(email.getText().toString());
                    ShapeUtil.INSTANCE.setUser(UpdateEmailActivity.this,userInfo);
                    finish();
                }
            }
        });
    }
}
