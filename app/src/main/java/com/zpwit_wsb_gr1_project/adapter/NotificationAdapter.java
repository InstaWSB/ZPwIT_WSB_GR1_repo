package com.zpwit_wsb_gr1_project.adapter;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.zpwit_wsb_gr1_project.R;
import com.zpwit_wsb_gr1_project.model.NotificationModel;

import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    Context context;
    List<NotificationModel> list;
   OnNotClicked onNotClicked;

    public NotificationAdapter(Context context, List<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items, parent, false);
        return new NotificationHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {

        holder.notification.setText(list.get(position).getNotification());
        holder.time.setText(calculateTime(list.get(position).getTime()));

        holder.itemView.setOnClickListener(v -> {
            onNotClicked.onClicked(list.get(position).getIdUserFrom());


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String calculateTime(Date date) {
        long millis = date.toInstant().toEpochMilli();
        return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000, DateUtils.FORMAT_ABBREV_TIME).toString();
    }


    public void OnNotClicked(OnNotClicked onNotClicked) {
        this.onNotClicked = onNotClicked;
    }

    public interface OnNotClicked {
        void onClicked(String uid);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class NotificationHolder extends RecyclerView.ViewHolder {

        TextView time, notification;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.timeTv);
            notification = itemView.findViewById(R.id.notification);

        }
    }

}
