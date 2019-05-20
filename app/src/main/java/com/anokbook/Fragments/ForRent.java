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
import com.anokbook.Adapters.RentRequestBookListAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.RentRequestModel;
import com.anokbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForRent extends Fragment implements WebCompleteTask {

    RentRequestBookListAdapter rentRequestBookListAdapter;
    private ArrayList<RentRequestModel> rentRequestModelArrayList = new ArrayList<>();
    @BindView(R.id.recycler_view_sent_req) RecyclerView recycler_view_sent_req;
    @BindView(R.id.empty_view) TextView empty_view;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipe_refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for_rent, container, false);

        ButterKnife.bind(this,view);
        recycler_view_sent_req.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        Sent_Request_List_Method();

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Sent_Request_List_Method();
                swipe_refresh.setRefreshing(false);
            }
        });

        return view;
    }
    public void Sent_Request_List_Method(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","api_bookrequestsent_byuserlist");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("sent_request_list_data",objectNew.toString());
            new WebTask(getActivity(), WebUrls.BASE_URL,objectNew,ForRent.this, RequestCode.CODE_Sent_Request_List,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Sent_Request_List == taskcode){
            Log.d("api_favorite_list_res",response);
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");

                    if (dataarray!=null && dataarray.length() >0){
                        empty_view.setVisibility(View.GONE);
                        recycler_view_sent_req.setVisibility(View.VISIBLE);
                        rentRequestModelArrayList.clear();
                        for (int i =0;i<dataarray.length();i++){
                            JSONObject dataobj = dataarray.getJSONObject(i);
                            RentRequestModel rentRequestModel = new RentRequestModel();
                            rentRequestModel.setId(dataobj.optString("id"));
                            rentRequestModel.setUser_id(dataobj.optString("user_id"));
                            rentRequestModel.setTo_id(dataobj.optString("to_id"));
                            rentRequestModel.setBook_name(dataobj.optString("book_name"));
                            rentRequestModel.setRent(dataobj.optString("rent"));
                            rentRequestModel.setStatus(dataobj.optString("status"));
                            rentRequestModel.setRent_days(dataobj.optString("duration"));
                            rentRequestModel.setOwner_name(dataobj.optString("Owner_Name"));


                            String date = dataobj.optString("created_at");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date newdate = simpleDateFormat.parse(date);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                            rentRequestModel.setRequest_date(dateFormat.format(newdate)+"");

//                            homeBookListModel.setUpdated_at(dataobj.optString("updated_at"));

                            JSONArray attachmentArray = dataobj.optJSONArray("attachment");
                            if (attachmentArray!=null){
                                rentRequestModel.setBook_image(attachmentArray.getJSONObject(0).optString("name"));
//                                for (int j = 0 ;j<attachmentArray.length();j++){
//                                    JSONObject attObj = attachmentArray.getJSONObject(j);
//                                    sentRequestModel.setBook_image(attachmentArray.getJSONObject(0).optString("name"));
//                                    homeBookListModel.setAttachment_id(attObj.optString("id"));
//                                    homeBookListModel.setAttachment_type(attObj.optString("type"));
//                                    homeBookListModel.setAttachment_name(attObj.optString("name"));
//                                }
                            }
                            rentRequestModelArrayList.add(rentRequestModel);
                        }
                        rentRequestBookListAdapter = new RentRequestBookListAdapter(rentRequestModelArrayList,getActivity());
                        recycler_view_sent_req.setAdapter(rentRequestBookListAdapter);
                        rentRequestBookListAdapter.notifyDataSetChanged();
                    }else {
                        empty_view.setVisibility(View.VISIBLE);
                        recycler_view_sent_req.setVisibility(View.GONE);
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
