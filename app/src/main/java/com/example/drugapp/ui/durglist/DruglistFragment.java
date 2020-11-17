package com.example.drugapp.ui.durglist;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.model.bean.Druglist1;
import com.example.drugapp.ui.base.BaseFragment;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class DruglistFragment extends BaseFragment {

    private RecyclerView druglistRecy;
    private DruglistAdapter druglistAdapter;
    private List<Druglist1> druglist1s = new ArrayList<>();
    private Intent intent;

    @Override
    protected void init() {
        initView();
        reqData();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_druglist;
    }

    private void initView() {
        druglistRecy = (RecyclerView) view.findViewById(R.id.druglist_recy);
    }

    @Override
    public void onResume() {
        super.onResume();
        reqData();
    }

    /**
     * 删除
     */
    private void delete(String id){
        HttpUtil.getInstance().GET(Apis.deleteByIdDruglist1 + "?id=" + id, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                if (rep.getInt("code") == 200) {
                    ToastUtil.toast(getActivity(),"删除成功");
                    reqData();
                }
            }
        });
    }

    /**
     * 请求数据
     */
    private void reqData() {
        HttpUtil.getInstance().GET(Apis.getByAccountDruglist1 + "?account=" + ShapeUtil.INSTANCE.getUser(getContext()).getAccount(), new HttpCallBack() {
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
                    druglist1s.add(druglist1s.size(), null);
                    initAdapter();
                }
            }
        });
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        druglistRecy.post(new Runnable() {
            @Override
            public void run() {
                druglistAdapter = new DruglistAdapter(druglist1s);
                druglistAdapter.setDruglistClick(new DruglistAdapter.DrugClick() {
                    @Override
                    public void Add() {
                        intent = new Intent(getActivity(), AddDruglist1Activity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void Click(Druglist1 drug) {
                        intent = new Intent(getActivity(),DruglistDetailsActivity.class);
                        intent.putExtra("name",drug.getName());
                        intent.putExtra("id",String.valueOf(drug.getId()));
                        startActivity(intent);
                    }

                    @Override
                    public void Delect(Druglist1 drug) {
                        delete(String.valueOf(drug.getId()));
                    }

                    @Override
                    public void Update(Druglist1 drug) {
                        intent = new Intent(getActivity(),UpdateDruglist1Activity.class);
                        intent.putExtra("name",drug.getName());
                        intent.putExtra("id",String.valueOf(drug.getId()));
                        startActivity(intent);
                    }
                });
                druglistRecy.setLayoutManager(new LinearLayoutManager(getContext()));
                druglistRecy.setAdapter(druglistAdapter);
            }
        });
    }

}
