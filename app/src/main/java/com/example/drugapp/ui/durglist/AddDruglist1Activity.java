package com.example.drugapp.ui.durglist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.ui.MainActivity;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.bmob.v3.util.V;
import okhttp3.Call;

public class AddDruglist1Activity extends AppCompatActivity {

    private EditText input;
    private RoundButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_druglist1);
        initView();
    }


    /**
     * 添加清单
     */
    private void add(String name) {
        JSONObject rep = new JSONObject();
        try {
            rep.put("name", name);
            rep.put("account", ShapeUtil.INSTANCE.getUser(this).getAccount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtil.getInstance().POST(Apis.addDruglist1, rep.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                if (rep.getInt("code") == 200) {
                    ToastUtil.toast(AddDruglist1Activity.this,"添加成功");
                    finish();
                }
            }
        });
    }

    private void initView() {
        input = (EditText) findViewById(R.id.input);
        submit = (RoundButton) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input.getText())){
                    ToastUtil.toast(AddDruglist1Activity.this,"不能为空");
                    return;
                }
                add(input.getText().toString());
            }
        });
    }
}