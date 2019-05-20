package com.anokbook.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Adapters.ShowingCategoriesAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.ShowingCategoriesModel;
import com.anokbook.R;

public class YourArePreparing extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.you_are_perparing_recyclerview)
    RecyclerView you_are_perparing_recyclerview;
    @BindView(R.id.btn_nxt) Button btn_nxt;
    @BindView(R.id.btn_skip) Button btn_skip;

    ShowingCategoriesAdapter showingCategoriesAdapter;
    ArrayList<ShowingCategoriesModel> showingCategoriesModelArrayList = new ArrayList<>();
//    ArrayList<String> cat_
//    String[] subjects = {
//            "All",
//            "Arts",
//            "Architecture",
//            "Agriculture",
//            "Merchant Navy",
//            "Commerce",
//            "Science",
//            "Programming",
//            "Maths",
//            "Hindi",
//            "English"
//    };
    public static boolean you_are_preparing=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_are_preparing);

        ButterKnife.bind(this,this);
        you_are_preparing = true;
        you_are_perparing_recyclerview.setLayoutManager(new GridLayoutManager(this,3));

        GetCategoriesList();
        ShowingCategoriesAdapter.arrayList_name.clear();
        ShowingCategoriesAdapter.arrayList_id.clear();
        btn_skip.setVisibility(View.VISIBLE);
        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("Activity","").compareTo("setting")==0){
            ShowingCategoriesAdapter.arrayList_name.clear();
            btn_nxt.setText(getString(R.string.submit));
            btn_skip.setVisibility(View.GONE);
        }else {
            btn_nxt.setText(getString(R.string.next));
        }

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_nxt.getText().toString().compareTo(getString(R.string.submit))==0){
                    you_are_preparing = false;
                    UserSelectCategories();
                   // Toast.makeText(YourArePreparing.this,ShowingCategoriesAdapter.arrayList_id.toString(),Toast.LENGTH_SHORT).show();
//                    finish();
                }else {
                    you_are_preparing = false;
                    UserSelectCategories();
                }
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                you_are_preparing = false;
                Intent intent = new Intent(YourArePreparing.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }

    public void GetCategoriesList(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","api_user_preparing_cat_list");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("preparing_cat_list_data",objectNew.toString());
            new WebTask(YourArePreparing.this, WebUrls.BASE_URL,objectNew,YourArePreparing.this, RequestCode.CODE_Preparing_Cat_List,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void UserSelectCategories(){
        if (ShowingCategoriesAdapter.arrayList_id!=null&&0<ShowingCategoriesAdapter.arrayList_id.size()) {
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                String cat_id_arr="";
                for (int i =0;i<ShowingCategoriesAdapter.arrayList_id.size();i++){
                    cat_id_arr+=ShowingCategoriesAdapter.arrayList_id.get(i)+",";
//                    System.out.println( cat_id_arr[i]);
                }
                System.out.println(cat_id_arr.substring(0,cat_id_arr.length()-1));
                dataObj.put("cat_id",cat_id_arr.substring(0,cat_id_arr.length()-1));

                JSONObject resqObj = new JSONObject();
                resqObj.put("method", "api_user_preparing_cat");
                resqObj.put("data", dataObj);

                HashMap objectNew = new HashMap();
                objectNew.put("request", resqObj.toString());
                Log.d("preparing_cat_data", objectNew.toString());
//                Toast.makeText(YourArePreparing.this,cat_id_arr.toString(),Toast.LENGTH_SHORT).show();
                new WebTask(YourArePreparing.this, WebUrls.BASE_URL,objectNew,YourArePreparing.this, RequestCode.CODE_User_Preparing_Cat,1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(YourArePreparing.this,"Please select categories or skip",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Preparing_Cat_List==taskcode){
            Log.d("preparing_cat_list_res",response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){

                    JSONArray dataObj = jsonObject.optJSONArray("data");
                    if (dataObj!=null){
                        for (int i=0;i<dataObj.length();i++){
                            JSONObject object = dataObj.optJSONObject(i);
//                            cat_id_array.add(object.optString("cat_id"));
//                            cat_name_array.add(object.optString("cat_name"));
                            ShowingCategoriesModel showingCategoriesModel = new ShowingCategoriesModel();
                            showingCategoriesModel.setName(object.optString("cat_name"));
                            showingCategoriesModel.setId(object.optString("id"));
                            showingCategoriesModel.setCheck(object.optString("IsActive"));
                            showingCategoriesModelArrayList.add(showingCategoriesModel);
                        }
                        showingCategoriesAdapter = new ShowingCategoriesAdapter(showingCategoriesModelArrayList,YourArePreparing.this);
                        you_are_perparing_recyclerview.setAdapter(showingCategoriesAdapter);
                        showingCategoriesAdapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(YourArePreparing.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (RequestCode.CODE_User_Preparing_Cat == taskcode){
            Log.d("preparing_cat_res",response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(YourArePreparing.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    if (btn_nxt.getText().toString().compareTo(getString(R.string.submit))==0){
                        finish();
                    }else {
                        Intent intent = new Intent(YourArePreparing.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Toast.makeText(YourArePreparing.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
