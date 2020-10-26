package com.example.drugapp.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.util.CountDownTimerUtils;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView headFinish;
    private TextView headTitle;
    private EditText account;
    private TextView sendcode;
    private EditText code;
    private EditText username;
    private EditText pass;
    private RoundButton register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        account = (EditText) findViewById(R.id.account);
        sendcode = (TextView) findViewById(R.id.sendcode);
        code = (EditText) findViewById(R.id.code);
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);
        register = (RoundButton) findViewById(R.id.register);

        headTitle.setText("注册");
        headFinish.setOnClickListener(this);
        sendcode.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_finish:
                finish();
                break;
            case R.id.sendcode:
                if (TextUtils.isEmpty(account.getText())) {
                    return;
                }
                String phone = account.getText().toString();
                sendVerCode(phone);
                break;
            case R.id.register:
                if (TextUtils.isEmpty(account.getText()) && TextUtils.isEmpty(code.getText())) {
                    return;
                }
                verCode(account.getText().toString(), code.getText().toString());
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendVerCode(String phone) {
        HttpUtil.getInstance().GET(Apis.sendSMS + "?phone=" + phone, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(sendcode,60000,1000);
                        countDownTimerUtils.start();
                    }
                });
                ToastUtil.toast(RegisterActivity.this, res);
            }
        });
    }

    /**
     * 验证
     */
    private void verCode(String phone, String code) {
        HttpUtil.getInstance().GET(Apis.verSMS + "?phone=" + phone + "&code=" + code, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(RegisterActivity.this, res);
                if (rep.getString("msg").equals("ok")) {
                    register();
                }
            }
        });
    }

    /**
     * 注册
     */
    private void register() throws JSONException {
        if (TextUtils.isEmpty(pass.getText()) && TextUtils.isEmpty(username.getText())) {
            ToastUtil.toast(RegisterActivity.this, "null");
            return;
        }
        JSONObject req = new JSONObject();
        req.put("account",account.getText().toString());
        req.put("password",pass.getText().toString());
        req.put("username",username.getText().toString());
        HttpUtil.getInstance().POST(Apis.register, req.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(RegisterActivity.this, rep.getString("msg"));
                if (rep.getInt("code") == 200) {
                    finish();
                }
            }
        });
    }
}
