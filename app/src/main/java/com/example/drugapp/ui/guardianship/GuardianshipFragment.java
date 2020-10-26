package com.example.drugapp.ui.guardianship;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.model.bean.UserInfo;
import com.example.drugapp.ui.base.BaseFragment;
import com.example.drugapp.util.ShapeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class GuardianshipFragment extends BaseFragment implements View.OnClickListener {

    private TextView guard;
    private TextView beguard;
    private RecyclerView recy;
    private SwipeRefreshLayout swipe;

    private String url = Apis.ByaccountGruandianships;
    private int type = 0;
    private List<UserInfo> userInfos = new ArrayList<>();


    @Override
    protected void init() {
        initView(view);
        initData();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_guardianship;
    }

    private void initView(View view) {
        guard = (TextView) view.findViewById(R.id.guard);
        beguard = (TextView) view.findViewById(R.id.beguard);
        recy = (RecyclerView) view.findViewById(R.id.recy);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(url, type);
            }
        });
        guard.setOnClickListener(this);
        beguard.setOnClickListener(this);
    }

    private void initData() {
        url = Apis.ByaccountGruandianships;
        type = 0;
        requestData(url, type);
    }

    private void requestData(String url, final int type) {
        HttpUtil.getInstance().GET(url + "?account=" + ShapeUtil.INSTANCE.getUser(getContext()).getAccount(), new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {
                swipe.setRefreshing(false);
            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                if (rep.getInt("code") == 200) {
                    JSONArray array = rep.getJSONArray("data");
                    userInfos.clear();
                    for (int i = 0; i < array.length(); i++) {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setAccount(array.getJSONObject(i).getString("account"));
                        userInfo.setUsername(array.getJSONObject(i).getString("username"));
                        userInfo.setPassword(array.getJSONObject(i).getString("password"));
                        userInfos.add(userInfo);
                    }
                }
                initAdapter(type);
            }
        });
    }

    private void initAdapter(final int type) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
                userInfos.add(userInfos.size(), null);
                GuardianshipAdapter adapter = new GuardianshipAdapter(userInfos, type);
                adapter.setGuardClick(new GuardianshipAdapter.GuardClick() {
                    @Override
                    public void Delect(UserInfo userInfo) {

                    }

                    @Override
                    public void Add(UserInfo userInfo) {
                        Intent intent = new Intent(getActivity(), AddGuardianshipActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void Query(UserInfo userInfo) {
                        Intent intent = new Intent(getActivity(), QueryGuardianshipActivity.class);
                        intent.putExtra("account",userInfo.getAccount());
                        startActivity(intent);
                    }

                    @Override
                    public void CallPhone(UserInfo userInfo) {
                        openCallPhone(userInfo.getAccount());
                    }
                });
                recy.setLayoutManager(new LinearLayoutManager(getContext()));
                recy.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guard:
                type = 0;
                click(type);
                url = Apis.ByaccountGruandianships;
                requestData(url, type);
                break;
            case R.id.beguard:
                type = 1;
                click(type);
                url = Apis.BybeaccountGruandianships;
                requestData(url, type);
                break;
        }
    }

    private void click(int index) {
        guard.setTextColor(Color.parseColor(index == 0 ? "#000000" : "#cccccc"));
        beguard.setTextColor(Color.parseColor(index == 1 ? "#000000" : "#cccccc"));
    }

    /**
     * 拨打电话
     */
    private void openCallPhone(String phone){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);

    }
}
