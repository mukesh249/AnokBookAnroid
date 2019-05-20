package com.anokbook.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.anokbook.Adapters.RentedBookListAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.UserBookListModel;
import com.anokbook.R;

public class Rented extends Fragment implements WebCompleteTask {

    @BindView(R.id.book_recyclerview)
    RecyclerView book_recyclerview;
    RentedBookListAdapter rentedBookListAdapter;
    ArrayList<UserBookListModel> userBookListModelArrayList = new ArrayList<>();
    public static Boolean gridview,listview;
    @BindView(R.id.list_view_img) ImageView list_view_img;
    @BindView(R.id.grid_view_img) ImageView grid_view_img;
    @BindView(R.id.empty_view) TextView empty_view;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipe_refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rented, container, false);

        ButterKnife.bind(this,view);
        book_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rentedBookListAdapter = new RentedBookListAdapter(userBookListModelArrayList, getActivity());
        book_recyclerview.setAdapter(rentedBookListAdapter);
        listview = true;
        gridview = false;
        list_view_img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        grid_view_img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.light_grey), PorterDuff.Mode.SRC_IN);

        list_view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview = true;
                gridview = false;
                list_view_img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                grid_view_img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.light_grey), PorterDuff.Mode.SRC_IN);
                book_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rentedBookListAdapter = new RentedBookListAdapter(userBookListModelArrayList, getActivity());
                book_recyclerview.setAdapter(rentedBookListAdapter);
            }
        });
        grid_view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview = false;
                gridview = true;
                list_view_img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.light_grey), PorterDuff.Mode.SRC_IN);
                grid_view_img.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                book_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                rentedBookListAdapter = new RentedBookListAdapter(userBookListModelArrayList, getActivity());
                book_recyclerview.setAdapter(rentedBookListAdapter);
            }
        });

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                All_Book_List_User_Method();
                swipe_refresh.setRefreshing(false);
            }
        });
        All_Book_List_User_Method();
        return view;

    }
    public void All_Book_List_User_Method(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","book_listby_user");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("book_listby_user_data",objectNew.toString());
            new WebTask(getActivity(), WebUrls.BASE_URL,objectNew,Rented.this, RequestCode.CODE_Book_ListBy_User,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Book_ListBy_User == taskcode){
            Log.d("book_listby_user_res",response);
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");

                    if (dataarray!=null && dataarray.length()>0){
                        userBookListModelArrayList.clear();
                        empty_view.setVisibility(View.GONE);
                        book_recyclerview.setVisibility(View.VISIBLE);
                        for (int i =0;i<dataarray.length();i++){
                            JSONObject dataobj = dataarray.getJSONObject(i);
                            UserBookListModel homeBookListModel = new UserBookListModel();
                            homeBookListModel.setId(dataobj.optString("id"));
                            homeBookListModel.setUser_id(dataobj.optString("user_id"));
                            homeBookListModel.setCat_id(dataobj.optString("cat_id"));
                            homeBookListModel.setBook_name(dataobj.optString("book_name"));
                            homeBookListModel.setAuthor_name(dataobj.optString("author_name"));
                            homeBookListModel.setDescription(dataobj.optString("description"));
                            homeBookListModel.setPage_no(dataobj.optString("page_no"));
                            homeBookListModel.setMrp(dataobj.optString("mrp"));
                            homeBookListModel.setName(dataobj.optString("name"));
                            homeBookListModel.setAddress(dataobj.optString("address"));
                            homeBookListModel.setLocation(dataobj.optString("location"));
                            homeBookListModel.setLandmarks(dataobj.optString("landmarks"));
                            homeBookListModel.setPublication(dataobj.optString("publication"));
                            homeBookListModel.setContact_no(dataobj.optString("contact_no"));
                            homeBookListModel.setAlternate_no(dataobj.optString("alternate_no"));
                            homeBookListModel.setPupose(dataobj.optString("pupose"));


                            String date = dataobj.optString("created_at");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date newdate = simpleDateFormat.parse(date);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                            homeBookListModel.setCreated_at(dateFormat.format(newdate)+"");

                            homeBookListModel.setUpdated_at(dataobj.optString("updated_at"));

                            JSONArray attachmentArray = dataobj.optJSONArray("attachment");
                            if (attachmentArray!=null){
                                for (int j = 0 ;j<attachmentArray.length();j++){
                                    JSONObject attObj = attachmentArray.getJSONObject(j);
                                    homeBookListModel.setBook_imag(attachmentArray.getJSONObject(0).optString("name"));
                                    homeBookListModel.setAttachment_id(attObj.optString("id"));
                                    homeBookListModel.setAttachment_type(attObj.optString("type"));
                                    homeBookListModel.setAttachment_name(attObj.optString("name"));
                                }
                            }
                            userBookListModelArrayList.add(homeBookListModel);
                        }
                        rentedBookListAdapter.notifyDataSetChanged();
                    }else {
                        empty_view.setVisibility(View.VISIBLE);
                        book_recyclerview.setVisibility(View.GONE);
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
