package com.anokbook.Activites;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class NewChangePassword extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.password_et)EditText new_password_et;
    @BindView(R.id.confirm_new_password_et) EditText confirm_new_password_et;
    @BindView(R.id.submit) Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_change_password);

        ButterKnife.bind(this,this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewChangePasswordMethod();
            }
        });
    }

    public void NewChangePasswordMethod(){
        if (TextUtils.isEmpty(new_password_et.getText().toString().trim())){
            new_password_et.setError(getString(R.string.password_required));
            new_password_et.requestFocus();
        }else if (TextUtils.isEmpty(confirm_new_password_et.getText().toString())){
            confirm_new_password_et.setError(getString(R.string.confirm_password_required));
            confirm_new_password_et.requestFocus();
        }else if (new_password_et.getText().toString().length()<6){
            new_password_et.setError(getString(R.string.password_too_short));
            new_password_et.requestFocus();
        }else if (new_password_et.getText().toString().trim().compareTo(confirm_new_password_et.getText().toString().trim())!=0){
            confirm_new_password_et.setError(getString(R.string.pass_and_con_pass_not_match));
            confirm_new_password_et.requestFocus();
        }else {
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("phone",SharedPrefManager.getUserMobile(Constrants.UserMobile));
                dataObj.put("new_password",new_password_et.getText().toString().trim());
                dataObj.put("confirm_password",confirm_new_password_et.getText().toString().trim());

                JSONObject requiredObj = new JSONObject();
                requiredObj.put("method","password_reset");
                requiredObj.put("data",dataObj);

                HashMap objectNew = new HashMap();
                objectNew.put("request",requiredObj.toString());
                Log.d("password_reset_data",objectNew.toString());
                new WebTask(NewChangePassword.this, WebUrls.BASE_URL,objectNew,NewChangePassword.this, RequestCode.CODE_Password_Reset,1);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("password_reset_res",response);
        if (RequestCode.CODE_Password_Reset == taskcode){
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject dataObj = jsonObject.optJSONObject("data");
                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(NewChangePassword.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewChangePassword.this,Login.class));
                    finish();
                }else {
                    Toast.makeText(NewChangePassword.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
