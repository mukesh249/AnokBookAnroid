package com.anokbook.Activites;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Adapters.ReceivedRequestBookListAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.ReveivedRequestModel;
import com.anokbook.R;

public class ReceivedRequest extends AppCompatActivity implements WebCompleteTask {
    ReceivedRequestBookListAdapter receivedRequestBookListAdapter;
    private ArrayList<ReveivedRequestModel> receivedRequestBookArrayList = new ArrayList<>();
    @BindView(R.id.recycler_view_received_req) RecyclerView recycler_view_received_req;
    @BindView(R.id.empty_view) TextView empty_view;
    @BindView(R.id.profile_edit_toobar) Toolbar toolbar;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipe_refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_request);

        ButterKnife.bind(this,this);
        recycler_view_received_req.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Received_Request_List_Method();
        setSupportActionBar(toolbar);
        setTitle("Received");
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Received_Request_List_Method();
                swipe_refresh.setRefreshing(false);
            }
        });

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

    public void Received_Request_List_Method(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","api_bookreceived_byuserlist");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("rece_request_list_data",objectNew.toString());
            new WebTask(ReceivedRequest.this, WebUrls.BASE_URL,objectNew, ReceivedRequest.this, RequestCode.CODE_Recieved_Request_List,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Recieved_Request_List == taskcode){
            Log.d("rece_request_list_res",response);
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");

                    if (dataarray!=null && dataarray.length() >0){
                        empty_view.setVisibility(View.GONE);
                        recycler_view_received_req.setVisibility(View.VISIBLE);
                        receivedRequestBookArrayList.clear();
                        for (int i =0;i<dataarray.length();i++){
                            JSONObject dataobj = dataarray.getJSONObject(i);
                            ReveivedRequestModel reveivedRequestModel = new ReveivedRequestModel();
                            reveivedRequestModel.setId(dataobj.optString("id"));
                            reveivedRequestModel.setUser_id(dataobj.optString("user_id"));
                            reveivedRequestModel.setTo_id(dataobj.optString("to_id"));
                            reveivedRequestModel.setStatus(dataobj.optString("status"));
                            reveivedRequestModel.setDuration(dataobj.optString("duration"));
                            reveivedRequestModel.setRent(dataobj.optString("rent"));
                            reveivedRequestModel.setBook_name(dataobj.optString("book_name"));
//                            reveivedRequestModel.setAuthor(dataobj.optString("author_name"));
                            reveivedRequestModel.setRequester_name(dataobj.optString("Requester_Name"));


                            String date = dataobj.optString("created_at");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date newdate = simpleDateFormat.parse(date);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                            reveivedRequestModel.setCreated_date(dateFormat.format(newdate)+"");

//                            homeBookListModel.setUpdated_at(dataobj.optString("updated_at"));

                            JSONArray attachmentArray = dataobj.optJSONArray("attachment");
                            if (attachmentArray!=null){
                                reveivedRequestModel.setBook_image(attachmentArray.getJSONObject(0).optString("name"));
//                                for (int j = 0 ;j<attachmentArray.length();j++){
//                                    JSONObject attObj = attachmentArray.getJSONObject(j);
//                                    sentRequestModel.setBook_image(attachmentArray.getJSONObject(0).optString("name"));
//                                    homeBookListModel.setAttachment_id(attObj.optString("id"));
//                                    homeBookListModel.setAttachment_type(attObj.optString("type"));
//                                    homeBookListModel.setAttachment_name(attObj.optString("name"));
//                                }
                            }
                            receivedRequestBookArrayList.add(reveivedRequestModel);
                        }
                        receivedRequestBookListAdapter = new ReceivedRequestBookListAdapter(receivedRequestBookArrayList,ReceivedRequest.this);
                        recycler_view_received_req.setAdapter(receivedRequestBookListAdapter);
                        receivedRequestBookListAdapter.notifyDataSetChanged();
                    }else {
                        empty_view.setVisibility(View.VISIBLE);
                        recycler_view_received_req.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
