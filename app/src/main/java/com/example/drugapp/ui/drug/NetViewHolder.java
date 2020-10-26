package com.example.drugapp.ui.drug;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.drugapp.R;
import com.zhpan.bannerview.holder.ViewHolder;

public class NetViewHolder implements ViewHolder<String> {

    @Override
    public int getLayoutId() {
        return R.layout.item_net;
    }

    @Override
    public void onBind(View itemView, String data, int position, int size) {
        ImageView imageView = itemView.findViewById(R.id.banner_image);
        Glide.with(imageView.getContext()).load(data).into(imageView);
    }

}