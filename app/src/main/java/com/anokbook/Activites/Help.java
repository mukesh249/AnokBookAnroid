package com.anokbook.Activites;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Help extends AppCompatActivity implements WebCompleteTask {


    @BindView(R.id.help_name_et) EditText help_name_et;
    @BindView(R.id.help_mobile_et) EditText help_mobile_et;
    @BindView(R.id.help_email_et) EditText help_email_et;
    @BindView(R.id.help_message_et) EditText help_message_et;
    @BindView(R.id.help_submit) Button help_submit;
    @BindView(R.id.profile_edit_toobar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ButterKnife.bind(this,this);

        help_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpMethod();
            }
        });

        setSupportActionBar(toolbar);
        setTitle("Help");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    private void HelpMethod() {
        if (TextUtils.isEmpty(help_name_et.getText().toString().trim())){
            help_name_et.setError(getString(R.string.enter_name));
            help_name_et.requestFocus();
        }else if (TextUtils.isEmpty(help_mobile_et.getText().toString().trim())){
            help_mobile_et.setError(getString(R.string.enter_contact_number));
            help_mobile_et.requestFocus();
        }else if (TextUtils.isEmpty(help_email_et.getText().toString().trim())){
            help_email_et.setError(getString(R.string.email_address_required));
            help_email_et.requestFocus();
        }else if (!SharedPrefManager.isValidEmail(help_email_et.getText().toString().trim())){
            help_email_et.setError(getString(R.string.email_not_valid));
            help_email_et.requestFocus();
        }else if (TextUtils.isEmpty(help_message_et.getText().toString().trim())){
            help_message_et.setError(getString(R.string.enter_message));
            help_message_et.requestFocus();
        }else {
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                dataObj.put("name", help_name_et.getText().toString().trim());
                dataObj.put("email", help_email_et.getText().toString().trim());
                dataObj.put("contact_no", help_mobile_et.getText().toString().trim());
                dataObj.put("message", help_message_et.getText().toString().trim());

                JSONObject resqObj = new JSONObject();
                resqObj.put("method", "api_contact_form");
                resqObj.put("data", dataObj);

                HashMap objectNew = new HashMap();
                objectNew.put("request", resqObj.toString());
                Log.d("api_contact_form_data", objectNew.toString());
                new WebTask(Help.this, WebUrls.BASE_URL, objectNew, Help.this, RequestCode.CODE_Privacypolicy, 1);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Privacypolicy == taskcode){
            Log.d("api_contact_form_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(Help.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(Help.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
