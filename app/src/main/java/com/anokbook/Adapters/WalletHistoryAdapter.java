package com.anokbook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.anokbook.Models.WalletHistoryModel;
import com.anokbook.R;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.ViewHolder> {

    ArrayList<WalletHistoryModel> walletHistoryModelArrayList=new ArrayList<>();
    WalletHistoryModel walletHistoryModel;
    Context context;
    int raw_post =0;

    public WalletHistoryAdapter(ArrayList<WalletHistoryModel> walletHistoryModelArrayList, Context context){
        this.walletHistoryModelArrayList = walletHistoryModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_history_raw,viewGroup,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        walletHistoryModel = this.walletHistoryModelArrayList.get(i);
        viewHolder.history_amount.setText(walletHistoryModel.getHistory_amount());
        viewHolder.history_date.setText(walletHistoryModel.getHistory_date());
        viewHolder.history_time.setText(walletHistoryModel.getHistroy_time());

        if (walletHistoryModel.getHistory_title()!=null){
            String st = walletHistoryModel.getHistory_title();
            String stf = st.substring(0,1).toUpperCase();

            viewHolder.title_history.setText(st.replaceFirst(st.substring(0,1),stf));
        }

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivity(new Intent(context, IdoDetail.class));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return walletHistoryModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_history,history_date,history_time,history_amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title_history = (TextView)itemView.findViewById(R.id.title_history);
            history_amount = (TextView)itemView.findViewById(R.id.amount_history_tv);
            history_time = (TextView)itemView.findViewById(R.id.time_history_tv);
            history_date = (TextView)itemView.findViewById(R.id.date_history_tv);
        }
    }
}
