package com.anokbook.Activites;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class Login extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.mobile_rel) RelativeLayout mobile_rel;
    @BindView(R.id.password_rel) RelativeLayout password_rel;
    @BindView(R.id.logo) ImageView logo;
    @BindView(R.id.mobile_et) EditText mobile_et;
    @BindView(R.id.password_et)EditText password_et;
    @BindView(R.id.create_new_tv) TextView create_new_tv;
    @BindView(R.id.forgot_tv)TextView forgot_tv;
    @BindView(R.id.checkbox) CheckBox checkbox;
    @BindView(R.id.term_condition_tv)TextView term_condition_tv;
    @BindView(R.id.login_btn) Button login_btm;
    @BindView(R.id.progress_horizontal) ProgressBar progress_horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        setContentView(R.layout.activity_signup_and_login);
        ButterKnife.bind(this,this);

        progress_horizontal.setVisibility(View.GONE);
        SharedPrefManager.getInstance(this).hideSoftKeyBoard(this);
        create_new_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registration.class);

                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View,String>(login_btm,"btn_trans");
                pairs[1] = new Pair<View,String>(logo,"logo_trans");
                pairs[2] = new Pair<View,String>(mobile_rel,"mobile_trans");
                pairs[3] = new Pair<View,String>(password_rel,"pass_trans");
                pairs[4] = new Pair<View,String>(create_new_tv,"Create_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });
        term_condition_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,TermAndCondition.class));
            }
        });
        forgot_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgetMobile.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(login_btm,"btn_trans");
                pairs[1] = new Pair<View,String>(logo,"logo_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });
        login_btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginmethod();
            }
        });
    }

    public void Loginmethod(){
        if (TextUtils.isEmpty(mobile_et.getText().toString().trim())){
            mobile_et.setError(getString(R.string.mobile_required));
            mobile_et.requestFocus();
        }else if (TextUtils.isEmpty(password_et.getText().toString())){
            password_et.setError(getString(R.string.password_required));
            password_et.requestFocus();
        }else if (!checkbox.isChecked()){
            Toast.makeText(Login.this,"Please accept terms and condition",Toast.LENGTH_SHORT).show();
        }else {
            try {
                String android_id= Settings.Secure.getString(Login.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                JSONObject dataObj = new JSONObject();
                dataObj.put("phone_no",mobile_et.getText().toString().trim());
                dataObj.put("password",password_et.getText().toString().trim());
                dataObj.put("device_type","android");
                dataObj.put("device_token",android_id);
                dataObj.put("device_id",android_id);

                JSONObject requiredObj = new JSONObject();
                requiredObj.put("method","login");
                requiredObj.put("data",dataObj);

                HashMap objectNew = new HashMap();
                objectNew.put("request",requiredObj.toString());
                Log.d("login_data",objectNew.toString());
                progress_horizontal.setVisibility(View.VISIBLE);
                new WebTask(Login.this, WebUrls.BASE_URL,objectNew,Login.this, RequestCode.CODE_Login,5);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("login_res",response);
        if (RequestCode.CODE_Login == taskcode){
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject dataObj = jsonObject.optJSONObject("data");
                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(Login.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    SharedPrefManager.setUserID(Constrants.UserId,dataObj.optString("user_id"));
                    SharedPrefManager.setUserMobile(Constrants.UserMobile,dataObj.optString("phone"));
                    SharedPrefManager.setUserEmail(Constrants.UserEmail,dataObj.optString("email"));
                    SharedPrefManager.setUserName(Constrants.UserName,dataObj.optString("name"));
                    SharedPrefManager.getInstance(Login.this).storeIsLoggedIn(true);
                    progress_horizontal.setVisibility(View.GONE);
                    Intent intent = new Intent(Login.this,YourArePreparing.class);
//                    Pair[] pairs = new Pair[3];
//                    pairs[0] = new Pair<View,String>(login_btm,"btn_trans");
//                    pairs[1] = new Pair<View,String>(checkbox,"Check_trans");
//                    pairs[2] = new Pair<View,String>(term_condition_tv,"terma_trans");
//
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    progress_horizontal.setVisibility(View.GONE);
                    Toast.makeText(Login.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
