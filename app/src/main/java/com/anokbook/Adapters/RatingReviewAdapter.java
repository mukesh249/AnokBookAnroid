package com.anokbook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import com.anokbook.Models.RatingReviewModel;
import com.anokbook.R;

public class RatingReviewAdapter extends RecyclerView.Adapter<RatingReviewAdapter.ViewHolder> {

    ArrayList<RatingReviewModel> ratingReviewModelArrayList=new ArrayList<>();
    RatingReviewModel ratingReviewModel;
    Context context;
    int raw_post =0;

    public RatingReviewAdapter(ArrayList<RatingReviewModel> ratingReviewModelArrayList, Context context){
        this.ratingReviewModelArrayList = ratingReviewModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rating_review_raw,viewGroup,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ratingReviewModel = this.ratingReviewModelArrayList.get(i);

    }

    @Override
    public int getItemCount() {
        return ratingReviewModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,post_date,content;
        RatingBar rating;
        CircleImageView user_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.rating_user_name);
            rating = (RatingBar) itemView.findViewById(R.id.rating_bar);
            content = (TextView)itemView.findViewById(R.id.rating_user_content);
            post_date = (TextView)itemView.findViewById(R.id.posted_date_tv);
            user_image = (CircleImageView) itemView.findViewById(R.id.rating_user_pic);
        }
    }
}
