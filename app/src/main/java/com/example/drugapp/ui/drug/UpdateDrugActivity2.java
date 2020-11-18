package com.example.drugapp.ui.drug;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.model.bean.Druglist1;
import com.example.drugapp.model.bean.Druglist2;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

public class UpdateDrugActivity2 extends AppCompatActivity implements View.OnClickListener {

    private ImageView headFinish;
    private TextView headTitle;
    private TextView remindtime;
    private Spinner spinner1, spinner2;
    private RoundButton submit;
    private List<Druglist1> druglist1s = new ArrayList<>();
    private List<Druglist2> druglist2s = new ArrayList<>();
    private Druglist1 clickDruglist1 = new Druglist1();
    private Druglist2 clickDruglist2 = new Druglist2();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedrug2);
        initView();
        reqlist1();
    }

    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        remindtime = (TextView) findViewById(R.id.remindtime);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        submit = (RoundButton) findViewById(R.id.submit);

        headTitle.setText("添加服药提醒");

        headFinish.setOnClickListener(this);
        remindtime.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_finish:
                finish();
                break;
            case R.id.remindtime:
                OpenTimePicker(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        remindtime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                    }
                });
                break;
            case R.id.submit:
                try {
                    updateDrug();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                break;
        }
    }

    /**
     * 添加用药信息
     */
    private void updateDrug() throws JSONException {
        JSONObject req = new JSONObject();
        req.put("account", ShapeUtil.INSTANCE.getUser(this).getAccount());
        req.put("id", getIntent().getStringExtra("id"));
        req.put("drugimage", clickDruglist2.getDrugimage());
        req.put("drugname", clickDruglist2.getDrugname());
        req.put("drugdesc", clickDruglist2.getDrugdesc());
        req.put("drugcreatedtime", clickDruglist2.getDrugcreatedtime());
        req.put("drugtaketime", remindtime.getText().toString());
        HttpUtil.getInstance().POST(Apis.updateDrug, req.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {
                System.out.println("error call:"+e);
            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(UpdateDrugActivity2.this,rep.getString("msg"));
                if (rep.getInt("code") == 200) {
                    finish();
                }
            }
        });
    }

    /**
     * 请求用药信息list1
     */
    private void reqlist1() {
        HttpUtil.getInstance().GET(Apis.getByAccountDruglist1 + "?account=" + ShapeUtil.INSTANCE.getUser(this).getAccount(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                if (rep.getInt("code") == 200) {
                    druglist1s.clear();
                    JSONArray array = rep.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        Druglist1 druglist1 = new Druglist1();
                        druglist1.setId(array.getJSONObject(i).getInt("id"));
                        druglist1.setName(array.getJSONObject(i).getString("name"));
                        druglist1.setAccount(array.getJSONObject(i).getString("account"));
                        druglist1s.add(druglist1);
                    }
                    initSpinner1();
                }
            }
        });
    }

    private void initSpinner1() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < druglist1s.size(); i++) {
                    list.add(druglist1s.get(i).getName());
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(UpdateDrugActivity2.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        clickDruglist1 = druglist1s.get(position);
                        reqlist2(String.valueOf(clickDruglist1.getId()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    /**
     * 请求用药信息list2
     */
    private void reqlist2(String id) {
        HttpUtil.getInstance().GET(Apis.getByIdDruglist2 + "?id=" + id, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                if (rep.getInt("code") == 200) {
                    druglist2s.clear();
                    JSONArray array = rep.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        Druglist2 druglist2 = new Druglist2();
                        druglist2.setId(array.getJSONObject(i).getInt("id"));
                        druglist2.setDrugimage(array.getJSONObject(i).getString("drugimage"));
                        druglist2.setDrugname(array.getJSONObject(i).getString("drugname"));
                        druglist2.setDrugdesc(array.getJSONObject(i).getString("drugdesc"));
                        druglist2.setDrugcreatedtime(array.getJSONObject(i).getString("drugcreatedtime"));
                        druglist2s.add(druglist2);
                    }
                    initSpinner2();
                }
            }
        });
    }

    private void initSpinner2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (druglist2s.size() == 0) {
                    ToastUtil.toast(UpdateDrugActivity2.this, "无数据");
                    return;
                }
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < druglist2s.size(); i++) {
                    list.add(druglist2s.get(i).getDrugname());
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(UpdateDrugActivity2.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);
                spinner2.setSelection(0);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        clickDruglist2 = druglist2s.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void OpenTimePicker(OnTimeSelectListener to) {
        TimePickerView pvTime = new TimePickerBuilder(this, to)
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setTitleBgColor(0xFFffffff)
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setDate(Calendar.getInstance())// 如果不设置的话，默认是系统时间*/
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }
}
