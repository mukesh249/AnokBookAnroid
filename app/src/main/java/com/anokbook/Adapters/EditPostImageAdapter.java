package com.anokbook.Adapters;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Models.EditPostImageModel;
import com.anokbook.R;

public class EditPostImageAdapter extends RecyclerView.Adapter<EditPostImageAdapter.ViewHolder> implements WebCompleteTask {

    ArrayList<EditPostImageModel> homeBookListModelArrayList=new ArrayList<>();
    ArrayList<Uri> upload_image_array = new ArrayList<>();
    EditPostImageModel editPostImageModel;
    Context context;
    int raw_post;


    public EditPostImageAdapter( ArrayList<Uri> upload_image_array,ArrayList<EditPostImageModel> homeBookListModelArrayList, Context context){
        this.homeBookListModelArrayList = homeBookListModelArrayList;
        this.upload_image_array = upload_image_array;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.editpostimage_raw,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        editPostImageModel = homeBookListModelArrayList.get(i);
        Log.d("image_adapter_uri",homeBookListModelArrayList.get(i).toString());
//            Picasso.with(context).load(homeBookListModelArrayList.get(i)).fit().into(viewHolder.book_image);
                Glide.with(context).load(homeBookListModelArrayList.get(i).getImage_url()).into(viewHolder.book_image);

        viewHolder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeBookListModelArrayList.get(i).getImage_id()!=null){
                    raw_post = i;
                    DeleteIamgeMethod(i);
                }else {
                    for (int k =0;k<upload_image_array.size();k++){
                        upload_image_array.remove(k);
                    }
                    homeBookListModelArrayList.remove(i);
                    notifyItemRemoved(i);
                    notifyItemChanged(i);
                    notifyItemRangeChanged(i, homeBookListModelArrayList.size());
                }
            }
        });
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
        ImageView cross;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            book_image = (SelectableRoundedImageView)itemView.findViewById(R.id.image1);
            cross = (ImageView)itemView.findViewById(R.id.cross_icon);
        }
        int p = getAdapterPosition();
    }

    private void DeleteIamgeMethod(int pos){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("id", homeBookListModelArrayList.get(pos).getImage_id());

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","api_remove_book_attachment");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("book_image_delete_data",objectNew.toString());
            new WebTask(context, WebUrls.BASE_URL,objectNew,this, RequestCode.CODE_Book_Image_Delete,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Book_Image_Delete==taskcode){
            Log.d("book_image_delete_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0) {
                    homeBookListModelArrayList.remove(raw_post);
                    notifyItemRemoved(raw_post);
                    notifyItemChanged(raw_post);
                    notifyItemRangeChanged(raw_post, homeBookListModelArrayList.size());
                    Toast.makeText(context,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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

//        image_perview.setImageBitmap(homeBookListModelArrayList.get(pos));
        Glide.with(context)
                .load(homeBookListModelArrayList.get(pos).getImage_url())
                .into(image_perview);
        // show it
        dialog.show();
    }
}
