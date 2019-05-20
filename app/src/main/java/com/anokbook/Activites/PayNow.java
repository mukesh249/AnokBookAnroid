package com.anokbook.Activites;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class PayNow extends AppCompatActivity implements WebCompleteTask {
    @BindView(R.id.submit_btn) Button submit_btn;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ammount_et) TextView ammount_et;
    String request_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        setContentView(R.layout.activity_pay_now);
        ButterKnife.bind(this,this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pay Now");
        if (getIntent().getExtras()!=null){
            Log.d("Book_amount",getIntent().getExtras().getString("Book_amount",""));
            ammount_et.setText(getIntent().getExtras().getString("Book_amount",""));
            request_id = getIntent().getExtras().getString("request_id","");
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayNowForBook();
            }
        });
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void PayNowForBook(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            dataObj.put("request_id",request_id);
            dataObj.put("total_charge",ammount_et.getText().toString());

            JSONObject requiredObj = new JSONObject();
            requiredObj.put("method","pay_now");
            requiredObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",requiredObj.toString());
            Log.d("pay_now_data",objectNew.toString());
            new WebTask(PayNow.this, WebUrls.BASE_URL,objectNew,PayNow.this, RequestCode.CODE_Update_Wallet,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Update_Wallet == taskcode) {
            try {
                JSONObject object = new JSONObject(response);
                Log.d("pay_now_success", response);
                if (object.optString("status").compareTo("success") == 0) {
                    Toast.makeText(PayNow.this,"Pay Successfully",Toast.LENGTH_SHORT).show();
                    finish();
//                    JSONArray dataArray = object.optJSONArray("data");
//                    wallet_amout_tv.setText(dataArray.optJSONObject(0).optString("wallet_amount"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
