package com.anokbook.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class ProfileList extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.logout_tv) TextView logout_tv;
    @BindView(R.id.my_profile_tv) TextView my_profile_tv;
    @BindView(R.id.help_tv) TextView help_tv;
    @BindView(R.id.setting_tv) TextView setting_tv;
    @BindView(R.id.faq_tv) TextView faq_tv;
    @BindView(R.id.my_wallet_tv) TextView my_wallet_tv;
    @BindView(R.id.privacy_policy_tv) TextView privacy_policy_tv;
    @BindView(R.id.toolbar_profile_list) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        ButterKnife.bind(this,this);
        setSupportActionBar(toolbar);
        setTitle("Profile");
        SellerProfile.sellerprofile=false;
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        my_profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileList.this,UserProfile.class).putExtra("user","login"));
            }
        });
        help_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileList.this,Help.class));
            }
        });
        setting_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileList.this,Setting.class));
            }
        });
        faq_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileList.this,FAQ.class));
            }
        });
        privacy_policy_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileList.this,Privacypolicy.class));
            }
        });
        my_wallet_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileList.this,MyWallet.class));
            }
        });
        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogoutMethod();
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
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    public void UserLogoutMethod(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id",SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","api_logout");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("logout_data",objectNew.toString());
            new WebTask(ProfileList.this, WebUrls.BASE_URL,objectNew,ProfileList.this, RequestCode.CODE_Logout_Api,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("logout_res",response);
        if (RequestCode.CODE_Logout_Api == taskcode){
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(ProfileList.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(ProfileList.this).storeIsLoggedIn(false);
                    startActivity(new Intent(ProfileList.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }else {
                    Toast.makeText(ProfileList.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
