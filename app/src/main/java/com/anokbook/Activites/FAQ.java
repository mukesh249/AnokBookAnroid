package com.anokbook.Activites;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Adapters.FaqAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Models.FaqModel;
import com.anokbook.R;

public class FAQ extends AppCompatActivity implements WebCompleteTask {



    FaqAdapter faqAdapter;
    ArrayList<FaqModel> faqModelArrayList = new ArrayList<>();
    @BindView(R.id.faq_recyclerview) RecyclerView faq_recyclerview;
    @BindView(R.id.profile_edit_toobar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ButterKnife.bind(this,this);

        setSupportActionBar(toolbar);
        setTitle("FAQ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        faq_recyclerview.setLayoutManager(new LinearLayoutManager(FAQ.this,LinearLayoutManager.VERTICAL,false));
        FaqMethod();
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

    public void FaqMethod(){
        try {
            JSONObject dataObj = new JSONObject();

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","faq_list");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("faq_list_data",objectNew.toString());
            new WebTask(FAQ.this, WebUrls.BASE_URL,objectNew,FAQ.this, RequestCode.CODE_Faq_List_Api,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Faq_List_Api == taskcode) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("Faq_response", response);
                if (jsonObject.optString("status").compareTo("success")==0) {
                    faqModelArrayList.clear();
                    JSONArray array = jsonObject.optJSONArray("data");
                    if (array!=null){
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            FaqModel item = new FaqModel();
                            item.setFaq_id(o.optString("id"));
                            item.setFaq_ques(o.optString("question"));
                            item.setFaq_ans(o.optString("answer"));
                            faqModelArrayList.add(item);
                        }
                        faqAdapter = new FaqAdapter(faqModelArrayList, FAQ.this);
                        faq_recyclerview.setAdapter(faqAdapter);
                    }
                }else {
                    Toast.makeText(FAQ.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
