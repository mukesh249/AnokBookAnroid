package com.anokbook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.anokbook.Api.WebUrls;
import com.anokbook.Models.SentRequestModel;
import com.anokbook.R;

public class BuyRequestBookListAdapter extends RecyclerView.Adapter<BuyRequestBookListAdapter.ViewHolder> {

    ArrayList<SentRequestModel> sentRequestModelArrayList =new ArrayList<>();
    SentRequestModel sentRequestModel;
    Context context;
    int raw_post =0;

    public BuyRequestBookListAdapter(ArrayList<SentRequestModel> sentRequestModelArrayList, Context context){
        this.sentRequestModelArrayList = sentRequestModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sent_reqyest_raw,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        sentRequestModel = sentRequestModelArrayList.get(i);
        viewHolder.book_name.setText(sentRequestModel.getBook_name());
        viewHolder.author_name.setText(sentRequestModel.getBook_author());
        viewHolder.rent_days.setText(sentRequestModel.getRent_days());
        viewHolder.request_date.setText(sentRequestModel.getRequest_date());
        Glide.with(context).load(WebUrls.IMG_BASE_URL+WebUrls.Book_Image_Url+sentRequestModel.getBook_image())
                .placeholder(R.drawable.book_image).fitCenter()
                .into(viewHolder.book_image);
    }

    @Override
    public int getItemCount() {
        return sentRequestModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_name,author_name,rent_days,request_date;
        ImageView book_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            book_name = (TextView)itemView.findViewById(R.id.book_name);
            author_name = (TextView)itemView.findViewById(R.id.author_name_tv);
            rent_days = (TextView)itemView.findViewById(R.id.rent_days_tv);
            request_date = (TextView)itemView.findViewById(R.id.rented_date_tv);
            book_image = (ImageView)itemView.findViewById(R.id.book_image);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int posi = getAdapterPosition();
//                    UserBookListModel homeBookListModel = userBookListModelArrayList.get(posi);
//                    context.startActivity(new Intent(context, IdoDetail.class).putExtra("book_id",homeBookListModel.getId()).putExtra("user_id",homeBookListModel.getUser_id()).putExtra("Activity","user_profile"));
//                }
//            });
        }
    }
}
