package com.anokbook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.anokbook.Activites.HomeScreen;
import com.anokbook.Activites.YourArePreparing;
import com.anokbook.Models.ShowingCategoriesModel;
import com.anokbook.R;

public class ShowingCategoriesAdapter extends RecyclerView.Adapter<ShowingCategoriesAdapter.ViewHolder> {

    ArrayList<ShowingCategoriesModel> showingCategoriesModelArrayList=new ArrayList<>();
    ShowingCategoriesModel showingCategoriesModel;
    public static ArrayList<String> arrayList_id= new ArrayList<>();
    public static ArrayList<String> arrayList_name = new ArrayList<>();
    Context context;
    public static String select_cat,select_cat_id ;

    int raw_post =0;

    public ShowingCategoriesAdapter(ArrayList<ShowingCategoriesModel> showingCategoriesModelArrayList, Context context){
        this.showingCategoriesModelArrayList = showingCategoriesModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (YourArePreparing.you_are_preparing){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.your_are_preparing_raw,viewGroup,false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.showing_categories_raw,viewGroup,false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        showingCategoriesModel = this.showingCategoriesModelArrayList.get(i);
        viewHolder.sc_name.setText(showingCategoriesModel.getName());

        if (YourArePreparing.you_are_preparing) {
            viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            if (showingCategoriesModel.getCheck().compareTo("1")==0){
                showingCategoriesModel.setSelected(true);
                viewHolder.cardView.setAlpha(0.7f);
                arrayList_id.add(showingCategoriesModel.getId());
                arrayList_name.add(showingCategoriesModel.getName());

            }else {
                viewHolder.cardView.setAlpha(1.0f);
                showingCategoriesModel.setSelected(false);
            }
        } else{
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    raw_post = viewHolder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
            if (i == raw_post) {
                if (!viewHolder.radio_btn.isChecked()) {
                    if (context instanceof HomeScreen) {
                        viewHolder.rel.setBackground(context.getResources().getDrawable(R.drawable.round_conner_blue));
                        viewHolder.sc_name.setPadding(15, 0, 15, 0);
                        viewHolder.sc_name.setTextColor(context.getResources().getColor(R.color.white));
                        viewHolder.radio_btn.setChecked(true);
                    } else {
                        viewHolder.sc_name.setPadding(5, 5, 5, 5);
                        viewHolder.rel.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                        viewHolder.radio_btn.setChecked(true);
                        select_cat = viewHolder.sc_name.getText().toString();
                        select_cat_id = showingCategoriesModelArrayList.get(i).getId();
                    }
                }

            } else {
                viewHolder.sc_name.setPadding(5, 5, 5, 5);
                viewHolder.rel.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.sc_name.setTextColor(context.getResources().getColor(R.color.black));
                viewHolder.radio_btn.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return showingCategoriesModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sc_name;
        RadioButton radio_btn;
        RelativeLayout rel;
        CardView cardView;
        int pos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (YourArePreparing.you_are_preparing){
                sc_name = (TextView)itemView.findViewById(R.id.you_r_pre_tv);
                cardView = (CardView)itemView.findViewById(R.id.card_view);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pos = getAdapterPosition();
                        showingCategoriesModel = showingCategoriesModelArrayList.get(pos);
                        if (!showingCategoriesModel.isSelected()){
                            showingCategoriesModel.setSelected(true);
                            cardView.setAlpha(0.7f);
//                            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.gray));
                            arrayList_id.add(showingCategoriesModel.getId());
                            arrayList_name.add(showingCategoriesModel.getName());
//                        al_listProfile.remove(getFinal.get(listPosition).getPic());
                        }else {
                            cardView.setAlpha(1.0f);
                            showingCategoriesModel.setSelected(false);
                            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                            arrayList_id.remove(showingCategoriesModel.getId());
                            arrayList_id.remove(showingCategoriesModel.getName());
//                        al_listProfile.add(getFinal.get(listPosition).getPic());

                        }
                    }
                });
            }else {
                sc_name = (TextView)itemView.findViewById(R.id.showcat_name_tv);
                radio_btn = (RadioButton)itemView.findViewById(R.id.radio_btn);
                radio_btn.setClickable(false);
                rel = (RelativeLayout)itemView.findViewById(R.id.show_raw_rel);
                if (context instanceof  HomeScreen){
                    radio_btn.setVisibility(View.GONE);
                }else {
                    radio_btn.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
