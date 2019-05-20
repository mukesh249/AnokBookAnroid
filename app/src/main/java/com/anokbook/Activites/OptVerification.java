package com.anokbook.Activites;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class OptVerification extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.logo) ImageView logo;
    @BindView(R.id.mobile_rel) RelativeLayout mobile_rel;
    @BindView(R.id.opt_et) EditText opt_et;
    @BindView(R.id.resend_tv) TextView resend_tv;
    @BindView(R.id.verify) Button verify_btn;
    String fromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_verification);

        ButterKnife.bind(this,this);
        resend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendMethod();
            }
        });

        if (getIntent().getExtras()!=null)
        fromActivity = getIntent().getExtras().getString("Activity","");

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOtpMethod();
            }
        });
    }

    public void VerifyOtpMethod(){
        if (TextUtils.isEmpty(opt_et.getText().toString().trim())){
            opt_et.setError(getString(R.string.opt_required));
            opt_et.requestFocus();
        }else {
            try {
                JSONObject dataObject = new JSONObject();
                dataObject.put("otp",opt_et.getText().toString().trim());
                dataObject.put("user_id",SharedPrefManager.getUserID(Constrants.UserId));

                JSONObject requestobj = new JSONObject();
                requestobj.put("method","check_otp");
                requestobj.put("data",dataObject);

                HashMap objectNew = new HashMap();
                objectNew.put("request",requestobj.toString());
                Log.d("check_otp_res:", objectNew.toString());
                new WebTask(OptVerification.this, WebUrls.BASE_URL,objectNew,OptVerification.this, RequestCode.CODE_OtpCheck,1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void ResendMethod(){
        try {
            JSONObject dataObject = new JSONObject();
            dataObject.put("user_id",SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject requestobj = new JSONObject();
            requestobj.put("method","resend_otp");
            requestobj.put("data",dataObject);

            HashMap objectNew = new HashMap();
            objectNew.put("request",requestobj.toString());
            Log.d("resend_otp_res:", objectNew.toString());
              new WebTask(OptVerification.this, WebUrls.BASE_URL,objectNew,OptVerification.this, RequestCode.CODE_ResendOtp,1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("otp_res:", response);
        if (RequestCode.CODE_OtpCheck == taskcode) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success") == 0) {
                    if (fromActivity!=null && fromActivity.compareTo("ForgetMobile")==0){
                        Toast.makeText(OptVerification.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OptVerification.this,NewChangePassword.class);

                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View,String>(logo,"logo_trans");
                        pairs[1] = new Pair<View,String>(verify_btn,"btn_trans");
                        pairs[2] = new Pair<View,String>(mobile_rel,"mobile_trans");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OptVerification.this,pairs);
                        startActivity(intent,options.toBundle());
                        finish();

                    }else {
                        Toast.makeText(OptVerification.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OptVerification.this, Login.class);
                        Pair[] pairs = new Pair[3];
                        pairs[0] = new Pair<View,String>(logo,"logo_trans");
                        pairs[1] = new Pair<View,String>(verify_btn,"btn_trans");
                        pairs[2] = new Pair<View,String>(mobile_rel,"mobile_trans");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OptVerification.this,pairs);
                        startActivity(intent,options.toBundle());
                        finish();

                    }

                } else {
                    Toast.makeText(OptVerification.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (RequestCode.CODE_ResendOtp==taskcode){
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success") == 0) {
                    Toast.makeText(OptVerification.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OptVerification.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
