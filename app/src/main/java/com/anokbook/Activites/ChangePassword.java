package com.anokbook.Activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePassword extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.current_password_et)
    EditText current_password_et;
    @BindView(R.id.new_password_et)
    EditText new_password_et;
    @BindView(R.id.confirm_new_password_et)
    EditText confirm_new_password_et;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.profile_edit_toobar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this, this);

        setSupportActionBar(toolbar);
        setTitle("Change Password");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassswordMethod();
            }
        });
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

    public void ChangePassswordMethod() {

        if (TextUtils.isEmpty(current_password_et.getText().toString().trim())) {
            current_password_et.setError(getString(R.string.enter_current_password));
            current_password_et.requestFocus();
        } else if (TextUtils.isEmpty(new_password_et.getText().toString().trim())) {
            new_password_et.setError(getString(R.string.enter_new_password));
            new_password_et.requestFocus();
        } else if (new_password_et.getText().toString().length() < 6) {
            new_password_et.setError(getString(R.string.password_too_short));
            new_password_et.requestFocus();
        } else if (new_password_et.getText().toString().trim().compareTo(confirm_new_password_et.getText().toString().trim()) != 0) {
            confirm_new_password_et.setError(getString(R.string.pass_and_con_pass_not_match));
            confirm_new_password_et.requestFocus();
        } else {
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                dataObj.put("password", current_password_et.getText().toString().trim());
                dataObj.put("new_password", new_password_et.getText().toString().trim());

                JSONObject resqObj = new JSONObject();
                resqObj.put("method", "api_change_password");
                resqObj.put("data", dataObj);

                HashMap objectNew = new HashMap();
                objectNew.put("request", resqObj.toString());
                Log.d("change_password_data", objectNew.toString());
                new WebTask(ChangePassword.this, WebUrls.BASE_URL, objectNew, ChangePassword.this, RequestCode.CODE_ChangePassword, 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("change_password_res", response);
        if (RequestCode.CODE_ChangePassword == taskcode) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null && jsonObject.optString("status").compareTo("success") == 0) {
                    Toast.makeText(ChangePassword.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePassword.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
