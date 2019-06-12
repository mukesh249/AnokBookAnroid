package com.anokbook.Activites;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.anokbook.Adapters.WalletHistoryAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.WalletHistoryModel;
import com.anokbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWallet extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.recycerview_histroy_wallet)
    RecyclerView recyclerview_history_wallet;
    ArrayList<WalletHistoryModel> walletHistoryAddArrayList = new ArrayList<>();
    ArrayList<WalletHistoryModel> walletHistoryPaidArrayList = new ArrayList<>();
    WalletHistoryAdapter walletHistoryAdapter;

    @BindView(R.id.wallet_amout_tv)
    TextView wallet_amout_tv;
    @BindView(R.id.add_money_wallet_tv)
    TextView add_money_wallet_tv;
    @BindView(R.id.send_money_on_bank_tv)
    TextView send_money_on_bank_tv;
    @BindView(R.id.profile_edit_toobar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        ButterKnife.bind(this, this);

        add_money_wallet_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWallet.this, AddMoney.class).putExtra("wallet_amount", wallet_amout_tv.getText().toString().trim()));
            }
        });

        send_money_on_bank_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWallet.this, SendMonyeOnBank.class));
            }
        });
        recyclerview_history_wallet.setLayoutManager(new LinearLayoutManager(this));


        setSupportActionBar(toolbar);
        setTitle("My Wallet");
        GetWallet();
//        AddMoneyHistory();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetWallet();
        AddMoneyHistory();
    }

    public void GetWallet() {
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject requiredObj = new JSONObject();
            requiredObj.put("method", "get_wallet");
            requiredObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", requiredObj.toString());
            Log.d("get_wallet_data", objectNew.toString());
            new WebTask(MyWallet.this, WebUrls.BASE_URL, objectNew, MyWallet.this, RequestCode.CODE_Get_Wallet, 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddMoneyHistory() {
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject requiredObj = new JSONObject();
            requiredObj.put("method", "get_transaction");
            requiredObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", requiredObj.toString());
            Log.d("get_transaction_data", objectNew.toString());
            new WebTask(MyWallet.this, WebUrls.BASE_URL, objectNew, MyWallet.this, RequestCode.CODE_Get_Transaction, 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void PaidHistory() {
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject requiredObj = new JSONObject();
            requiredObj.put("method", "paidbooklist");
            requiredObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", requiredObj.toString());
            Log.d("paidlist_data", objectNew.toString());
            new WebTask(MyWallet.this, WebUrls.BASE_URL, objectNew, MyWallet.this, RequestCode.CODE_PaidList, 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Get_Wallet == taskcode) {
            try {
                JSONObject object = new JSONObject(response);
                Log.d("get_wallet_success", response);
                if (object.optString("status").compareTo("success") == 0) {
                    JSONArray dataArray = object.optJSONArray("data");
                    wallet_amout_tv.setText(dataArray.optJSONObject(0).optString("wallet_amount"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Get_Transaction == taskcode) {
            try {
                JSONObject object = new JSONObject(response);
                Log.d("get_transaction_success", response);
                if (object.optString("status").compareTo("success") == 0) {
                    JSONArray dataArray = object.optJSONArray("data");
                    walletHistoryAddArrayList.clear();
                    walletHistoryPaidArrayList.clear();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject obj = dataArray.optJSONObject(i);
                        WalletHistoryModel walletHistoryModel = new WalletHistoryModel();
                        walletHistoryModel.setHistory_id(obj.optString("user_id"));
                        walletHistoryModel.setHistory_amount(obj.optString("amount"));
                        walletHistoryModel.setPayment_id(obj.optString("payment_id"));

                        String dates = obj.optString("created_at");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                        Date date = dateFormat.parse(dates);
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                        SimpleDateFormat newtime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                        walletHistoryModel.setHistory_date(newFormat.format(date) + "");
                        walletHistoryModel.setHistroy_time(newtime.format(date) + "");
                        walletHistoryAddArrayList.add(walletHistoryModel);
                    }
                    if (walletHistoryAddArrayList != null && walletHistoryAddArrayList.size() > 0)
                        PaidHistory();
//                    walletHistoryAdapter = new WalletHistoryAdapter(walletHistoryModelArrayList,MyWallet.this);
//                    recyclerview_history_wallet.setAdapter(walletHistoryAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_PaidList == taskcode) {
            try {
                JSONObject object = new JSONObject(response);
                Log.d("paidlist_success", response);
                if (object.optString("status").compareTo("success") == 0) {
                    JSONArray dataArray = object.optJSONArray("data");
                    walletHistoryPaidArrayList.clear();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject obj = dataArray.optJSONObject(i);
                        WalletHistoryModel walletHistoryModel = new WalletHistoryModel();
                        walletHistoryModel.setHistory_id(obj.optString("id"));
                        walletHistoryModel.setHistory_amount(Double.parseDouble(obj.optString("rent")) + Double.parseDouble(obj.optString("service_charge")) + Double.parseDouble(obj.optString("security_charge")) + "");
                        walletHistoryModel.setPayment_id(obj.optString("payment_id"));
                        walletHistoryModel.setHistory_title(obj.optString("book_name"));

                        String dates = obj.optString("created_at");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                        Date date = dateFormat.parse(dates);
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                        SimpleDateFormat newtime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                        walletHistoryModel.setHistory_date(newFormat.format(date) + "");
                        walletHistoryModel.setHistroy_time(newtime.format(date) + "");
                        walletHistoryPaidArrayList.add(walletHistoryModel);
                    }
                    walletHistoryAddArrayList.addAll(walletHistoryPaidArrayList);
                    walletHistoryAdapter = new WalletHistoryAdapter(walletHistoryAddArrayList, MyWallet.this);
                    recyclerview_history_wallet.setAdapter(walletHistoryAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}