package com.anokbook.Activites;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.anokbook.R;

public class ForgetMobile extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.logo) ImageView logo;
    @BindView(R.id.mobile_rel) RelativeLayout mobile_rel;
    @BindView(R.id.mobile_number_et) EditText mobile_number_et;
    @BindView(R.id.submit) Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_mobile);

        ButterKnife.bind(this,this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetMobileMethod();
            }
        });
    }

    public void ForgetMobileMethod(){
        if (TextUtils.isEmpty(mobile_number_et.getText().toString().trim())){
            mobile_number_et.setError(getString(R.string.mobile_required));
            mobile_number_et.findFocus();
        }else {
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("phone",mobile_number_et.getText().toString().trim());

                JSONObject requiredObj = new JSONObject();
                requiredObj.put("method","forgot_password");
                requiredObj.put("data",dataObj);

                HashMap objectNew = new HashMap();
                objectNew.put("request",requiredObj.toString());
                Log.d("forgotmobile_data",objectNew.toString());
                new WebTask(ForgetMobile.this, WebUrls.BASE_URL,objectNew,ForgetMobile.this, RequestCode.CODE_ForgetMobile,1);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("forgotmobile_res",response);
        if (RequestCode.CODE_ForgetMobile == taskcode){
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(ForgetMobile.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgetMobile.this,OptVerification.class);

                    Pair[] pairs = new Pair[3];
                    pairs[0] = new Pair<View,String>(submit,"btn_trans");
                    pairs[1] = new Pair<View,String>(logo,"logo_trans");
                    pairs[2] = new Pair<View,String>(mobile_rel,"mobile_trans");

                    intent.putExtra("Activity","ForgetMobile");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetMobile.this,pairs);
                    startActivity(intent,options.toBundle());
                }else {
                    Toast.makeText(ForgetMobile.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
