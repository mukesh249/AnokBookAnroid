package com.anokbook.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.JsonObject;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Adapters.EditPostImageAdapter;
import com.anokbook.Adapters.PlaceAutocompleteAdapter;
import com.anokbook.Api.ApiConfig;
import com.anokbook.Api.AppConfig;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.MyApplication;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.EditPostImageModel;
import com.anokbook.Models.PlaceInfo;
import com.anokbook.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditPost extends AppCompatActivity implements WebCompleteTask , GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.ap_book_name_et) EditText ap_book_name;
    @BindView(R.id.ap_book_auth_name_et) EditText ap_book_auth_name_et;
    @BindView(R.id.ap_book_no_page_et) EditText ap_book_no_page_et;
    @BindView(R.id.ap_book_mrp_et) EditText ap_book_mrp_et;
    @BindView(R.id.ap_book_description_et) EditText ap_book_description_et;
    @BindView(R.id.ap_book_publication_add_et) EditText ap_book_publication_add_et;
    @BindView(R.id.ap_location_et) AutoCompleteTextView ap_location_et;
    @BindView(R.id.ap_address_et) EditText ap_address_et;
    @BindView(R.id.ap_landmark_et) EditText ap_landmark_et;
    @BindView(R.id.ap_contact_no_et) EditText ap_contact_no_et;
    @BindView(R.id.ap_alt_contact_no_et) EditText ap_alt_contact_no_et;
    @BindView(R.id.addpost_toobar) Toolbar toolbar;
    @BindView(R.id.recycer_view_add_post) RecyclerView recycer_view_add_post;
    @BindView(R.id.ap_purpose_spinner) Spinner ap_purpose_spinner;
    @BindView(R.id.ap_categories_spinner) Spinner ap_categories_spinner;
    @BindView(R.id.submit) Button submit;
    @BindView(R.id.ap_book_selling_price_et) EditText ap_book_selling_price_et;
    EditPostImageAdapter editPostImageAdapter;
    ArrayList<String> cat_id_array = new ArrayList<>();
    ArrayList<String> cat_name_array = new ArrayList<>();
    ArrayList<String> purpose_id_array = new ArrayList<>();
    ArrayList<String> purpose_name_array = new ArrayList<>();
    String cat_id,purpose_id;
    static ArrayList<EditPostImageModel> editPostImageModelArrayList=new ArrayList<>();
    ArrayList<Uri> upload_image_array = new ArrayList<>();
    private final static String TAG = "AddPost";
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    double lat,lng;
    LatLng latlng;
    String book_id;

    private static EditPost mInstance;
    @BindView(R.id.add_image) ImageView add_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        ButterKnife.bind(this,this);
        mInstance = this;

        MyApplication.hideKeyBoard(EditPost.this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setSupportActionBar(toolbar);
        setTitle("Edit Post");
        CategoriesListMethod();
        PurposeListMethod();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPostMethod();
            }
        });
        if (getIntent().getExtras()!=null)
        book_id= getIntent().getExtras().getString("user_book_id","");
        BookDetailMethod();
        init();
        recycer_view_add_post.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ap_categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(AddPost.this, cat_id_array.get(position), Toast.LENGTH_SHORT).show();
                if (cat_id_array!=null && cat_id_array.size()!=0)
                    cat_id = cat_id_array.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ap_purpose_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (purpose_id_array!=null && purpose_id_array.size()!=0)
                    purpose_id = purpose_id_array.get(position);
//                    Toast.makeText(AddPost.this,purpose_id_array.get(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelection();
            }
        });
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
    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    public void CategoriesListMethod(){
        try {
            JSONObject dataObj = new JSONObject();
            JSONObject resqObj = new JSONObject();
            resqObj.put("method","category_list");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("category_list_data",objectNew.toString());
            new WebTask(EditPost.this, WebUrls.BASE_URL,objectNew,EditPost.this, RequestCode.CODE_Category_List,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void PurposeListMethod(){
        try {
            JSONObject dataObj = new JSONObject();

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","purpose_list");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("purpose_list_data",objectNew.toString());
            new WebTask(EditPost.this, WebUrls.BASE_URL,objectNew,EditPost.this, RequestCode.CODE_Purpose_List,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void BookDetailMethod(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("book_id", book_id);
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            JSONObject resqObj = new JSONObject();
            resqObj.put("method","book_viewby_bookid");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("book_viewby_bookid_data",objectNew.toString());
            new WebTask(EditPost.this, WebUrls.BASE_URL,objectNew,EditPost.this, RequestCode.CODE_Book_Viewby_Bookid,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Category_List == taskcode){
            Log.d("category_list_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){

                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i =0 ;i<dataArray.length();i++){
                        JSONObject dataObj = dataArray.optJSONObject(i);
                        cat_id_array.add(dataObj.optString("id"));
                        cat_name_array.add(dataObj.optString("cat_name"));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cat_name_array);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ap_categories_spinner.setAdapter(arrayAdapter);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else   if (RequestCode.CODE_Purpose_List == taskcode){
            Log.d("purpose_list_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){

                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i =0 ;i<dataArray.length();i++){
                        JSONObject dataObj = dataArray.optJSONObject(i);
                        purpose_id_array.add(dataObj.optString("id"));
                        purpose_name_array.add(dataObj.optString("purpose_name"));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,purpose_name_array);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ap_purpose_spinner.setAdapter(arrayAdapter);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        if (RequestCode.CODE_Book_Viewby_Bookid == taskcode){
            Log.d("book_viewby_bookid_res",response);
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");

                    for (int i =0;i<dataarray.length();i++) {
                        JSONObject dataobj = dataarray.getJSONObject(i);
                        if (dataobj != null) {
                            ap_book_name.setText(dataobj.optString("book_name"));
                            ap_book_auth_name_et.setText(dataobj.optString("author_name"));
                            ap_book_description_et.setText(dataobj.optString("description"));
                            ap_book_no_page_et.setText(dataobj.optString("page_no"));
                            ap_book_mrp_et.setText(dataobj.optString("mrp"));
                            ap_address_et.setText(dataobj.optString("address"));
//                            ap_categories_spinner.setSelection(Integer.parseInt(dataobj.optString("cat_id")));
                            ap_book_publication_add_et.setText(dataobj.optString("publication"));
                            ap_location_et.setText(dataobj.optString("location"));
                            ap_landmark_et.setText(dataobj.optString("landmarks"));
                            ap_contact_no_et.setText(dataobj.optString("contact_no"));
                            ap_alt_contact_no_et.setText(dataobj.optString("alternate_no"));
                            ap_book_selling_price_et.setText(dataobj.optString("selling_price"));
//                            ap_purpose_spinner.setSelection(Integer.parseInt(dataobj.optString("pupose")));
                            lat = Double.parseDouble(dataobj.optString("latitude"));
                            lng = Double.parseDouble(dataobj.optString("longitude"));
                            editPostImageModelArrayList.clear();
                            if (dataobj.optJSONArray("attachment")!=null){
                                JSONArray attachmentArray = dataobj.optJSONArray("attachment");
                                for (int k = 0; k < attachmentArray.length(); k++) {
                                    EditPostImageModel editPostImageModel = new EditPostImageModel();
//                                    arrayList_image.add(loadImageFromNetwork(WebUrls.IMG_BASE_URL+WebUrls.Book_Image_Url+jsonObject1.optString("name")));
//                                    if (attachmentArray.length()==k+1){
////                                        editPostImageModel.setImage_url("dffgsd");
//                                    }
//                                    if (k<attachmentArray.length())
                                        JSONObject jsonObject1 = attachmentArray.getJSONObject(k);
                                        editPostImageModel.setImage_id(jsonObject1.optString("id"));
                                        editPostImageModel.setImage_url(WebUrls.IMG_BASE_URL+WebUrls.Book_Image_Url+jsonObject1.optString("name"));
//                                    }
                                    editPostImageModelArrayList.add(editPostImageModel);
                                }
//                                File file = new File(String.valueOf(R.drawable.ic_gallery));
//                                Uri uri = Uri.fromFile(file);

//                                editPostImageModelArrayList.add(attachmentArray.length()+"");

                                editPostImageAdapter = new EditPostImageAdapter(upload_image_array,editPostImageModelArrayList,EditPost.this);
                                recycer_view_add_post.setAdapter(editPostImageAdapter);
                                editPostImageAdapter.notifyDataSetChanged();
                            }
                        }
                    }


                }else {
                    Toast.makeText(EditPost.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
    private void init(){
        Log.d(TAG,"init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        ap_location_et.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,
                LAT_LNG_BOUNDS,null);
        ap_location_et.setAdapter(mPlaceAutocompleteAdapter);

        ap_location_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //excuting our method for searching
                    geoLocate();
                }
                return false;
            }
        });
    }
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geoLoating");
        String searchString = ap_location_et.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOexception" + e.getMessage());
        }

        if (list.size()>0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
            //Toast.makeText(getContext(),address.toString(),Toast.LENGTH_LONG).show();
            //moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }
    /*
       -------------------------------google places API autocomplete suggestions
 */
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            SharedPrefManager.getInstance(getApplicationContext()).hideSoftKeyBoard(Signup.this);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient,placeId);
            placeResult.setResultCallback(mUpdatePlaceDitailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDitailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()){
                Log.d(TAG,"onResult: place query did not complete successfully."+ places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {
                mPlace = new PlaceInfo();
                mPlace.setAddress(place.getAddress().toString());
//                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setName(place.getName().toString());
                mPlace.setRating(place.getRating());
                mPlace.setWebsiteuri(place.getWebsiteUri());

                Log.d(TAG,"OnResult: place details " + mPlace.toString());

            }catch (NullPointerException e){
                Log.d(TAG,"OnResult: NullPointerException " + e.getMessage());
            }

            latlng= mPlace.getLatLng();
            lat = latlng.latitude;
            lng = latlng.longitude;
            places.release();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void AddPostMethod(){
        if (TextUtils.isEmpty(ap_book_name.getText().toString().trim())){
            ap_book_name.setError(getString(R.string.enter_book_name));
            ap_book_name.requestFocus();
        }else if (TextUtils.isEmpty(ap_book_auth_name_et.getText().toString().trim())){
            ap_book_auth_name_et.setError(getString(R.string.enter_author_name));
            ap_book_auth_name_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_book_no_page_et.getText().toString().trim())){
            ap_book_no_page_et.setError(getString(R.string.enter_page_no));
            ap_book_no_page_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_book_description_et.getText().toString().trim())){
            ap_book_description_et.setError(getString(R.string.description));
            ap_book_description_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_book_mrp_et.getText().toString().trim())){
            ap_book_mrp_et.setError(getString(R.string.enter_mrp));
            ap_book_mrp_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_book_publication_add_et.getText().toString().trim())){
            ap_book_publication_add_et.setError(getString(R.string.enter_publication));
            ap_book_publication_add_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_location_et.getText().toString().trim())){
            ap_location_et.setError(getString(R.string.enter_location_a));
            ap_location_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_address_et.getText().toString().trim())){
            ap_address_et.setError(getString(R.string.street));
            ap_address_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_landmark_et.getText().toString().trim())){
            ap_landmark_et.setError(getString(R.string.enter_landmark));
            ap_landmark_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_contact_no_et.getText().toString().trim())){
            ap_contact_no_et.setError(getString(R.string.enter_contact_number));
            ap_contact_no_et.requestFocus();
        }else if (TextUtils.isEmpty(ap_alt_contact_no_et.getText().toString().trim())){
            ap_alt_contact_no_et.setError(getString(R.string.enter_contact_number));
            ap_alt_contact_no_et.requestFocus();
        }else {
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("book_id",book_id);
                dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                dataObj.put("book_name", ap_book_name.getText().toString().trim());
                dataObj.put("cat_id", cat_id);
                dataObj.put("author_name", ap_book_auth_name_et.getText().toString().trim());
                dataObj.put("page_no", ap_book_no_page_et.getText().toString().trim());
                dataObj.put("description", ap_book_description_et.getText().toString().trim());
                dataObj.put("mrp", ap_book_mrp_et.getText().toString().trim());
                dataObj.put("publication", ap_book_publication_add_et.getText().toString().trim());
                dataObj.put("location", ap_location_et.getText().toString().trim());
                dataObj.put("address", ap_address_et.getText().toString().trim());
                dataObj.put("landmarks", ap_landmark_et.getText().toString().trim());
                dataObj.put("contact_no", ap_contact_no_et.getText().toString().trim());
                dataObj.put("alternate_no", ap_alt_contact_no_et.getText().toString().trim());
                dataObj.put("pupose", purpose_id);
                dataObj.put("latitude",lat);
                dataObj.put("longitude",lng);

                JSONObject resqObj = new JSONObject();
                resqObj.put("method", "api_updatebook");
                resqObj.put("data", dataObj);

//                HashMap objectNew = new HashMap();
//                objectNew.put("request", resqObj.toString());
                Log.d("api_addbook_data", resqObj.toString());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.setCancelable(true);
                progressDialog.show();

                String jsonObjData = String.valueOf(resqObj);
                RequestBody objdatabody = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);

                final ArrayList<MultipartBody.Part> photos = new ArrayList<>();
                MultipartBody.Part filePartmultipleImagesPro,filePartmultipleImages;
//                RequestBody requestBodyProfile = RequestBody.create(MediaType.parse("image/*"), Profile_file);
//                filePartmultipleImagesPro = MultipartBody.Part.createFormData("image", Profile_file.getName(), requestBodyProfile);
//                Log.d("image_array",arrayList_image.toString());
                Log.d("upload_image_array",upload_image_array.toString());
                for (int i = 0; i < upload_image_array.size(); i++) {
                    File file = null;
                    file = new File(String.valueOf(upload_image_array.get(i).getPath()));
                    Log.d("file_path_image",file.getPath());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),  file);
                    filePartmultipleImages = MultipartBody.Part.createFormData("attachment[]", file.getName(), requestBody);
                    photos.add(filePartmultipleImages);
                }
//                for (int i = 0; i < upload_image_array.size(); i++) {
//                    File file = null;
//                    file = new File(String.valueOf(upload_image_array.get(i).getPath()));
//////create a file to write bitmap data
////                    File f = new File(this.getCacheDir(), "image"+i);
////                    f.createNewFile();
////
//////Convert bitmap to byte array
////                    Bitmap bitmap = arrayList_image.get(i);
////                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
////                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70 /*ignored for PNG*/, bos);
////                    byte[] bitmapdata = bos.toByteArray();
////                    Log.d("bit_image",bitmapdata.toString());
//////write the bytes in file
////                    FileOutputStream fos = new FileOutputStream(f);
////                    fos.write(bitmapdata);
////                    fos.flush();
////                    fos.close();
//
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),  file);
//                    filePartmultipleImages = MultipartBody.Part.createFormData("attachment[]", file.getName()+".jpg", requestBody);
//                    photos.add(filePartmultipleImages);
//                }

                Log.d("photos_array",photos.toString());
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                Call<JsonObject> call = getResponse.AddPost(
                        objdatabody,
                        photos
                );

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                        dismissProgressBar();
                        if (response.isSuccessful()) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response.body().toString());
                                Log.d("","response_body"+  response.body().toString());
                                JSONArray dataObj = obj.optJSONArray("data");

                                if (obj.optString("status").compareTo("success") == 0) {
                                    try {
//                                        Intent intent = new Intent(EditPost.this, HomeScreen.class);
                                        Toast.makeText(EditPost.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                       // IdoDetail.getInstance().BookDetailMethod(dataObj.optJSONObject(0).optString("id"));

//                                        IdoDetail.getInstance().recreate();
                                        finish();
//                                        startActivity(new Intent(EditPost.this, IdoDetail.class).putExtra("book_id",));
//                                        progressDialog.dismiss();
                                        finish();
                                    } catch (Exception e) {
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                JSONObject responseJ = new JSONObject(response.errorBody().string());
                                System.out.println("error response " + responseJ.toString());
                                try {
                                    JSONObject errorObje = responseJ.getJSONObject("error");
                                    String status = errorObje.getString("statusCode");
                                    String message = errorObje.getString("message");
                                    Log.e("Error Status", status);
                                    Log.e("Error Message", message);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();

                            } catch (Exception e) {

                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
//                        dismissProgressBar();
                        //  TestFragment.imageStringpath.clear();
                        Toast.makeText(EditPost.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println("fail response.." + t.toString());
//                        dismissProgressBar();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap loadImageFromNetwork(String url){
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static EditPost getInstance(){
        return mInstance;
    }
    public void ImageSelection(){
        Matisse.from(EditPost.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(1);
    }
    ArrayList<Uri> mSelected;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mSelected=new ArrayList<>();
            mSelected= (ArrayList<Uri>) Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
            for (int i = 0;i<mSelected.size();i++){
                EditPostImageModel editPostImageModel = new EditPostImageModel();
                editPostImageModel.setImage_url(mSelected.get(i).toString());
                Log.d("file_path",getPath(mSelected.get(i)));
                upload_image_array.add(Uri.parse(getPath(mSelected.get(i))));
                editPostImageModelArrayList.add(editPostImageModel);
            }
            editPostImageAdapter = new EditPostImageAdapter(upload_image_array,editPostImageModelArrayList,EditPost.this);
            recycer_view_add_post.setAdapter(editPostImageAdapter);
            editPostImageAdapter.notifyDataSetChanged();
        }
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
