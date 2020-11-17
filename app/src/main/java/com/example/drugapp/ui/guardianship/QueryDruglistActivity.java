package com.example.drugapp.ui.guardianship;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.model.bean.Drug;
import com.example.drugapp.model.bean.Druglist1;
import com.example.drugapp.model.bean.Druglist2;
import com.example.drugapp.util.ShapeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class QueryDruglistActivity extends AppCompatActivity {

    private List<Druglist1> druglist1s = new ArrayList<>();
    private List<Druglist2> druglist2s = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private TextView content;
    private String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querydruglist);
        initView();
        initData();
    }

    private void initView() {
        content = (TextView) findViewById(R.id.content);
    }

    private void initData() {
        reqlist1();
    }

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
                    initText();
                }
            }
        });
    }

    //自调
    private void initText(){
        str = "";
        if (druglist1s.size() == 0){
            return;
        }
        reqlist2(0);
    }

    private void reqlist2(int index) {
        HttpUtil.getInstance().GET(Apis.getByIdDruglist2 + "?id=" + druglist1s.get(index).getId(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                if (rep.getInt("code") == 200) {
                    str = str.trim() + "\n⭕️ "+druglist1s.get(index).getName() + "\n        ";
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
                        str = str + "🌟 "+druglist2.getDrugname()+"\n        ";
                    }
                    if (index == druglist1s.size() -1){
                        content.setText(str);
                        return;
                    }
                    int i = index + 1;
                    reqlist2(i);
                }
            }
        });
    }

}
