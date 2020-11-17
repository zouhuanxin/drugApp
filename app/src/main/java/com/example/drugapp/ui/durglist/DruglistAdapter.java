package com.example.drugapp.ui.durglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.example.drugapp.R;
import com.example.drugapp.model.bean.Drug;
import com.example.drugapp.model.bean.Druglist1;
import com.kproduce.roundcorners.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class DruglistAdapter extends RecyclerView.Adapter<DruglistAdapter.ViewHolder> {
    private List<Druglist1> list = new ArrayList<>();
    private DrugClick drugClick;

    public interface DrugClick {
        void Add();
        void Click(Druglist1 drug);
        void Delect(Druglist1 drug);
        void Update(Druglist1 drug);
    }

    public DruglistAdapter(List<Druglist1> list) {
        this.list = list;
    }

    public void setDruglistClick(DrugClick drugClick) {
        this.drugClick = drugClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_druglist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position) == null) {
            holder.adddruglist.setVisibility(View.VISIBLE);
            holder.adddruglist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drugClick != null) {
                        drugClick.Add();
                    }
                }
            });
        } else {
            holder.group.setVisibility(View.VISIBLE);
            holder.name.setText(list.get(position).getName());
            holder.group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drugClick != null) {
                        drugClick.Click(list.get(position));
                    }
                }
            });
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
        private TextView name;
        private TextView adddruglist;
        private TextView delect;
        private TextView update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group = (SwipeLayout) itemView.findViewById(R.id.group);
            name = (TextView) itemView.findViewById(R.id.name);
            adddruglist = (TextView) itemView.findViewById(R.id.adddruglist);
            delect = (TextView) itemView.findViewById(R.id.delect);
            update = (TextView) itemView.findViewById(R.id.update);
        }
    }

}
