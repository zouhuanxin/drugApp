package com.example.drugapp.ui.guardianship;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.example.drugapp.R;
import com.example.drugapp.model.bean.UserInfo;
import com.kproduce.roundcorners.RoundTextView;

import java.util.List;

public class GuardianshipAdapter extends RecyclerView.Adapter<GuardianshipAdapter.ViewHolder> {
    private List<UserInfo> list;
    private GuardClick guardClick;
    private int type = 0;

    public interface GuardClick {
        void Delect(UserInfo userInfo);

        void Add(UserInfo userInfo);

        void Query(UserInfo userInfo);

        void CallPhone(UserInfo userInfo);
    }

    public GuardianshipAdapter(List<UserInfo> list, int type) {
        this.list = list;
        this.type = type;
    }

    public void setGuardClick(GuardClick guardClick) {
        this.guardClick = guardClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guardianship, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position) == null) {
            holder.add.setVisibility(View.VISIBLE);
            if (type == 0) {
                holder.add.setVisibility(View.VISIBLE);
                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (guardClick != null) {
                            guardClick.Add(list.get(position));
                        }
                    }
                });
            } else {
                if (list.size() == 0) {
                    holder.add.setText("你还没有监护别人哦");
                    holder.add.setTextColor(Color.parseColor("#cccccc"));
                } else {
                    holder.add.setVisibility(View.GONE);
                }
            }
        } else {
            holder.swipeLayout.setVisibility(View.VISIBLE);
            holder.image.setText(list.get(position).getUsername().substring(0, 1));
            holder.account.setText(list.get(position).getUsername());
            holder.username.setText("联系方式:" + list.get(position).getAccount());
            holder.delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (guardClick != null) {
                        guardClick.Delect(list.get(position));
                    }
                }
            });
            holder.query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (guardClick != null) {
                        guardClick.Query(list.get(position));
                    }
                }
            });
            holder.callphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (guardClick != null) {
                        guardClick.CallPhone(list.get(position));
                    }
                }
            });
            if (type == 0) {
                holder.delect.setVisibility(View.VISIBLE);
            } else {
                holder.query.setVisibility(View.VISIBLE);
                holder.callphone.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeLayout swipeLayout;
        private TextView add;
        private TextView delect;
        private TextView query;
        private TextView callphone;
        private RoundTextView image;
        private TextView account;
        private TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delect = (TextView) itemView.findViewById(R.id.delect);
            query = (TextView) itemView.findViewById(R.id.query);
            callphone = (TextView) itemView.findViewById(R.id.callphone);
            image = (RoundTextView) itemView.findViewById(R.id.image);
            account = (TextView) itemView.findViewById(R.id.account);
            username = (TextView) itemView.findViewById(R.id.username);
            add = (TextView) itemView.findViewById(R.id.add);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
        }
    }

}
