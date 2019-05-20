package com.anokbook.Activites;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.R;

public class SentDetail extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.did_you_receive_lin) LinearLayout did_you_receive_lin;
    @BindView(R.id.drop_icon_tp) ImageView drop_icon_tp;
    @BindView(R.id.contact_to_owner_lin) LinearLayout contact_to_owner_lin;
    @BindView(R.id.transaction_process_lin) LinearLayout transaction_process_lin;
    @BindView(R.id.spin_kit) SpinKitView spin_kit;
    @BindView(R.id.pay_now_tp) TextView pay_now_tp;
    @BindView(R.id.approval_tv_tp) TextView approval_tv_tp;
    @BindView(R.id.profile_edit_toobar) Toolbar toolbar;
    @BindView(R.id.pay_security_nd_service_tax_tv) TextView pay_security_nd_service_tax_tv;
    String book_name,owner_name,request_id,book_id,status;
    double wallet_amount,book_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window  = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_detail);
        ButterKnife.bind(this,this);
        setSupportActionBar(toolbar);
        did_you_receive_lin.setVisibility(View.GONE);
        drop_icon_tp.setVisibility(View.GONE);
        transaction_process_lin.setVisibility(View.GONE);
        contact_to_owner_lin.setVisibility(View.GONE);
        pay_security_nd_service_tax_tv.setVisibility(View.GONE);
        pay_now_tp.setVisibility(View.GONE);
        spin_kit.setVisibility(View.GONE);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras()!=null){
            request_id = getIntent().getExtras().getString("request_id");
            book_name = getIntent().getExtras().getString("book_name");
            owner_name = getIntent().getExtras().getString("owner_name");
            String bn = book_name.substring(0,1).toUpperCase();
            String wn = owner_name.substring(0,1).toUpperCase();

            getSupportActionBar().setTitle(book_name.replaceFirst(book_name.substring(0,1),bn)+"/"+
                    owner_name.replaceFirst(owner_name.substring(0,1),wn));
            StatusSentBook();
        }

        pay_now_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin_kit.setVisibility(View.VISIBLE);
                GetWallet();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void StatusSentBook(){
        try{
            JSONObject dataObj = new JSONObject();
            dataObj.put("request_id",request_id);

            JSONObject reqObj = new JSONObject();
            reqObj.put("method","api_bookrequestsent_byuserlist_detail");
            reqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",reqObj.toString());
            Log.d("StatusSentBook_data",objectNew.toString());
            new WebTask(SentDetail.this, WebUrls.BASE_URL,objectNew,SentDetail.this, RequestCode.CODE_StatusSentBook,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

//    public void ChangeStatusReceivedBook(){
//        try{
//            JSONObject dataObj = new JSONObject();
//            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
//            dataObj.put("book_id", book_id);
//            dataObj.put("status", status);
//
//            JSONObject reqObj = new JSONObject();
//            reqObj.put("method","api_changebook_status");
//            reqObj.put("data",dataObj);
//
//            HashMap objectNew = new HashMap();
//            objectNew.put("request",reqObj.toString());
//            Log.d("changebook_status_data",objectNew.toString());
//            new WebTask(ReceivedDetail.this, WebUrls.BASE_URL,objectNew,ReceivedDetail.this, RequestCode.CODE_ChangeStatusReceivedBokk,1);
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }

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
            new WebTask(SentDetail.this, WebUrls.BASE_URL, objectNew, SentDetail.this, RequestCode.CODE_Get_Wallet, 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_StatusSentBook == taskcode){
            Log.d("StatusSentBook_res",response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                JSONObject dataOje = dataArray.getJSONObject(0);
                if (jsonObject.optString("status").compareTo("success")==0){
                    book_id = dataOje.optString("book_id");
                    transaction_process_lin.setBackground(getDrawable(R.drawable.round_conner_white));
                    transaction_process_lin.setVisibility(View.VISIBLE);
                    if (dataOje.optString("status").compareTo("Approval")==0){
                        approval_tv_tp.setText("Approval Received from book Owner");
                        pay_security_nd_service_tax_tv.setVisibility(View.VISIBLE);
                        pay_now_tp.setVisibility(View.VISIBLE);
                        transaction_process_lin.setVisibility(View.VISIBLE);
                        if (dataOje.optString("pay_status").compareTo("No")==0){
                            did_you_receive_lin.setVisibility(View.GONE);
                            pay_now_tp.setEnabled(true);
                            contact_to_owner_lin.setVisibility(View.GONE);
                            drop_icon_tp.setVisibility(View.GONE);
                            approval_tv_tp.setTextColor(getResources().getColor(R.color.colorGreen));
                            pay_security_nd_service_tax_tv.setTextColor(getResources().getColor(R.color.black));
                            transaction_process_lin.setBackground(getDrawable(R.drawable.round_conner_white));
                        }else {
                            pay_now_tp.setText("Payment Done");
                            pay_now_tp.setEnabled(false);
                            did_you_receive_lin.setVisibility(View.VISIBLE);
                            drop_icon_tp.setVisibility(View.VISIBLE);
                            contact_to_owner_lin.setVisibility(View.VISIBLE);
                            approval_tv_tp.setTextColor(getResources().getColor(R.color.white));
                            pay_security_nd_service_tax_tv.setTextColor(getResources().getColor(R.color.white));
                            transaction_process_lin.setBackground(getDrawable(R.drawable.round_conner_green));
                        }
                        book_amount = (Double.parseDouble(dataOje.optString("security_charge"))+Double.parseDouble(dataOje.optString("service_charge")));
                        pay_security_nd_service_tax_tv.setText("Pay Security + service Tax: "+book_amount+"/-" +
                                "\n( After you return this book to book owner your security charger "+
                                dataOje.optString("security_charge")+" refund from Anok Book )" );
                    }else if (dataOje.optString("status").compareTo("Pending")==0){
                        approval_tv_tp.setText(getString(R.string.pending_approval_from_book_owner));
                        approval_tv_tp.setTextColor(getResources().getColor(R.color.colorRed));
                    }else {
                        approval_tv_tp.setText(jsonObject.optString("message"));
                    }
                }else {
                    Toast.makeText(SentDetail.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Get_Wallet==taskcode){

            try {
                JSONObject object = new JSONObject(response);
                Log.d("get_wallet_success",response);
                if (object.optString("status").compareTo("success")==0){
                    JSONArray dataArray = object.optJSONArray("data");
                    wallet_amount =Double.parseDouble(dataArray.optJSONObject(0).optString("wallet_amount"));
                    spin_kit.setVisibility(View.GONE);

                    if (wallet_amount>=book_amount){
                        Intent intent = new Intent(SentDetail.this,PayNow.class);
                        intent.putExtra("Book_amount",book_amount+"");
                        intent.putExtra("request_id",request_id);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(SentDetail.this,MyWallet.class);
                        intent.putExtra("wallet_amount",wallet_amount);
                        startActivity(intent);
                    }
//                    wallet_amout_tv.setText(dataArray.optJSONObject(0).optString("wallet_amount")+" INR");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        if (RequestCode.CODE_ChangeStatusReceivedBokk==taskcode){
//            Log.d("changebook_status_res",response);
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                JSONArray dataArray = jsonObject.optJSONArray("data");
//                JSONObject dataOje = dataArray.getJSONObject(0);
//                if (jsonObject.optString("status").compareTo("success")==0){
//                    if (dataOje.optString("status").compareTo("Approval")==0){
//                        yes_tp.setEnabled(false);
//                        no_tp.setEnabled(false);
//                        payment_status_tv.setVisibility(View.VISIBLE);
//                        payment_status_tv.setText(getString(R.string.pending_payment_from_requester));
//                    }else {
//                        yes_tp.setEnabled(false);
//                        no_tp.setEnabled(false);
//                        payment_status_tv.setVisibility(View.GONE);
//                        contact_to_requester_lin.setVisibility(View.GONE);
//                        did_you_hand_lin.setVisibility(View.GONE);
//                    }
//                }else {
//                    Toast.makeText(ReceivedDetail.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
