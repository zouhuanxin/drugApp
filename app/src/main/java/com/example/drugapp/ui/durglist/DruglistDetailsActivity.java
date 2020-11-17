package com.example.drugapp.ui.durglist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.model.bean.Drug;
import com.example.drugapp.model.bean.Druglist1;
import com.example.drugapp.model.bean.Druglist2;
import com.example.drugapp.ui.drug.DrugAdapter;
import com.example.drugapp.ui.drug.UpdateDrugActivity;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class DruglistDetailsActivity extends AppCompatActivity {

    private TextView title;
    private RecyclerView recy;
    private List<Druglist2> druglist2s = new ArrayList<>();
    private DruglistDetailsAdapter druglistDetailsAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_druglist_details);
        initView();
        reqData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reqData();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        recy = (RecyclerView) findViewById(R.id.recy);

        title.setText(getIntent().getStringExtra("name"));
    }

    /**
     * 请求数据
     */
    private void reqData() {
        HttpUtil.getInstance().GET(Apis.getByIdDruglist2 + "?id=" + getIntent().getStringExtra("id"), new HttpCallBack() {
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
                    druglist2s.add(druglist2s.size(), null);
                    initAdapter();
                }
            }
        });
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        recy.post(new Runnable() {
            @Override
            public void run() {
                druglistDetailsAdapter = new DruglistDetailsAdapter(druglist2s);
                druglistDetailsAdapter.setDrugClick(new DruglistDetailsAdapter.DrugClick() {
                    @Override
                    public void Add(Druglist2 drug) {
                        intent = new Intent(DruglistDetailsActivity.this,AddDruglistDetailsActivity.class);
                        intent.putExtra("id",getIntent().getStringExtra("id"));
                        startActivity(intent);
                    }

                    @Override
                    public void Delect(Druglist2 drug) {
                        DelectDruglistDetails(drug.getId());
                    }

                    @Override
                    public void Update(Druglist2 drug) {
                        UpdateDruglistDetails(drug);
                    }
                });
                recy.setLayoutManager(new LinearLayoutManager(DruglistDetailsActivity.this));
                recy.setAdapter(druglistDetailsAdapter);
            }
        });
    }


    /**
     * 删除
     */
    private void DelectDruglistDetails(int id) {
        HttpUtil.getInstance().GET(Apis.deleteByIdDruglist2 + "?id=" + id, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(DruglistDetailsActivity.this, rep.getString("msg"));
                if (rep.getInt("code") == 200) {
                    reqData();
                }
            }
        });
    }

    /**
     * 前往修改
     */
    private void UpdateDruglistDetails(Druglist2 drug) {
        Intent intent = new Intent(DruglistDetailsActivity.this, UpdateDruglistDetailsActivity.class);
        intent.putExtra("name", drug.getDrugname());
        intent.putExtra("image", drug.getDrugimage());
        intent.putExtra("desc", drug.getDrugdesc());
        intent.putExtra("drugcreatedtime", drug.getDrugcreatedtime());
        intent.putExtra("id", String.valueOf(drug.getId()));
        startActivity(intent);
    }

}