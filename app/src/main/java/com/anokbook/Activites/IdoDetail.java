package com.anokbook.Activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.R;

public class IdoDetail extends AppCompatActivity implements WebCompleteTask {

    @BindView(R.id.ido_detail__toolbar)
    Toolbar ido_detail__toolbar;
    @BindView(R.id.slidera)
    ViewPager slidera;
    @BindView(R.id.dots_indicator)
    DotsIndicator dotsIndicator;

    private ImageView[] dots;

    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    @BindView(R.id.map_image_rel) RelativeLayout map_image_rel;
    @BindView(R.id.buy_bottom_tv) TextView buy_tv;
    @BindView(R.id.rent_bottom_tv) TextView rent_tv;
    @BindView(R.id.che_rel) RelativeLayout che_rel;
    @BindView(R.id.view_profile) TextView view_profile;
    @BindView(R.id.book_name_tv) TextView book_name_tv;
    @BindView(R.id.book_publication_tv) TextView book_publication_tv;
    @BindView(R.id.book_author_tv) TextView book_author_tv;
    @BindView(R.id.book_no_of_pages_tv) TextView book_no_of_pages_tv;
    @BindView(R.id.mrp_tv) TextView mrp_tv;
    @BindView(R.id.security_tv) TextView security_tv;
    @BindView(R.id.book_categories_tv) TextView book_categories_tv;
    @BindView(R.id.address_full) TextView address_full;
    @BindView(R.id.book_description_tv) TextView book_description_tv;
    @BindView(R.id.favorite_icon) SparkButton favorite_icon;
    @BindView(R.id.delete_icon_post) ImageView delete_icon_post;
    @BindView(R.id.share_tv) TextView share_tv;
    @BindView(R.id.review_tv) TextView review_tv;
    @BindView(R.id.report_tv)  TextView report_tv;
    @BindView(R.id.address_title) TextView address_title;
    @BindView(R.id.progress_horizontal) ProgressBar progress_horizontal;
    @BindView(R.id.nestedscroll) NestedScrollView nestedscroll;
    @BindView(R.id.bottom) LinearLayout bottom;
    TextView rent_estimation_price;
    String hint;
    private String book_id,user_id,recuriter_id;
    private static ArrayList<String> imageId = new ArrayList<>();
    boolean day,week,month;
    String rent_string,security_charge_string,service_tax_string,total_amount,buyPrice,serive_tax;
    int days = 1;
    private static IdoDetail mInstance;
    private AlertDialog alertDialog;
    private AlertDialog buyDialog;
    private double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ido_detail);
        ButterKnife.bind(this,this);
        mInstance = this;

        progress_horizontal.setVisibility(View.VISIBLE);
        nestedscroll.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);

        buy_tv.setVisibility(View.VISIBLE);
        rent_tv.setVisibility(View.VISIBLE);
        view_profile.setVisibility(View.VISIBLE);
        view_profile.setText(getString(R.string.view_profile));
//        imageId = new int[]{R.drawable.map_image, R.drawable.map_image, R.drawable.map_image, R.drawable.map_image};
        book_id = getIntent().getExtras().getString("book_id","");
        user_id = getIntent().getExtras().getString("user_id","");
        if (user_id.compareTo(SharedPrefManager.getUserID(Constrants.UserId))==0){
            buy_tv.setVisibility(View.GONE);
            rent_tv.setVisibility(View.GONE);
            view_profile.setText("Edit Post");
            favorite_icon.setVisibility(View.GONE);
        }else if (getIntent().getExtras().getString("Activity","").compareTo("seller_profile")==0){
            view_profile.setVisibility(View.GONE);
        }
//        Log.d("book_id_requied",book_id);

        BookDetailMethod(book_id);

////-----------------------------------------------------------Number picker-------------------------
//        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker);
//
//// Set divider color
//        numberPicker.setDividerColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        numberPicker.setDividerColorResource(R.color.colorPrimary);
//
//// Set selected text color
//        numberPicker.setSelectedTextColor(ContextCompat.getColor(this, R.color.primaryDark));
//        numberPicker.setSelectedTextColorResource(R.color.primaryDark);
//
//// Set value
//        numberPicker.setMaxValue(30);
//        numberPicker.setMinValue(1);
//        numberPicker.setValue(3);
//
//// Set fading edge enabled
//        numberPicker.setFadingEdgeEnabled(true);
//
//// Set scroller enabled
//        numberPicker.setScrollerEnabled(true);
//
//// Set wrap selector wheel
//        numberPicker.setWrapSelectorWheel(true);
//
//// OnClickListener
//        numberPicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("Tag", "Click on current value");
////                Toast.makeText(IdoDetail.this,numberPicker.getsel)
//            }
//        });
//
//// OnValueChangeListener
//        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Toast.makeText(IdoDetail.this,newVal+"",Toast.LENGTH_SHORT).show();
////                Log.d("Tag", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
//            }
//        });
////-----------------------------------------------------------Number picker-------------------------
        buy_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy_tv.setTextColor(getResources().getColor(R.color.white));
                buy_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));

                rent_tv.setTextColor(getResources().getColor(R.color.black));
                rent_tv.setBackgroundColor(getResources().getColor(R.color.white));
                BuyPopupmethod();

            }
        });

        rent_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy_tv.setTextColor(getResources().getColor(R.color.black));
                buy_tv.setBackgroundColor(getResources().getColor(R.color.white));

                rent_tv.setTextColor(getResources().getColor(R.color.white));
                rent_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                RentPopupmethod();
            }
        });

        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_profile.getText().toString().compareTo(getString(R.string.view_profile))==0){
                    startActivity(new Intent(IdoDetail.this,SellerProfile.class).putExtra("seller_id",user_id));
                }else {
                    startActivity(new Intent(IdoDetail.this,EditPost.class).putExtra("user_book_id",book_id));
                }
            }
        });
        favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite_icon.playAnimation();
                LikeMethod();
            }
        });

        share_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sharemsg = "\nLet me recommend you this application\n\n";
                    sharemsg+="https://play.google.com/store/apps/details?id=babbabazrii.com.bababazri";
                    intent.putExtra(Intent.EXTRA_TEXT,sharemsg);
                    startActivity(Intent.createChooser(intent,"Choose one"));

                }catch (Exception e){

                }
            }
        });
        map_image_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        user_id = getIntent().getExtras().getString("user_id","");
        if (user_id.compareTo(SharedPrefManager.getUserID(Constrants.UserId))==0){
            recreate();
        }
//        book_id = getIntent().getExtras().getString("book_id","");
//        user_id = getIntent().getExtras().getString("user_id","");
//        BookDetailMethod();
    }
    public static IdoDetail getInstance(){
        return mInstance;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        if (timer!=null)
        timer.cancel();
        return true;
    }
    public class CustomAdapter extends PagerAdapter {

        private Activity activity;
        private ArrayList<String> imagesArray;
        private String[] namesArray;

        public CustomAdapter(Activity activity, ArrayList<String> imagesArray) {

            this.activity = activity;
            this.imagesArray = imagesArray;
            this.namesArray = namesArray;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater inflater = ((Activity)activity).getLayoutInflater();

            View viewItem = inflater.inflate(R.layout.slider_item, container, false);
            ImageView imageView = (ImageView) viewItem.findViewById(R.id.slidera_image);

//            imageView.setImageURI(Uri.parse(imagesArray.get(position)));
            Glide.with(IdoDetail.this).load(imagesArray.get(position))
                    .placeholder(R.color.light_grey).fitCenter()
                    .into(imageView);
            ((ViewPager)container).addView(viewItem);

            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Popupmethod(position);
                }
            });

            return viewItem;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imagesArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == ((View)object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView((View) object);
        }
    }

    public void RentPopupmethod() {

        alertDialog = new AlertDialog.Builder(IdoDetail.this).create();
        View dialog = getLayoutInflater().inflate(R.layout.rent_popup, null);

        final Button request_for_book_tv = (Button) dialog.findViewById(R.id.request_for_book_tv);
        final EditText no_day_w_mon = (EditText) dialog.findViewById(R.id.enter_no_dwm);
        final TextView day_tv = (TextView) dialog.findViewById(R.id.day_tv);
        final TextView week_tv = (TextView) dialog.findViewById(R.id.week_tv);
        final TextView month_tv = (TextView) dialog.findViewById(R.id.month);
        rent_estimation_price = (TextView) dialog.findViewById(R.id.rent_estimation_price);
        day = true;
        week = false;
        month = false;
        hint = "Enter number of Days";
        no_day_w_mon.setHint(hint);
        day_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = true;
                week = false;
                month = false;
                no_day_w_mon.setText("");
                day_tv.setTextColor(getResources().getColor(R.color.black));
                day_tv.setBackgroundColor(getResources().getColor(R.color.yellow));
                week_tv.setTextColor(getResources().getColor(R.color.white));
                week_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                month_tv.setTextColor(getResources().getColor(R.color.white));
                month_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                hint = "Enter number of Days";
                no_day_w_mon.setHint(hint);
            }
        });
        week_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = false;
                week = true;
                month = false;
                no_day_w_mon.setText("");
                day_tv.setTextColor(getResources().getColor(R.color.white));
                day_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                week_tv.setTextColor(getResources().getColor(R.color.black));
                week_tv.setBackgroundColor(getResources().getColor(R.color.yellow));
                month_tv.setTextColor(getResources().getColor(R.color.white));
                month_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                hint = "Enter number of Week";
                no_day_w_mon.setHint(hint);
            }
        });
        month_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = false;
                week = false;
                month = true;
                no_day_w_mon.setText("");
                day_tv.setTextColor(getResources().getColor(R.color.white));
                day_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                week_tv.setTextColor(getResources().getColor(R.color.white));
                week_tv.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                month_tv.setTextColor(getResources().getColor(R.color.black));
                month_tv.setBackgroundColor(getResources().getColor(R.color.yellow));
                hint = "Enter number of months";
                no_day_w_mon.setHint(hint);
            }
        });
        no_day_w_mon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(no_day_w_mon.getText().toString())) {
                    if (day) {
                        Toast.makeText(IdoDetail.this, "Day is selected you can enter from 1 to 70", Toast.LENGTH_SHORT).show();
                    } else if (week) {
                        Toast.makeText(IdoDetail.this, "Week is selected you can enter from 1 to 10", Toast.LENGTH_SHORT).show();
                    } else if (month) {
                        Toast.makeText(IdoDetail.this, "Month is selected you can enter from 1 to 2", Toast.LENGTH_SHORT).show();
                    }
                    rent_estimation_price.setText("0");
                } else {
                    int value = Integer.parseInt(no_day_w_mon.getText().toString().trim());
//                    double total_amount,total_rent = 0;
//                    double rent = 0,security_charge,service_tax;
                    request_for_book_tv.setEnabled(true);
                    if (day && value >= 1 && value < 71) {
                        days = value;
                        RentCalculationMethod(days);
//                        security_charge = Double.parseDouble(mrp_tv.getText().toString().trim())/2;
//                        rent = Rent(days,security_charge);
//                        service_tax = rent/2;
//                        total_rent = service_tax + rent;
//                        total_amount = security_charge + total_rent;
//                        Log.d("","rent & total rent:" + rent +" " +total_rent +"\n serice ch: " + service_tax + "\n Total Am: " +total_amount);
//                        //varible initilaize
//                        rent_string = String.valueOf(total_rent);
//                        security_charge_string = String.valueOf(security_charge);
//                        service_tax_string = String.valueOf(service_tax);

//                        rent_estimation_price.setText(" " + total_amount);
                    } else if (week && value >= 1 && value < 11) {
                        days = 7*value;
                        RentCalculationMethod(days);
//                        security_charge = Double.parseDouble(mrp_tv.getText().toString().trim())/2;
//                        rent = Rent(days,security_charge);
//                        service_tax = rent/2;
//                        total_rent = service_tax + rent;
//                        total_amount = security_charge + total_rent;
//
//                        Log.d("","rent & total rent:" + rent +" " +total_rent +"\n serice ch: " + service_tax + "\n Total Am: " +total_amount);
//
//                        //varible initilaize
//                        rent_string = String.valueOf(total_rent);
//                        security_charge_string = String.valueOf(security_charge);
//                        service_tax_string = String.valueOf(service_tax);

//                        rent_estimation_price.setText(" " + total_amount);
                    } else if (month && value >= 1 && value < 3) {
                        days = 30*value;
                        RentCalculationMethod(days);
//                        security_charge = Double.parseDouble(mrp_tv.getText().toString().trim())/2;
//                        rent = Rent(days,security_charge);
//                        service_tax = rent/2;
//                        total_rent = service_tax + rent;
//                        total_amount = security_charge + total_rent;
//                        //varible initilaize
//                        rent_string = String.valueOf(total_rent);
//                        security_charge_string = String.valueOf(security_charge);
//                        service_tax_string = String.valueOf(service_tax);
//                        Log.d("","rent & total rent:" + rent +" " +total_rent +"\n serice ch: " + service_tax + "\n Total Am: " +total_amount);
//                        rent_estimation_price.setText(" " + total_amount);
                    } else {
                        no_day_w_mon.setError("incorrect input!!Please enter valid Input");
                        no_day_w_mon.requestFocus();
                        request_for_book_tv.setEnabled(false);
                    }
                }
            }
        });
        request_for_book_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(no_day_w_mon.getText().toString().trim())) {
                    no_day_w_mon.setError(hint);
                    no_day_w_mon.requestFocus();
                } else {
                    try {
                        JSONObject dataObj = new JSONObject();
                        dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                        dataObj.put("to_id", recuriter_id);
                        dataObj.put("book_id", book_id);
                        dataObj.put("duration", days + "");
                        dataObj.put("service_charge",service_tax_string );
                        dataObj.put("security_charge",security_charge_string );
                        dataObj.put("rent", rent_string);

                        JSONObject resqObj = new JSONObject();
                        resqObj.put("method", "api_book_request");
                        resqObj.put("data", dataObj);

                        HashMap objectNew = new HashMap();
                        objectNew.put("request", resqObj.toString());
                        Log.d("api_book_request_data", objectNew.toString());
                        new WebTask(IdoDetail.this, WebUrls.BASE_URL, objectNew, IdoDetail.this, RequestCode.CODE_Book_Request_Api, 1);

                    } catch (JSONException e) {

                    }
                }
            }
        });
        alertDialog.setView(dialog);
        alertDialog.show();
    }
    private void Popupmethod(int pos){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_image_perview);

        ImageView full_image_cancel= (ImageView) dialog.findViewById(R.id.full_image_cancel);
        ViewPager full_image_viewpage = (ViewPager)dialog.findViewById(R.id.full_image_viewpage);
        DotsIndicator dotsin_vi = (DotsIndicator)dialog.findViewById(R.id.dots_indicator);
        full_image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        PagerAdapter adapter = new FullImageAdapter(IdoDetail.this,imageId);
        full_image_viewpage.setAdapter(adapter);
        dotsin_vi.setViewPager(full_image_viewpage);

        dialog.show();
    }
    public void BuyPopupmethod(){
        buyDialog = new AlertDialog.Builder(IdoDetail.this).create();
        View view = getLayoutInflater().inflate(R.layout.buy_popup, null);


        final TextView cost_estimation_price = (TextView)view.findViewById(R.id.cost_estimation_price);

        final Button request_for_book_tv  = (Button)view.findViewById(R.id.request_for_book_tv);
        final TextView total_tv = (TextView)view.findViewById(R.id.total_tv);
        final TextView service_tax = (TextView)view.findViewById(R.id.service_tax_tv);
        service_tax.setText(serive_tax);
        cost_estimation_price.setText(String.format(" %s", Double.valueOf(buyPrice)));
        total_amount = Double.valueOf(buyPrice) + Double.parseDouble(service_tax.getText().toString().trim())+"";
        Log.d("total_amount",total_amount);
        total_tv.setText(String.format("%s INR", Double.valueOf(buyPrice) + Double.parseDouble(service_tax.getText().toString().trim())));
        request_for_book_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(no_day_w_mon.getText().toString().trim())) {
//                    no_day_w_mon.setError(hint);
//                    no_day_w_mon.requestFocus();
//                } else {
                    try {
                        JSONObject dataObj = new JSONObject();
                        dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                        dataObj.put("to_id", recuriter_id);
                        dataObj.put("book_id", book_id);
                        dataObj.put("tax",serive_tax);
                        dataObj.put("mrp",total_amount);

                        JSONObject resqObj = new JSONObject();
                        resqObj.put("method", "api_buybook_request");
                        resqObj.put("data", dataObj);

                        HashMap objectNew = new HashMap();
                        objectNew.put("request", resqObj.toString());
                        Log.d("api_book_request_data", objectNew.toString());
                        new WebTask(IdoDetail.this, WebUrls.BASE_URL, objectNew, IdoDetail.this, RequestCode.CODE_BuyRequest, 1);

                    } catch (JSONException e) {

                    }
//                }
            }
        });

        // show it
        buyDialog.setView(view);
        buyDialog.show();
    }
    public void RentCalculationMethod(int daysi){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("mrp", mrp_tv.getText());
            dataObj.put("days",daysi+"");

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","rent_calculation");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("rent_calculation_data",objectNew.toString());
            new WebTask(IdoDetail.this, WebUrls.BASE_URL,objectNew,IdoDetail.this, RequestCode.CODE_Rent_Calculation,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void BookDetailMethod(String book_i){
//        book_id = getIntent().getExtras().getString("book_id","");
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("book_id", book_i);
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            dataObj.put("lat",Nearbybooks.latti+"");
            dataObj.put("long",Nearbybooks.longi+"");
            JSONObject resqObj = new JSONObject();
            resqObj.put("method","book_viewby_bookid");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("book_viewby_bookid_data",objectNew.toString());
            new WebTask(IdoDetail.this, WebUrls.BASE_URL,objectNew,IdoDetail.this, RequestCode.CODE_Book_Viewby_Bookid,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void LikeMethod(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            dataObj.put("recuriter_id",recuriter_id);
            dataObj.put("book_id",book_id);

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","api_like_books");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("api_like_books_data",objectNew.toString());
            new WebTask(IdoDetail.this, WebUrls.BASE_URL,objectNew,IdoDetail.this, RequestCode.CODE_Like_Books,5);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("book_viewby_bookid_res",response);
        if (RequestCode.CODE_Book_Viewby_Bookid == taskcode) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null && jsonObject.optString("status").compareTo("success") == 0) {
                    JSONArray dataarray = jsonObject.optJSONArray("data");

                    for (int i = 0; i < dataarray.length(); i++) {
                        JSONObject dataobj = dataarray.getJSONObject(i);
                        if (dataobj != null) {
                            book_name_tv.setText(dataobj.optString("book_name"));
    //                        String date = dataobj.optString("created_at");
    //                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //                        Date newdate = simpleDateFormat.parse(date);
    //                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
    //                            user_date.setText("Since " + dateFormat.format(newdate));
                            book_name_tv.setText(dataobj.optString("book_name"));
                            book_author_tv.setText(dataobj.optString("author_name"));
                            book_description_tv.setText(dataobj.optString("description"));
                            book_no_of_pages_tv.setText(dataobj.optString("page_no"));
                            mrp_tv.setText(dataobj.optString("mrp"));
                            address_full.setText(dataobj.optString("location"));
                            book_categories_tv.setText(dataobj.optString("cat_name"));
                            recuriter_id = dataobj.optString("user_id");
                            book_publication_tv.setText(dataobj.optString("publication"));
                            ido_detail__toolbar.setTitle(dataobj.optString("mrp"));
                            buyPrice = dataobj.optString("selling_price");
                            serive_tax = dataobj.optString("servicetax");
                            security_tv.setText(String.format("%s", Double.parseDouble(dataobj.optString("mrp")) / 2));

                            latitude = dataobj.optDouble("latitude");
                            longitude = dataobj.optDouble("longitude");

                            DecimalFormat decimalFormat = new DecimalFormat(".##");
                            address_title.setText(String.format("%s km Away", decimalFormat.format(dataobj.optDouble("distance"))));

                            setSupportActionBar(ido_detail__toolbar);
                            String fsd = dataobj.optString("book_name");
                            String rd = fsd.substring(0,1).toUpperCase();
                            getSupportActionBar().setTitle(fsd.replaceFirst(fsd.substring(0,1),rd)+"/Price: "+dataobj.optString("mrp")+" INR");
                            //toolbar back button color and icon change
                            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
                            upArrow.setColorFilter(Color.parseColor("#2f3e9e"), PorterDuff.Mode.SRC_ATOP);
                            getSupportActionBar().setHomeAsUpIndicator(upArrow);

                            // add back arrow to toolbar
                            if (getSupportActionBar() != null){
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                getSupportActionBar().setDisplayShowHomeEnabled(true);
                            }

                            if (dataobj.optString("user_like_status").compareTo("1") == 0) {
                                favorite_icon.setChecked(true);
    //                                favorite_icon.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                            } else {
                                favorite_icon.setChecked(false);
    //                                favorite_icon.setImageDrawable(getDrawable(R.drawable.heart));
                            }


                            if (dataobj.optJSONArray("attachment") != null) {
                                JSONArray attachmentArray = dataobj.optJSONArray("attachment");
                                imageId.clear();
    //                                imageId = new String[attachmentArray.length()];
                                for (int k = 0; k < attachmentArray.length(); k++) {
                                    JSONObject jsonObject1 = attachmentArray.getJSONObject(k);
                                    imageId.add(WebUrls.IMG_BASE_URL + WebUrls.Book_Image_Url + jsonObject1.optString("name"));
    //                                    imageId[k] =  WebUrls.IMG_BASE_URL+WebUrls.Book_Image_Url+jsonObject1.optString("name");
                                }
                                viewPagerInit();
                            }
                            progress_horizontal.setVisibility(View.GONE);
                            nestedscroll.setVisibility(View.VISIBLE);
                            bottom.setVisibility(View.VISIBLE);
                        }
                    }


                } else {
                    Toast.makeText(IdoDetail.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Like_Books == taskcode){
            Log.d("api_like_books_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    if (dataObj.optString("like_status").compareTo("1")==0){
                        favorite_icon.setChecked(true);
//                        favorite_icon.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                        HomeScreen.getInstance().All_Book_List_Method();
                    }else {
                        favorite_icon.setChecked(false);
                        HomeScreen.getInstance().All_Book_List_Method();
//                        favorite_icon.setImageDrawable(getDrawable(R.drawable.heart));
                    }
                }else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (RequestCode.CODE_Book_Request_Api == taskcode){
            Log.d("api_book_request_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_BuyRequest == taskcode){
            Log.d("Buy_request_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    buyDialog.dismiss();
                }else {
                    Toast.makeText(this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    buyDialog.dismiss();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        if (RequestCode.CODE_Rent_Calculation == taskcode){
            Log.d("Rent_Calculation_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").compareTo("success")==0){
                    rent_estimation_price.setText(jsonObject.optJSONObject("data").optString("total_amount"));
                    service_tax_string = jsonObject.optJSONObject("data").optString("service_charge");
                    security_charge_string = jsonObject.optJSONObject("data").optString("security_charge");
                    rent_string = jsonObject.optJSONObject("data").optString("rent");


                }else {
                    Toast.makeText(this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }



    }
    int dotscount;
    int currentPage = 0;
    private void viewPagerInit() {

        if (imageId != null) {
            PagerAdapter adapter = new CustomAdapter(IdoDetail.this, imageId);
            slidera.setAdapter(adapter);
            dotsIndicator.setViewPager(slidera);
//            slidera.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                @Override
//                public void onPageScrolled(int i, float v, int i1) {
//
//                }
//
//                @Override
//                public void onPageSelected(int position) {
//                    for(int i = 0; i< imageId.size(); i++){
//                        dots[i].setImageDrawable(ContextCompat.getDrawable(IdoDetail.this, R.drawable.noactivie_dots));
//                    }
//                    dots[position].setImageDrawable(ContextCompat.getDrawable(IdoDetail.this, R.drawable.active_dots));
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int i) {
//
//                }
//            });
//            dotscount = adapter.getCount();
//            dots = new ImageView[dotscount];
//
//            for (int i = 0; i < imageId.size(); i++) {
//                dots[i] = new ImageView(this);
//                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.noactivie_dots));
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                params.setMargins(8, 0, 8, 0);
//                dotsIndicator.addView(dots[i], params);
//            }
//
//            dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));

//            /*After setting the adapter use the timer */
//            final Handler handler = new Handler();
//            final Runnable Update = new Runnable() {
//                public void run() {
//                    if (currentPage == imageId.size()) {
//                        currentPage = 0;
//                    }
//                    if (slidera!=null){
//                        slidera.setCurrentItem(currentPage++, true);
//                    }
//
//                }
//            };
//
//            timer = new Timer(); // This will create a new Thread
//            timer.schedule(new TimerTask() { // task to be scheduled
//
//                @Override
//                public void run() {
//                    handler.post(Update);
//                }
//            }, DELAY_MS, PERIOD_MS);
        }
    }
    public class FullImageAdapter extends PagerAdapter {

        private Activity activity;
        private ArrayList<String> imagesArray;
        private String[] namesArray;

        public FullImageAdapter(Activity activity,ArrayList<String> imagesArray){

            this.activity = activity;
            this.imagesArray = imagesArray;
            this.namesArray = namesArray;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater = ((Activity)activity).getLayoutInflater();

            View viewItem = inflater.inflate(R.layout.full_image_view, container, false);
            PhotoView imageView = (PhotoView) viewItem.findViewById(R.id.full_image);
//            imageView.setImageResource(imagesArray.get(u));
            Glide.with(IdoDetail.this)
                    .load(imagesArray.get(position))
                    .into(imageView);
            ((ViewPager)container).addView(viewItem);

            return viewItem;
        }

        @Override
        
        public int getCount() {
            // TODO Auto-generated method stub
            return imagesArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == ((View)object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView((View) object);
        }
    }



//    public double Rent(int days,double security_charge){
//        double rent = 0;
//        if (days <= 7) {
//            rent =  (security_charge*10)/100;
//        } else if (days > 7 && days <= 14) {
//            rent =  (security_charge*20)/100;
//        } else if (days > 14 && days <= 21) {
//            rent =  (security_charge*30)/100;
//        } else if (days > 21 && days <= 28) {
//            rent =  (security_charge*40)/100;
//        } else if (days > 28 && days <= 35) {
//            rent =  (security_charge*50)/100;
//        } else if (days > 35 && days <= 42) {
//            rent =  (security_charge*60)/100;
//        } else if (days > 42 && days <= 49) {
//            rent =  (security_charge*70)/100;
//        } else if (days > 49 && days <= 56) {
//            rent =  (security_charge*80)/100;
//        } else if (days > 56 && days <= 63) {
//            rent =  (security_charge*90)/100;
//        } else if (days > 63 && days <= 70) {
//            rent =  (security_charge*100)/100;
//        }
//        return rent;
//    }
}

