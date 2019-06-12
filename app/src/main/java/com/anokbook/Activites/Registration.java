package com.anokbook.Activites;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Registration extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.reg_name_et)
    EditText reg_name_et;
    @BindView(R.id.reg_mobile_et)
    EditText reg_mobile_et;
    @BindView(R.id.reg_email_et)
    EditText reg_email_et;
    @BindView(R.id.reg_password_et)
    EditText reg_password_et;
    @BindView(R.id.reg_con_password_et)
    EditText reg_con_passwrod_et;
    @BindView(R.id.already_tv)
    TextView already_tv;
    @BindView(R.id.reg_btn)
    Button reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this, this);

        already_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Registration.this,Login.class));
                finish();
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationMethod();
            }
        });
    }

    private void RegistrationMethod() {
        if (TextUtils.isEmpty(reg_name_et.getText().toString().trim())) {
            reg_name_et.setError(getString(R.string.name_required));
            reg_name_et.requestFocus();
        } else if (TextUtils.isEmpty(reg_mobile_et.getText().toString().toString())) {
            reg_mobile_et.setError(getString(R.string.mobile_required));
            reg_mobile_et.requestFocus();
        } else if (reg_mobile_et.getText().toString().length() < 10) {
            reg_mobile_et.setError(getString(R.string.valid_mobile));
            reg_mobile_et.requestFocus();
        } else if (TextUtils.isEmpty(reg_email_et.getText().toString().trim())) {
            reg_email_et.setError(getString(R.string.email_address_required));
            reg_email_et.requestFocus();
        } else if (TextUtils.isEmpty(reg_password_et.getText().toString().trim())) {
            reg_password_et.setError(getString(R.string.password_required));
            reg_password_et.requestFocus();
        } else if (reg_password_et.getText().toString().length() < 8) {
            reg_password_et.setError(getString(R.string.password_too_short));
            reg_password_et.requestFocus();
        } else if (!SharedPrefManager.getInstance(Registration.this).CheckPassword(reg_password_et.getText().toString())) {
            reg_password_et.setError(getString(R.string.pass_must_6));
            reg_password_et.requestFocus();
        } else if (TextUtils.isEmpty(reg_con_passwrod_et.getText().toString().toString())) {
            reg_con_passwrod_et.setError(getString(R.string.confirm_password_required));
            reg_con_passwrod_et.requestFocus();
        } else if (reg_password_et.getText().toString().trim().compareTo(reg_con_passwrod_et.getText().toString().trim()) != 0) {
            reg_con_passwrod_et.setError(getString(R.string.pass_and_con_pass_not_match));
            reg_con_passwrod_et.requestFocus();
        } else if (!SharedPrefManager.isValidEmail(reg_email_et.getText().toString().trim())) {
            reg_email_et.setError(getString(R.string.email_not_valid));
            reg_email_et.requestFocus();
        } else {
            try {
                JSONObject request_Obj = new JSONObject();
                JSONObject objectNew = new JSONObject();
                objectNew.put("name", reg_name_et.getText().toString().trim());
                objectNew.put("email", reg_email_et.getText().toString().trim());
                objectNew.put("password", reg_password_et.getText().toString().trim());
                objectNew.put("phone", reg_mobile_et.getText().toString().trim());

                request_Obj.put("method", "register");
                request_Obj.put("data", objectNew);

                HashMap reqNew = new HashMap();
                reqNew.put("request", request_Obj.toString());
                Log.d("registration_res:", reqNew.toString());
                new WebTask(Registration.this, WebUrls.BASE_URL, reqNew, Registration.this, RequestCode.CODE_Register, 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_Register == taskcode) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject dataObj = jsonObject.optJSONObject("data");
                if (jsonObject.optString("status").compareTo("success") == 0) {
                    SharedPrefManager.setUserID(Constrants.UserId, dataObj.optString("user_id"));
                    SharedPrefManager.setUserMobile(Constrants.UserMobile, dataObj.optString("phone"));
                    SharedPrefManager.setUserEmail(Constrants.UserEmail, dataObj.optString("email"));
                    SharedPrefManager.setUserName(Constrants.UserName, dataObj.optString("name"));
                    startActivity(new Intent(Registration.this, OptVerification.class));
                    finish();
                } else {
                    Toast.makeText(Registration.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
