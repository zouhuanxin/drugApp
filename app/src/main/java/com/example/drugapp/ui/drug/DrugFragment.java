package com.example.drugapp.ui.drug;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.drugapp.R;
import com.example.drugapp.http.Apis;
import com.example.drugapp.http.HttpCallBack;
import com.example.drugapp.http.HttpUtil;
import com.example.drugapp.model.bean.Drug;
import com.example.drugapp.ui.base.BaseFragment;
import com.example.drugapp.util.ShapeUtil;
import com.example.drugapp.util.ToastUtil;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorGravity;
import com.zhpan.bannerview.constants.IndicatorSlideMode;
import com.zhpan.bannerview.holder.HolderCreator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.zhpan.bannerview.constants.PageStyle.MULTI_PAGE_SCALE;

public class DrugFragment extends BaseFragment {

    private BannerViewPager bannerview;
    private RecyclerView drugRecy;

    private List<String> bannerlist = new ArrayList<>();
    private List<Drug> drugs = new ArrayList<>();
    private SwipeRefreshLayout swipe;

    @Override
    protected int initLayout() {
        return R.layout.fragment_drug;
    }

    @Override
    protected void init() {
        initView(view);
        initData();
        initBanner();
    }

    private void initView(View view) {
        bannerview = (BannerViewPager) view.findViewById(R.id.bannerview);
        drugRecy = (RecyclerView) view.findViewById(R.id.drug_recy);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqData();
            }
        });
    }

    private void initData() {
        bannerlist.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4154168667,3021172379&fm=11&gp=0.jpg");
        bannerlist.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2848120264,2778704476&fm=26&gp=0.jpg");

        reqData();
    }

    private void initBanner() {
        bannerview.showIndicator(true)
                .setInterval(3000)
                .setCanLoop(false)
                .setAutoPlay(true)
                .setIndicatorSlideMode(IndicatorSlideMode.WORM)
                .setRoundCorner(2)
                //.setIndicatorStyle(ROUND_RECT)
                .setIndicatorColor(Color.parseColor("#8C6C6D72"), Color.parseColor("#F9BD00"))
                .setIndicatorGravity(IndicatorGravity.CENTER)
                .setScrollDuration(1000)
                .setPageStyle(MULTI_PAGE_SCALE)
                .setPageMargin(getContext().getResources().getDimensionPixelOffset(R.dimen.dp_15))
                .setRevealWidth(getContext().getResources().getDimensionPixelOffset(R.dimen.dp_15))
                .setHolderCreator(new HolderCreator<NetViewHolder>() {
                    @Override
                    public NetViewHolder createViewHolder() {
                        return new NetViewHolder();
                    }
                })
                .setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                    @Override
                    public void onPageClick(int position) {

                    }
                }).create(bannerlist);
    }

    /**
     * 请求数据
     */
    private void reqData() {
        HttpUtil.getInstance().GET(Apis.getAllDrugData+"?account="+ ShapeUtil.INSTANCE.getUser(getContext()).getAccount(), new HttpCallBack() {
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
                drugs.add(drugs.size(), null);
                DrugAdapter drugAdapter = new DrugAdapter(drugs);
                drugAdapter.setDrugClick(new DrugAdapter.DrugClick() {
                    @Override
                    public void Add() {
                        Intent intent = new Intent(getActivity(), AddDrugActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void Delect(Drug drug) {
                        DelectDrug(drug.getId());
                    }

                    @Override
                    public void Update(Drug drug) {
                        UpdateDrug(drug);
                    }
                });
                drugRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
                drugRecy.setAdapter(drugAdapter);
            }
        });
    }

    /**
     * 删除
     */
    private void DelectDrug(int id) {
        HttpUtil.getInstance().GET(Apis.removeDrug + "?id=" + id, new HttpCallBack() {
            @Override
            public void Error(Call call, IOException e) {

            }

            @Override
            public void Success(Call call, String res) throws Exception {
                JSONObject rep = new JSONObject(res);
                ToastUtil.toast(getActivity(), rep.getString("msg"));
                if (rep.getInt("code") == 200) {
                    reqData();
                }
            }
        });
    }

    /**
     * 前往修改
     */
    private void UpdateDrug(Drug drug) {
        Intent intent = new Intent(getActivity(),UpdateDrugActivity.class);
        intent.putExtra("name", drug.getDrugname());
        intent.putExtra("image", drug.getDrugimage());
        intent.putExtra("desc", drug.getDrugdesc());
        intent.putExtra("drugcreatedtime", drug.getDrugcreatedtime());
        intent.putExtra("taketime", drug.getDrugtaketime());
        intent.putExtra("id", String.valueOf(drug.getId()));
        startActivity(intent);
    }
}
