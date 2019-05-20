package com.anokbook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import com.anokbook.Models.NotificationModel;
import com.anokbook.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    NotificationModel notificationModel;
    Context context;
    int raw_post =0;

    public NotificationAdapter(ArrayList<NotificationModel> notificationModelArrayList, Context context){
        this.notificationModelArrayList = notificationModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_raw,viewGroup,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        notificationModel = this.notificationModelArrayList.get(i);

    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,time_ago,content;
        CircleImageView user_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.notification_title);
            content = (TextView)itemView.findViewById(R.id.notification_message);
            time_ago = (TextView)itemView.findViewById(R.id.notification_time);
            user_image = (CircleImageView) itemView.findViewById(R.id.notification_book_image);
        }
    }
}
