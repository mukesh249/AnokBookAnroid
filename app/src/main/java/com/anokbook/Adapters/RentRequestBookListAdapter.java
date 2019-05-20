package com.anokbook.Adapters;

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

import com.anokbook.Activites.SentDetail;
import com.anokbook.Api.WebUrls;
import com.anokbook.Models.RentRequestModel;
import com.anokbook.R;

public class RentRequestBookListAdapter extends RecyclerView.Adapter<RentRequestBookListAdapter.ViewHolder> {

    ArrayList<RentRequestModel> rentRequestModelArrayList =new ArrayList<>();
    RentRequestModel rentRequestModel;
    Context context;
    int raw_post =0;

    public RentRequestBookListAdapter(ArrayList<RentRequestModel> rentRequestModelArrayList, Context context){
        this.rentRequestModelArrayList = rentRequestModelArrayList;
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
        rentRequestModel = rentRequestModelArrayList.get(i);
        String bns = rentRequestModel.getBook_name();
        String bns_fst = bns.substring(0,1).toUpperCase();
        viewHolder.book_name.setText(bns.replaceFirst(bns.substring(0,1),bns_fst));
        String ons = rentRequestModel.getOwner_name();
        String ons_fst = ons.substring(0,1).toUpperCase();
        viewHolder.author_name.setText(ons.replaceFirst(ons.substring(0,1),ons_fst));
        viewHolder.rent_days.setText("Rent for "+rentRequestModel.getRent_days()+" days");
        viewHolder.total_rent_tv.setText(rentRequestModel.getRent()+"/-");
        viewHolder.request_date.setText(rentRequestModel.getRequest_date());
        Glide.with(context).load(WebUrls.IMG_BASE_URL+WebUrls.Book_Image_Url+rentRequestModel.getBook_image())
                .placeholder(R.drawable.book_image).fitCenter()
                .into(viewHolder.book_image);
    }

    @Override
    public int getItemCount() {
        return rentRequestModelArrayList.size();
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
                    RentRequestModel rentRequestModel = rentRequestModelArrayList.get(posi);
                    Intent intent = new Intent(context, SentDetail.class);
                    intent.putExtra("book_name",rentRequestModel.getBook_name());
                    intent.putExtra("owner_name",rentRequestModel.getOwner_name());
                    intent.putExtra("request_id",rentRequestModel.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
