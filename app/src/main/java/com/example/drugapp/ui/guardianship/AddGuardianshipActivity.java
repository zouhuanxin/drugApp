package com.example.drugapp.ui.guardianship;

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
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class AddGuardianshipActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView headFinish;
    private TextView headTitle;
    private EditText account;
    private EditText pass;
    private RoundButton submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addguardianship);
        initView();
    }

    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        account = (EditText) findViewById(R.id.account);
        pass = (EditText) findViewById(R.id.pass);
        submit = (RoundButton) findViewById(R.id.submit);

        headTitle.setText("添加我的监护人");
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
                    add();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void add() throws JSONException {
        if (TextUtils.isEmpty(account.getText()) || TextUtils.isEmpty(pass.getText())){
            ToastUtil.toast(this,"不能为空");
            return;
        }
        JSONObject req = new JSONObject();
        req.put("account", ShapeUtil.INSTANCE.getUser(this).getAccount());
        req.put("beaccount",account.getText().toString());
        req.put("bepassword",pass.getText().toString());
        HttpUtil.getInstance().POST(Apis.addGuardianship, req.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(AddGuardianshipActivity.this,rep.getString("msg"));
                if (rep.getInt("code") == 200){
                    finish();
                }
            }
        });
    }
}
