package com.anokbook.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.anokbook.Adapters.BuyRequestBookListAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.SentRequestModel;
import com.anokbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForBuy extends Fragment implements WebCompleteTask {
    BuyRequestBookListAdapter buyRequestBookListAdapter;
    private ArrayList<SentRequestModel> sentRequestModelArrayList = new ArrayList<>();
    @BindView(R.id.recycler_view_sent_req_buy) RecyclerView recycler_view_sent_req_buy;
    @BindView(R.id.empty_view) TextView empty_view;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipe_refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for_buy, container, false);
        ButterKnife.bind(this,view);
        empty_view.setVisibility(View.GONE);
        recycler_view_sent_req_buy.setVisibility(View.VISIBLE);

        recycler_view_sent_req_buy.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        Sent_Request_List_Method();
        return view;
    }
    public void Sent_Request_List_Method(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","api_buybookrequestsent_byuserlist");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("request_buy_list_data",objectNew.toString());
            new WebTask(getActivity(), WebUrls.BASE_URL,objectNew,ForBuy.this, RequestCode.CODE_Sent_Request_List,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Sent_Request_List == taskcode){
            Log.d("request_buy_list_res",response);
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");

                    if (dataarray!=null && dataarray.length() >0){
                        sentRequestModelArrayList.clear();
                        empty_view.setVisibility(View.GONE);
                        recycler_view_sent_req_buy.setVisibility(View.VISIBLE);
                        for (int i =0;i<dataarray.length();i++){
                            JSONObject dataobj = dataarray.getJSONObject(i);
                            SentRequestModel sentRequestModel = new SentRequestModel();
                            sentRequestModel.setId(dataobj.optString("id"));
                            sentRequestModel.setUser_id(dataobj.optString("user_id"));
                            sentRequestModel.setTo_id(dataobj.optString("to_id"));
                            sentRequestModel.setBook_name(dataobj.optString("book_name"));
                            sentRequestModel.setBook_author(dataobj.optString("author_name"));
                            sentRequestModel.setOwner_name(dataobj.optString("Owner_Name"));


                            String date = dataobj.optString("created_at");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date newdate = simpleDateFormat.parse(date);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                            sentRequestModel.setRequest_date(dateFormat.format(newdate)+"");

//                            homeBookListModel.setUpdated_at(dataobj.optString("updated_at"));

                            JSONArray attachmentArray = dataobj.optJSONArray("attachment");
                            if (attachmentArray!=null){
                                sentRequestModel.setBook_image(attachmentArray.getJSONObject(0).optString("name"));
                            }
                            sentRequestModelArrayList.add(sentRequestModel);
                        }
                        buyRequestBookListAdapter = new BuyRequestBookListAdapter(sentRequestModelArrayList,getActivity());
                        recycler_view_sent_req_buy.setAdapter(buyRequestBookListAdapter);
                        buyRequestBookListAdapter.notifyDataSetChanged();
                    }else {
                        empty_view.setVisibility(View.VISIBLE);
                        recycler_view_sent_req_buy.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(getActivity(),jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
