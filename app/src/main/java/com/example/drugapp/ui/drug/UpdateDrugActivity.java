package com.example.drugapp.ui.drug;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.util.ToastUtil;
import com.kproduce.roundcorners.RoundButton;
import com.kproduce.roundcorners.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UpdateDrugActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView headFinish;
    private TextView headTitle;
    private TextView name;
    private EditText desc;
    private TextView createdtime;
    private TextView remindtime;
    private RoundImageView addimage;
    private RoundButton submit;

    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedrug);
        initView();
        initData();
    }

    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        name = (TextView) findViewById(R.id.name);
        desc = (EditText) findViewById(R.id.desc);
        createdtime = (TextView) findViewById(R.id.createdtime);
        remindtime = (TextView) findViewById(R.id.remindtime);
        addimage = (RoundImageView) findViewById(R.id.addimage);
        submit = (RoundButton) findViewById(R.id.submit);

        headTitle.setText("更新服药提醒");

        headFinish.setOnClickListener(this);
        createdtime.setOnClickListener(this);
        remindtime.setOnClickListener(this);
        addimage.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initData(){
        name.setText(getIntent().getStringExtra("name"));
        desc.setText(getIntent().getStringExtra("desc"));
        createdtime.setText(getIntent().getStringExtra("drugcreatedtime"));
        remindtime.setText(getIntent().getStringExtra("taketime"));
        url = getIntent().getStringExtra("image");
        Glide.with(UpdateDrugActivity.this).load(url).into(addimage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_finish:
                finish();
                break;
            case R.id.createdtime:
                OpenTimePicker(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        createdtime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                    }
                });
                break;
            case R.id.remindtime:
                OpenTimePicker(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        remindtime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                    }
                });
                break;
            case R.id.addimage:
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_PICK);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
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
     * 上传文件
     */
    private void uploadFile(String picPath) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    url = bmobFile.getFileUrl();
                } else {
                    ToastUtil.toast(UpdateDrugActivity.this, "上传文件失败：" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    /**
     * 添加用药信息
     */
    private void updateDrug() throws JSONException {
        if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(desc.getText()) || TextUtils.isEmpty(createdtime.getText())
                || TextUtils.isEmpty(remindtime.getText()) || url == null) {
            ToastUtil.toast(UpdateDrugActivity.this, "错误");
            return;
        }
        JSONObject req = new JSONObject();
        req.put("id", getIntent().getStringExtra("id"));
        req.put("drugimage", url);
        req.put("drugdesc", desc.getText().toString());
        req.put("drugcreatedtime", createdtime.getText().toString());
        req.put("drugtaketime", remindtime.getText().toString());
        HttpUtil.getInstance().POST(Apis.updateDrug, req.toString(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {
                System.out.println("error call:"+e);
            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(UpdateDrugActivity.this,rep.getString("msg"));
                if (rep.getInt("code") == 200) {
                    finish();
                }
            }
        });
    }

    /**
     * 压缩
     */
    private void lb(String path) {
        Luban.with(this)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(getFilesDir().getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        Glide.with(UpdateDrugActivity.this).load(file.getPath()).into(addimage);
                        uploadFile(file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            lb(getImagePath(uri, null));
        }
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void OpenTimePicker(OnTimeSelectListener to) {
        TimePickerView pvTime = new TimePickerBuilder(this, to)
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Sure")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
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
