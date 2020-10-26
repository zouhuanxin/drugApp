package com.example.drugapp.ui.drug;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.example.drugapp.R;
import com.example.drugapp.model.bean.Drug;
import com.kproduce.roundcorners.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.util.V;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {
    private List<Drug> list = new ArrayList<>();
    private DrugClick drugClick;

    public interface DrugClick {
        void Add();
        void Delect(Drug drug);
        void Update(Drug drug);
    }

    public DrugAdapter(List<Drug> list) {
        this.list = list;
    }

    public void setDrugClick(DrugClick drugClick) {
        this.drugClick = drugClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drug, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position) == null) {
            holder.adddrug.setVisibility(View.VISIBLE);
            holder.adddrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add
                    if (drugClick != null) {
                        drugClick.Add();
                    }
                }
            });
        } else {
            holder.group.setVisibility(View.VISIBLE);
            Glide.with(holder.image.getContext()).load(list.get(position).getDrugimage()).into(holder.image);
            holder.name.setText(list.get(position).getDrugname());
            holder.desc.setText(list.get(position).getDrugdesc());
            holder.createdtime.setText(list.get(position).getDrugcreatedtime());
            holder.remindtime.setText(list.get(position).getDrugtaketime());
            holder.delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drugClick != null) {
                        drugClick.Delect(list.get(position));
                    }
                }
            });
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drugClick != null) {
                        drugClick.Update(list.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeLayout group;
        private RoundImageView image;
        private TextView name;
        private TextView desc;
        private TextView createdtime;
        private TextView remindtime;
        private TextView adddrug;
        private TextView delect;
        private TextView update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group = (SwipeLayout) itemView.findViewById(R.id.group);
            image = (RoundImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.desc);
            createdtime = (TextView) itemView.findViewById(R.id.createdtime);
            remindtime = (TextView) itemView.findViewById(R.id.remindtime);
            adddrug = (TextView) itemView.findViewById(R.id.adddrug);
            delect = (TextView) itemView.findViewById(R.id.delect);
            update = (TextView) itemView.findViewById(R.id.update);
        }
    }

}
