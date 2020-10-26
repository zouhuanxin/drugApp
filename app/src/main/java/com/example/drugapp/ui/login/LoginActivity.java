package com.example.drugapp.ui.login;

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
import com.example.drugapp.model.bean.UserInfo;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView headFinish;
    private TextView headTitle;
    private EditText account;
    private EditText pass;
    private RoundButton login;
    private TextView goregister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }


    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        account = (EditText) findViewById(R.id.account);
        pass = (EditText) findViewById(R.id.pass);
        login = (RoundButton) findViewById(R.id.login);
        goregister = (TextView) findViewById(R.id.goregister);

        headTitle.setText("登陆");
        headFinish.setOnClickListener(this);
        login.setOnClickListener(this);
        goregister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_finish:
                finish();
                break;
            case R.id.login:
                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.goregister:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 登陆
     */
    private void login() throws JSONException {
        if (TextUtils.isEmpty(pass.getText()) && TextUtils.isEmpty(account.getText())) {
            ToastUtil.toast(LoginActivity.this, "null");
            return;
        }
        JSONObject req = new JSONObject();
        req.put("account",account.getText().toString());
        req.put("password",pass.getText().toString());
        HttpUtil.getInstance().POST(Apis.login, req.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject json = new JSONObject(res);
                if (json.getInt("code") == 200) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setAccount(json.getJSONObject("data").getString("account"));
                    userInfo.setPassword(json.getJSONObject("data").getString("password"));
                    userInfo.setUsername(json.getJSONObject("data").getString("username"));
                    if (json.getJSONObject("data").toString().indexOf("email") != -1){
                        userInfo.setEmail(json.getJSONObject("data").getString("email"));
                    }
                    ShapeUtil.INSTANCE.setUser(LoginActivity.this,userInfo);
                    finish();
                }
                ToastUtil.toast(LoginActivity.this, json.getString("msg"));
            }
        });
    }
}
