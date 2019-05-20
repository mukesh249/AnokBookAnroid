package com.anokbook.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;

import com.anokbook.R;

public class AddPostImageAdapter extends RecyclerView.Adapter<AddPostImageAdapter.ViewHolder> {

    ArrayList<Uri> homeBookListModelArrayList=new ArrayList<>();
    Context context;
    int raw_post =0;

    public AddPostImageAdapter(ArrayList<Uri> homeBookListModelArrayList, Context context){
        this.homeBookListModelArrayList = homeBookListModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addpostimage_raw,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
//        homeBookListModel = this.homeBookListModelArrayList.get(i);
//        viewHolder.sc_name.setText(homeBookListModelArrayList.getName());

//        Picasso.with(context).load(homeBookListModelArrayList.get(i))
//                .fit()
//                .into(viewHolder.book_image);
        Glide.with(context).load(homeBookListModelArrayList.get(i)).into(viewHolder.book_image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popupmethod(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeBookListModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelectableRoundedImageView book_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            book_image = (SelectableRoundedImageView)itemView.findViewById(R.id.image1);
        }
        int p = getAdapterPosition();
    }
    private void Popupmethod(int pos){
        final Dialog dialog = new Dialog(context,R.style.MyTheme);
        dialog.setContentView(R.layout.imageperview);

        ImageView cross_icon = (ImageView)dialog.findViewById(R.id.cross_icon);
        ImageView image_perview = (ImageView)dialog.findViewById(R.id.image_perview);

        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Glide.with(context)
                .load(homeBookListModelArrayList.get(pos))
                .into(image_perview);
        // show it
        dialog.show();
    }
}
