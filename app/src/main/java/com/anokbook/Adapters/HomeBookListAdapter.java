package com.anokbook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.anokbook.Activites.HomeScreen;
import com.anokbook.Activites.IdoDetail;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.HomeBookListModel;
import com.anokbook.R;
import com.bumptech.glide.Glide;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeBookListAdapter extends RecyclerView.Adapter<HomeBookListAdapter.ViewHolder> implements Filterable {

    Context context;
    int raw_post;
    int pos;
    private Activity activity;
    private ArrayList<HomeBookListModel> homeBookListModelArrayList;
    private ArrayList<HomeBookListModel> homeBookListModelArrayList_fliter;
    private OnItemClickListener mListener;

    public HomeBookListAdapter(ArrayList<HomeBookListModel> homeBookListModelArrayList, Context context) {
        super();
        this.homeBookListModelArrayList = homeBookListModelArrayList;
        this.homeBookListModelArrayList_fliter = homeBookListModelArrayList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (HomeScreen.listview) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_book_raw, viewGroup, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_book_grid_raw, viewGroup, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HomeBookListModel homeBookListModel = this.homeBookListModelArrayList.get(i);

        String fsd = homeBookListModel.getBook_name();
        String rd = fsd.substring(0, 1).toUpperCase();

        viewHolder.book_name.setText(fsd.replaceFirst(fsd.substring(0, 1), rd));
        viewHolder.security_charge.setText(homeBookListModel.getMrp());
        viewHolder.post_date.setText(homeBookListModel.getCreated_at());
        DecimalFormat df2 = new DecimalFormat(".##");
        viewHolder.distance_tv.setText(df2.format(homeBookListModel.getDistance()) + " km");
//        Log.d("like_status_r",homeBookListModel.getUser_like_status()+"");
        if (homeBookListModel.getUser_like_status().compareTo("1") == 0) {
            viewHolder.like.setChecked(true);
        } else {
            viewHolder.like.setChecked(false);
        }
        Glide.with(context).load(WebUrls.IMG_BASE_URL + WebUrls.Book_Image_Url + homeBookListModel.getBook_imag())
                .placeholder(R.drawable.book_image)
                .into(viewHolder.book_image);
    }

    @Override
    public int getItemCount() {
        return homeBookListModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    homeBookListModelArrayList = homeBookListModelArrayList_fliter;
                } else {
                    ArrayList<HomeBookListModel> filteredList = new ArrayList<>();
                    for (HomeBookListModel row : homeBookListModelArrayList_fliter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBook_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getAuthor_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getPublication().toLowerCase().contains(charString.toLowerCase())
                                || row.getCat_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getMrp().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    homeBookListModelArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = homeBookListModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                homeBookListModelArrayList = (ArrayList<HomeBookListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, WebCompleteTask {
        TextView book_name, author_name, security_charge, post_date, distance_tv;
        ImageView book_image;
        SparkButton like;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            book_name = (TextView) itemView.findViewById(R.id.book_name);
            author_name = (TextView) itemView.findViewById(R.id.author_name_tv);
            security_charge = (TextView) itemView.findViewById(R.id.security_charge_tv);
            post_date = (TextView) itemView.findViewById(R.id.posted_date_tv);
            book_image = (ImageView) itemView.findViewById(R.id.book_image);
            like = (SparkButton) itemView.findViewById(R.id.like);
            distance_tv = (TextView) itemView.findViewById(R.id.distance_tv);

            like.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mListener !=null){
//                        int posi = getAdapterPosition();
//                        if (posi != RecyclerView.NO_POSITION){
//                            mListener.onItemClick(posi,itemView);
//                        }
//                    }
//                    try {
//
//
                    int posi = getAdapterPosition();
////                    HomeBookListModel homeBookListModel = homeBookListModelArrayList.get(posi);
//                        Intent intent = new Intent(context, IdoDetail.class);
//                        intent.putExtra("book_id", homeBookListModelArrayList.get(posi).getId());
//                        intent.putExtra("user_id", homeBookListModelArrayList.get(posi).getUser_id());
////                    intent.putExtra(context, homeBookListModel);
//                        android.support.v4.util.Pair<View, String> p1 = Pair.create((View) book_image, "Book_image");
//                        android.support.v4.util.Pair<View, String> p2 = Pair.create((View) book_name, "Book_name");
//                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2);
//                        context.startActivity(intent, options.toBundle());
//                    }catch (Exception e){
//
//                    }
                    HomeBookListModel homeBookListModel = homeBookListModelArrayList.get(posi);
                    Intent intent = new Intent(context, IdoDetail.class);
                    intent.putExtra("book_id", homeBookListModel.getId());
                    intent.putExtra("user_id", homeBookListModel.getUser_id());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.like) {
                like.playAnimation();
                LikeMethod(pos);
            }
        }

        public void LikeMethod(int poss) {
            poss = getAdapterPosition();
            HomeBookListModel homeDataWrapper = homeBookListModelArrayList.get(poss);
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                dataObj.put("recuriter_id", homeDataWrapper.getUser_id());
                dataObj.put("book_id", homeDataWrapper.getId());

                JSONObject resqObj = new JSONObject();
                resqObj.put("method", "api_like_books");
                resqObj.put("data", dataObj);

                HashMap objectNew = new HashMap();
                objectNew.put("request", resqObj.toString());
                Log.d("api_like_books_data", objectNew.toString());
                new WebTask(context, WebUrls.BASE_URL, objectNew, this, RequestCode.CODE_Like_Books, 5);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onComplete(String response, int taskcode) {
            if (taskcode == RequestCode.CODE_Like_Books) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optString("status").equals("success")) {
                        JSONObject dataobj = object.optJSONObject("data");
                        if (dataobj.optString("like_status").equalsIgnoreCase("1")) {
                            pos = getAdapterPosition();
                            homeBookListModelArrayList.get(pos).setUser_like_status("1");
//                            like.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite));
                            like.setChecked(true);
                            notifyItemChanged(pos);

                        } else {
                            pos = getAdapterPosition();
                            homeBookListModelArrayList.get(pos).setUser_like_status("0");
                            notifyItemChanged(pos);
                            like.setChecked(false);
//                            like.setBackground(context.getResources().getDrawable(R.drawable.heart));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
