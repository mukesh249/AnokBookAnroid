package com.anokbook.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import de.hdodenhof.circleimageview.CircleImageView;
import com.anokbook.Adapters.UserPostedBookListAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Models.UserBookListModel;
import com.anokbook.R;

public class SellerProfile extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.user_toolbar) Toolbar user_toolbar;
    @BindView(R.id.user_rented_tv) TextView user_rented_tv;
    @BindView(R.id.user_posted_tv) TextView user_posted_tv;
    @BindView(R.id.user_profile_pic) CircleImageView user_profile_pic;
    @BindView(R.id.user_name) TextView user_name;
    @BindView(R.id.user_address) TextView user_address;
    @BindView(R.id.user_content) TextView user_content;
    @BindView(R.id.user_date) TextView user_date;
    @BindView(R.id.user_mobile_no) TextView user_mobile_no;
    @BindView(R.id.user_email) TextView user_email;
    @BindView(R.id.user_book_recyclerview) RecyclerView user_book_reyclerview;
    UserPostedBookListAdapter userPostedBookListAdapter;
    ArrayList<UserBookListModel> homeBookListModelArrayList = new ArrayList<>();
    public static boolean sellerprofile =false;

    String seller_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);

        ButterKnife.bind(this, this);

        setSupportActionBar(user_toolbar);
        setTitle("Profile");
        user_mobile_no.setVisibility(View.GONE);
        user_email.setVisibility(View.GONE);
        if (getIntent().getExtras()!=null){
            if (getIntent().getExtras().getString("user","").compareTo("login")==0){
                user_mobile_no.setVisibility(View.VISIBLE);
                user_email.setVisibility(View.VISIBLE);
            }else {
                user_mobile_no.setVisibility(View.GONE);
                user_email.setVisibility(View.GONE);
            }
        }
        user_posted_tv.setTextColor(getResources().getColor(R.color.white));
        user_posted_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        user_rented_tv.setTextColor(getResources().getColor(R.color.dark_gray));
        user_rented_tv.setBackgroundColor(getResources().getColor(R.color.white));

        sellerprofile = true;
        user_book_reyclerview.setLayoutManager(new LinearLayoutManager(SellerProfile.this, LinearLayout.HORIZONTAL, false));
        userPostedBookListAdapter = new UserPostedBookListAdapter(homeBookListModelArrayList, SellerProfile.this);
        user_book_reyclerview.setAdapter(userPostedBookListAdapter);

        user_posted_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_posted_tv.setTextColor(getResources().getColor(R.color.white));
                user_posted_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                user_rented_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                user_rented_tv.setBackgroundColor(getResources().getColor(R.color.white));
                user_book_reyclerview.setLayoutManager(new LinearLayoutManager(SellerProfile.this, LinearLayout.HORIZONTAL, false));
                userPostedBookListAdapter = new UserPostedBookListAdapter(homeBookListModelArrayList, SellerProfile.this);
                user_book_reyclerview.setAdapter(userPostedBookListAdapter);
            }
        });
        user_rented_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_posted_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                user_posted_tv.setBackgroundColor(getResources().getColor(R.color.white));
                user_rented_tv.setTextColor(getResources().getColor(R.color.white));
                user_rented_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                user_book_reyclerview.setLayoutManager(new LinearLayoutManager(SellerProfile.this, LinearLayout.HORIZONTAL, false));
                userPostedBookListAdapter = new UserPostedBookListAdapter(homeBookListModelArrayList, SellerProfile.this);
                user_book_reyclerview.setAdapter(userPostedBookListAdapter);
            }
        });
        seller_id = getIntent().getExtras().getString("seller_id","");
        Log.d("seller_id",seller_id);
        UserProfileMethod();
        All_Book_List_User_Method();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional d
    }
    public void
    UserProfileMethod(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", seller_id);

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","user_profile");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("user_profile_data",objectNew.toString());
            new WebTask(SellerProfile.this, WebUrls.BASE_URL,objectNew,SellerProfile.this, RequestCode.CODE_UserProfile,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void All_Book_List_User_Method(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", seller_id);

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","book_listby_user");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("book_listby_user_data",objectNew.toString());
            new WebTask(SellerProfile.this, WebUrls.BASE_URL,objectNew,SellerProfile.this, RequestCode.CODE_Book_ListBy_User,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_UserProfile == taskcode){
            Log.d("user_profile_res",response);
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");
                    JSONObject dataobj = dataarray.getJSONObject(0);
                    if (dataobj!=null){
                        user_name.setText(dataobj.optString("name"));
                        String date = dataobj.optString("created_at");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date newdate = simpleDateFormat.parse(date);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
                        user_address.setText(dataobj.optString("street")+", "+dataobj.optString("landmarks"));
                        user_date.setText("Since "+dateFormat.format(newdate));
                        user_mobile_no.setText("+91-"+dataobj.optString("phone"));
                        user_email.setText(dataobj.optString("email"));
                        Picasso.with(this).load(WebUrls.IMG_BASE_URL+WebUrls.Image_Url+dataobj.optString("profile_img"))
                                .fit()
                                .placeholder(R.drawable.avatar).into(user_profile_pic);
                    }

                }else {
                    Toast.makeText(SellerProfile.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (RequestCode.CODE_Book_ListBy_User == taskcode){
            Log.d("book_listby_user_res",response);
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");

                    if (dataarray!=null){
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
                            homeBookListModelArrayList.add(homeBookListModel);
                        }
                        userPostedBookListAdapter.notifyDataSetChanged();
                    }

                }else {
                    Toast.makeText(SellerProfile.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
