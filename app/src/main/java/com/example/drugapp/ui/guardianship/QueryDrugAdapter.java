package com.example.drugapp.ui.guardianship;

import android.graphics.Color;
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
import com.kproduce.roundcorners.RoundImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryDrugAdapter extends RecyclerView.Adapter<QueryDrugAdapter.ViewHolder> {
    private List<Drug> list = new ArrayList<>();
    private DrugClick drugClick;

    public interface DrugClick {
        void Add();

        void Delect(Drug drug);

        void Update(Drug drug);
    }

    public QueryDrugAdapter(List<Drug> list) {
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
        if (list.size() == 0) {
            holder.adddrug.setVisibility(View.VISIBLE);
            holder.adddrug.setText("他还没有服药提醒");
            return;
        }
        /**
         * 当前时间大于提醒时间加五分钟时 状态为0 则他没有吃药 红色
         * 当前时间大于提醒时间加五分钟时 状态为1 则他有吃药 绿色
         * 当前时间小于提醒时间时候 状态为0 则还没有提醒他 蓝色
         * 当前时间大于提醒时间且小于提醒时间加五分钟时 则系统正在提醒他吃药 黄色
         */
        long dif = subtractionTime(list.get(position).getDrugtaketime());
        if (dif > 5 && list.get(position).getDrugstatus() == 0) {
            //red
            holder.tag.setBackgroundColor(Color.parseColor("#ff0000"));
        } else if (dif > 5 && list.get(position).getDrugstatus() == 1) {
            //green
            holder.tag.setBackgroundColor(Color.parseColor("#5F9EA0"));
        } else if (dif < 0 && list.get(position).getDrugstatus() == 0) {
            //blue
            holder.tag.setBackgroundColor(Color.parseColor("#4678ff"));
        } else if (dif >0 && dif <5) {
            //yellow
            holder.tag.setBackgroundColor(Color.parseColor("#ffff00"));
        }
        holder.group.setVisibility(View.VISIBLE);
        holder.tag.setVisibility(View.VISIBLE);
        Glide.with(holder.image.getContext()).load(list.get(position).getDrugimage()).into(holder.image);
        holder.name.setText(list.get(position).getDrugname());
        holder.desc.setText(list.get(position).getDrugdesc());
        holder.createdtime.setText(list.get(position).getDrugcreatedtime());
        holder.remindtime.setText(list.get(position).getDrugtaketime());
        holder.delect.setVisibility(View.GONE);
        holder.update.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 1 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeLayout group;
        private TextView tag;
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
            tag = (TextView) itemView.findViewById(R.id.tag);
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

    /**
     * 时间减法
     */
    private long subtractionTime(String time2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            Date date2 = df.parse(time2);
            //从对象中拿到时间
            long currentTime = System.currentTimeMillis();
            long createTime = date2.getTime();
            diff = (currentTime - createTime) / 1000 / 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

}
