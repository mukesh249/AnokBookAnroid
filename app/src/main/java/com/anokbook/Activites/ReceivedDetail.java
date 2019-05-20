package com.anokbook.Activites;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ReceivedDetail extends AppCompatActivity implements WebCompleteTask {
    @BindView(R.id.transaction_process_lin) LinearLayout transaction_process_lin;
    @BindView(R.id.contact_to_requester_lin) LinearLayout contact_to_requester_lin;
    @BindView(R.id.did_you_hand_lin) LinearLayout did_you_hand_lin;
    @BindView(R.id.transaction_process_rel) RelativeLayout transaction_process_rel;
    @BindView(R.id.card_tp) CardView card_tp;
    @BindView(R.id.card_request) CardView card_request;
    @BindView(R.id.did_you_hand_rel) RelativeLayout did_you_hand_rel;
    @BindView(R.id.transaction_process_sh) LinearLayout transaction_process_sh;
    @BindView(R.id.view_requester_detail_tv) TextView view_requester_detail_tv;
    @BindView(R.id.did_you_hand_lin_sh) LinearLayout did_you_hand_lin_sh;
    @BindView(R.id.profile_edit_toobar) Toolbar toolbar;
    @BindView(R.id.payment_status_tv) TextView payment_status_tv;
    @BindView(R.id.yes_tp) TextView yes_tp;
    @BindView(R.id.no_tp) TextView no_tp;
    @BindView(R.id.drop_icon_tp) ImageView drop_icon_tp;
    @BindView(R.id.yes_did_u) TextView yes_did_u;
    @BindView(R.id.no_did_u) TextView no_did_u;
    String book_name,requester_name,request_id,book_id,status,handouver_status;
    Boolean tp=false,di=false,cd=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_detail);
        ButterKnife.bind(this,this);
        card_tp.setVisibility(View.GONE);
        contact_to_requester_lin.setVisibility(View.GONE);
        card_request.setVisibility(View.GONE);
        drop_icon_tp.setVisibility(View.GONE);
//        did_you_hand_lin_sh.setVisibility(View.GONE);
        card_tp.setCardBackgroundColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras()!=null)
        {
            request_id = getIntent().getExtras().getString("request_id");
            book_name = getIntent().getExtras().getString("book_name");
            requester_name = getIntent().getExtras().getString("requester_name");
            String bn = book_name.substring(0,1).toUpperCase();
            String rn = requester_name.substring(0,1).toUpperCase();
            getSupportActionBar().setTitle(book_name.replaceFirst(book_name.substring(0,1),bn)
                    +"/"+requester_name.replaceFirst(requester_name.substring(0,1),rn));
            StatusReceivedBook();
        }
        yes_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "Approval";
                ChangeStatusReceivedBook();
            }
        });
        no_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "Reject";
                ChangeStatusReceivedBook();
            }
        });

        yes_did_u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handouver_status = "Yes";
                BookHandOverStatus();
            }
        });
        no_did_u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handouver_status = "No";
                BookHandOverStatus();
            }
        });
//        if (status.compareTo("")==0){
//
//        }
//        transaction_process_sh.setVisibility(View.GONE);
//        did_you_hand_lin_sh.setVisibility(View.GONE);
//        view_requester_detail_tv.setVisibility(View.GONE);
//        transaction_process_rel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(tp){
//                    tp = false;
//                    transaction_process_sh.setVisibility(View.GONE);
//                } else{
//                    tp = true;
//                    transaction_process_sh.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        did_you_hand_rel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(di){
//                    di = false;
//                    did_you_hand_lin_sh.setVisibility(View.GONE);
//                } else{
//                    di = true;
//                    did_you_hand_lin_sh.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        contact_to_requester_rel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(cd){
//                    cd = false;
//                    view_requester_detail_tv.setVisibility(View.GONE);
//                } else{
//                    cd = true;
//                    view_requester_detail_tv.setVisibility(View.VISIBLE);
//                }
//            }
//        });
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
    public void BookHandOverStatus(){
        try{
            JSONObject dataObj = new JSONObject();
            dataObj.put("request_id",request_id);
            dataObj.put("status",handouver_status);

            JSONObject reqObj = new JSONObject();
            reqObj.put("method","api_book_rent_start");
            reqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",reqObj.toString());
            Log.d("book_rent_start_data",objectNew.toString());
            new WebTask(ReceivedDetail.this, WebUrls.BASE_URL,objectNew,ReceivedDetail.this, RequestCode.CODE_HandOverStatus,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void StatusReceivedBook(){
        try{
            JSONObject dataObj = new JSONObject();
            dataObj.put("request_id",request_id);

            JSONObject reqObj = new JSONObject();
            reqObj.put("method","api_bookreceived_byuserlist_detail");
            reqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",reqObj.toString());
            Log.d("bookreceived_data",objectNew.toString());
            new WebTask(ReceivedDetail.this, WebUrls.BASE_URL,objectNew,ReceivedDetail.this, RequestCode.CODE_StatusReceivedBook,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void ChangeStatusReceivedBook(){
        try{
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            dataObj.put("book_id", book_id);
            dataObj.put("status", status);

            JSONObject reqObj = new JSONObject();
            reqObj.put("method","api_changebook_status");
            reqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",reqObj.toString());
            Log.d("changebook_status_data",objectNew.toString());
            new WebTask(ReceivedDetail.this, WebUrls.BASE_URL,objectNew,ReceivedDetail.this, RequestCode.CODE_ChangeStatusReceivedBokk,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_StatusReceivedBook == taskcode){
            Log.d("bookreceivedDetail_res",response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                JSONObject dataOje = dataArray.getJSONObject(0);
                if (jsonObject.optString("status").compareTo("success")==0){
                    book_id = dataOje.optString("book_id");
                    if (dataOje.optString("status").compareTo("Approval")==0){
                        yes_tp.setEnabled(false);
                        no_tp.setEnabled(false);
                        card_tp.setVisibility(View.VISIBLE);
                        contact_to_requester_lin.setVisibility(View.GONE);
                        card_request.setVisibility(View.GONE);
                        payment_status_tv.setVisibility(View.VISIBLE);
                        if (dataOje.optString("pay_status").compareToIgnoreCase("Yes")==0){
                            payment_status_tv.setText("Payment Done by Requester");
                            card_tp.setCardBackgroundColor(getResources().getColor(R.color.gray));
                            drop_icon_tp.setVisibility(View.VISIBLE);
                            contact_to_requester_lin.setVisibility(View.VISIBLE);
                            card_request.setVisibility(View.VISIBLE);
                            if (dataOje.optString("agree_status_to")!=null){
                                if (dataOje.optString("agree_status_to").compareToIgnoreCase("Yes")==0){
                                    did_you_hand_lin_sh.setVisibility(View.VISIBLE);
                                }else {
                                    did_you_hand_lin_sh.setVisibility(View.VISIBLE);
                                }
                            }else {
                                did_you_hand_lin_sh.setVisibility(View.GONE);
                            }
                        }else {
                            drop_icon_tp.setVisibility(View.GONE);
                            payment_status_tv.setText(getString(R.string.pending_payment_from_requester));
                            contact_to_requester_lin.setVisibility(View.GONE);
                            card_request.setVisibility(View.GONE);
                        }


                    }else if (dataOje.optString("status").compareTo("Pending")==0){
                        yes_tp.setEnabled(true);
                        no_tp.setEnabled(true);
                        payment_status_tv.setVisibility(View.GONE);
                        card_tp.setVisibility(View.GONE);
                        card_request.setVisibility(View.GONE);
                        contact_to_requester_lin.setVisibility(View.GONE);
                        did_you_hand_lin.setVisibility(View.GONE);
                    }else {
                        yes_tp.setEnabled(false);
                        no_tp.setEnabled(false);
                        payment_status_tv.setVisibility(View.GONE);
                        card_tp.setVisibility(View.GONE);
                        card_request.setVisibility(View.GONE);
                        contact_to_requester_lin.setVisibility(View.GONE);
                        did_you_hand_lin.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(ReceivedDetail.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_ChangeStatusReceivedBokk==taskcode){
            Log.d("changebook_status_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                JSONObject dataOje = dataArray.getJSONObject(0);
                if (jsonObject.optString("status").compareTo("success")==0){
                    if (dataOje.optString("status").compareTo("Approval")==0){
                        yes_tp.setEnabled(false);
                        no_tp.setEnabled(false);
                        payment_status_tv.setVisibility(View.VISIBLE);
                        payment_status_tv.setText(getString(R.string.pending_payment_from_requester));
                    }else {
                        yes_tp.setEnabled(false);
                        no_tp.setEnabled(false);
                        payment_status_tv.setVisibility(View.GONE);
                        contact_to_requester_lin.setVisibility(View.GONE);
                        did_you_hand_lin.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(ReceivedDetail.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_HandOverStatus == taskcode){
            Log.d("Handoverstatus_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0
                        && jsonObject.optJSONObject("data").optString("agree_status_from").compareToIgnoreCase("Yes")==0){
                    did_you_hand_lin_sh.setVisibility(View.VISIBLE);
                }else {
                    did_you_hand_lin_sh.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
