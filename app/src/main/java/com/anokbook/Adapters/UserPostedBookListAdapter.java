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

import com.anokbook.Activites.IdoDetail;
import com.anokbook.Activites.SellerProfile;
import com.anokbook.Api.WebUrls;
import com.anokbook.Models.UserBookListModel;
import com.anokbook.R;

public class UserPostedBookListAdapter extends RecyclerView.Adapter<UserPostedBookListAdapter.ViewHolder> {

    ArrayList<UserBookListModel> userBookListModelArrayList=new ArrayList<>();
    UserBookListModel userBookListModel;
    Context context;
    int raw_post =0;

    public UserPostedBookListAdapter(ArrayList<UserBookListModel> userBookListModelArrayList, Context context){
        this.userBookListModelArrayList = userBookListModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_book_raw,viewGroup,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        userBookListModel = this.userBookListModelArrayList.get(i);
//        viewHolder.sc_name.setText(homeBookListModelArrayList.getName());
        viewHolder.book_name.setText(userBookListModel.getBook_name());
        viewHolder.security_charge.setText(userBookListModel.getMrp());
        viewHolder.post_date.setText(userBookListModel.getCreated_at());
        Glide.with(context).load(WebUrls.IMG_BASE_URL+WebUrls.Book_Image_Url+userBookListModel.getBook_imag())
                .placeholder(R.drawable.book_image).fitCenter()
                .into(viewHolder.book_image);
    }

    @Override
    public int getItemCount() {
        return userBookListModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_name,author_name,security_charge,post_date;
        ImageView book_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            book_name = (TextView)itemView.findViewById(R.id.book_name);
            author_name = (TextView)itemView.findViewById(R.id.author_name_tv);
            security_charge = (TextView)itemView.findViewById(R.id.security_charge_tv);
            post_date = (TextView)itemView.findViewById(R.id.posted_date_tv);
            book_image = (ImageView)itemView.findViewById(R.id.book_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = getAdapterPosition();
                    UserBookListModel homeBookListModel = userBookListModelArrayList.get(posi);
                    if (SellerProfile.sellerprofile){
                        context.startActivity(new Intent(context, IdoDetail.class).putExtra("book_id",homeBookListModel.getId()).putExtra("user_id",homeBookListModel.getUser_id()).putExtra("Activity","seller_profile"));
                    }else {
                        context.startActivity(new Intent(context, IdoDetail.class).putExtra("book_id",homeBookListModel.getId()).putExtra("user_id",homeBookListModel.getUser_id()).putExtra("Activity","user_profile"));
                    }

                }
            });
        }
    }
}
