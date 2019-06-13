package com.anokbook.Activites;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermAndCondition extends AppCompatActivity implements WebCompleteTask {
    @BindView(R.id.privacy_policy_title_tv)
    TextView privacy_policy_title;
    @BindView(R.id.privacy_policy_content_tv)
    TextView privacy_policy_content;
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
        setContentView(R.layout.activity_term_and_condition);


        ButterKnife.bind(this, this);

        setSupportActionBar(toolbar);
        setTitle("Term And Conditions");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        PrivacyPolicyMethod();
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

    private void PrivacyPolicyMethod() {
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("cms_id", "4");

            JSONObject resqObj = new JSONObject();
            resqObj.put("method", "cms_list");
            resqObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", resqObj.toString());
            Log.d("Privacy_list_data", objectNew.toString());
            new WebTask(TermAndCondition.this, WebUrls.BASE_URL, objectNew, TermAndCondition.this, RequestCode.CODE_Privacypolicy, 1);
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Privacypolicy == taskcode) {
            Log.d("Privacy_list_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success") == 0) {
                    privacy_policy_title.setText(jsonObject.optJSONObject("data").optString("title"));
                    privacy_policy_content.setText(jsonObject.optJSONObject("data").optString("description"));
                } else {
                    Toast.makeText(TermAndCondition.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
