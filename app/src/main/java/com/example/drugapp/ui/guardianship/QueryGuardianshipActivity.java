package com.example.drugapp.ui.guardianship;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.model.bean.Drug;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class QueryGuardianshipActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView headFinish;
    private TextView headTitle;
    private SwipeRefreshLayout swipe;
    private RecyclerView recy;

    private List<Drug> drugs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queryguardianship);
        initView();
        initData();
    }

    private void initView() {
        headFinish = (ImageView) findViewById(R.id.head_finish);
        headTitle = (TextView) findViewById(R.id.head_title);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        recy = (RecyclerView) findViewById(R.id.recy);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqData();
            }
        });
        headTitle.setText("他的服药提醒");
        headFinish.setOnClickListener(this);
    }

    private void initData(){
        reqData();
    }

    private void reqData() {
        HttpUtil.getInstance().GET(Apis.getAllDrugData+"?account="+ getIntent().getStringExtra("account"), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {
                swipe.setRefreshing(false);
            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                if (rep.getInt("code") == 200) {
                    drugs.clear();
                    JSONArray array = rep.getJSONArray("data");
                    System.out.println("res:"+res);
                    for (int i = 0; i < array.length(); i++) {
                        Drug drug = new Drug();
                        drug.setId(array.getJSONObject(i).getInt("id"));
                        drug.setDrugimage(array.getJSONObject(i).getString("drugimage"));
                        drug.setDrugname(array.getJSONObject(i).getString("drugname"));
                        drug.setDrugdesc(array.getJSONObject(i).getString("drugdesc"));
                        drug.setDrugcreatedtime(array.getJSONObject(i).getString("drugcreatedtime"));
                        drug.setDrugtaketime(array.getJSONObject(i).getString("drugtaketime"));
                        //0 false
                        drug.setDrugstatus(array.getJSONObject(i).getInt("drugstatus"));
                        drugs.add(drug);
                    }
                }
                initAdapter();
            }
        });
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
                QueryDrugAdapter queryDrugAdapter = new QueryDrugAdapter(drugs);
                recy.setLayoutManager(new LinearLayoutManager(QueryGuardianshipActivity.this));
                recy.setAdapter(queryDrugAdapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_finish:
                finish();
                break;
        }
    }
}
