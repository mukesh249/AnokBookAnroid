package com.anokbook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.anokbook.Activites.ReceivedDetail;
import com.anokbook.Api.WebUrls;
import com.anokbook.Models.ReveivedRequestModel;
import com.anokbook.R;

public class ReceivedRequestBookListAdapter extends
        RecyclerView.Adapter<ReceivedRequestBookListAdapter.ViewHolder> {

    ArrayList<ReveivedRequestModel> reveivedRequestModelArrayList =new ArrayList<>();
    ReveivedRequestModel reveivedRequestModel;
    Context context;
    int raw_post =0;
    Activity activity;

    public ReceivedRequestBookListAdapter(ArrayList<ReveivedRequestModel>
                                                  reveivedRequestModelArrayList, Context context){
        this.reveivedRequestModelArrayList = reveivedRequestModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.sent_reqyest_raw,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        reveivedRequestModel = reveivedRequestModelArrayList.get(i);

        if (reveivedRequestModel.getBook_name()!=null){
            String bns = reveivedRequestModel.getBook_name();
            String bns_fst = bns.substring(0,1).toUpperCase();
            viewHolder.book_name.setText(bns.replaceFirst(bns.substring(0,1),bns_fst));
        }


        if (reveivedRequestModel.getRequester_name()!=null && reveivedRequestModel.getRequester_name().compareTo("")!=0) {
            String rns = reveivedRequestModel.getRequester_name();
            String rns_fst = rns.substring(0, 1).toUpperCase();
            viewHolder.author_name.setText(rns.replaceFirst(rns.substring(0, 1), rns_fst));
        }
        viewHolder.rent_days.setText("Rent for "+reveivedRequestModel.getDuration()+" days");
        viewHolder.total_rent_tv.setText(reveivedRequestModel.getRent()+"/-");
        viewHolder.request_date.setText(reveivedRequestModel.getCreated_date());

        Glide.with(context)
                .load(WebUrls.IMG_BASE_URL
                        +WebUrls.Book_Image_Url+reveivedRequestModel.getBook_image())
                .placeholder(R.drawable.book_image).fitCenter()
                .into(viewHolder.book_image);
    }

    @Override
    public int getItemCount() {
        return reveivedRequestModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_name,author_name,rent_days,request_date,total_rent_tv;
        ImageView book_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            book_name = (TextView)itemView.findViewById(R.id.book_name);
            author_name = (TextView)itemView.findViewById(R.id.author_name_tv);
            rent_days = (TextView)itemView.findViewById(R.id.rent_days_tv);
            request_date = (TextView)itemView.findViewById(R.id.rented_date_tv);
            total_rent_tv = (TextView)itemView.findViewById(R.id.total_rent_tv);
            book_image = (ImageView)itemView.findViewById(R.id.book_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = getAdapterPosition();
                    ReveivedRequestModel reveivedRequestModel = reveivedRequestModelArrayList.get(posi);
                    Intent intent = new Intent(context,ReceivedDetail.class);
                    intent.putExtra("book_name",reveivedRequestModel.getBook_name());
                    intent.putExtra("requester_name",reveivedRequestModel.getRequester_name());
                    intent.putExtra("request_id",reveivedRequestModel.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
