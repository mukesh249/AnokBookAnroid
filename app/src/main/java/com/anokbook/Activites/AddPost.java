package com.anokbook.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anokbook.Adapters.AddPostImageAdapter;
import com.anokbook.Adapters.PlaceAutocompleteAdapter;
import com.anokbook.Api.ApiConfig;
import com.anokbook.Api.AppConfig;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.Models.PlaceInfo;
import com.anokbook.R;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddPost extends AppCompatActivity implements WebCompleteTask, GoogleApiClient.OnConnectionFailedListener {

    private final static String TAG = "AddPost";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );
    @BindView(R.id.ap_book_name_et)
    EditText ap_book_name;
    @BindView(R.id.ap_book_auth_name_et)
    EditText ap_book_auth_name_et;
    @BindView(R.id.ap_book_no_page_et)
    EditText ap_book_no_page_et;
    @BindView(R.id.ap_book_mrp_et)
    EditText ap_book_mrp_et;
    @BindView(R.id.ap_book_selling_price_et)
    EditText ap_book_selling_price_et;
    @BindView(R.id.ap_book_description_et)
    EditText ap_book_description_et;
    @BindView(R.id.ap_book_publication_add_et)
    EditText ap_book_publication_add_et;
    @BindView(R.id.ap_location_et)
    AutoCompleteTextView ap_location_et;
    @BindView(R.id.ap_address_et)
    EditText ap_address_et;
    @BindView(R.id.ap_landmark_et)
    EditText ap_landmark_et;
    @BindView(R.id.ap_contact_no_et)
    EditText ap_contact_no_et;
    @BindView(R.id.ap_alt_contact_no_et)
    EditText ap_alt_contact_no_et;
    @BindView(R.id.addpost_toobar)
    Toolbar toolbar;
    @BindView(R.id.recycer_view_add_post)
    RecyclerView recycer_view_add_post;
    @BindView(R.id.ap_purpose_spinner)
    Spinner ap_purpose_spinner;
    @BindView(R.id.ap_categories_spinner)
    Spinner ap_categories_spinner;
    @BindView(R.id.submit)
    Button submit;
    AddPostImageAdapter addPostImageAdapter;
    ArrayList<String> cat_id_array = new ArrayList<>();
    ArrayList<String> cat_name_array = new ArrayList<>();
    ArrayList<String> purpose_id_array = new ArrayList<>();
    ArrayList<String> purpose_name_array = new ArrayList<>();
    String cat_id, purpose_id;
    ArrayList<Uri> arrayList = new ArrayList<>();
    double lat, lng;
    LatLng latlng;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDitailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: place query did not complete successfully." + places.getStatus().toString());
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

                Log.d(TAG, "OnResult: place details " + mPlace.toString());

            } catch (NullPointerException e) {
                Log.d(TAG, "OnResult: NullPointerException " + e.getMessage());
            }

            latlng = mPlace.getLatLng();
            lat = latlng.latitude;
            lng = latlng.longitude;
//            lat = String.valueOf(latlng.latitude);
//            lng = String.valueOf(latlng.longitude);
//            addressObj = new JSONObject();
//            try {
//                JSONObject locationobj = new JSONObject();
//                locationobj.put("lat",lat);
//                locationobj.put("lng", lng);
//                addressObj.put("address", et_address.getText().toString().trim());
//                addressObj.put("location", locationobj);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            Log.d(TAG,"OnResult: place lat lng details " + lat + ","+ lng);

            places.release();
        }
    };
    /*
       -------------------------------google places API autocomplete suggestions
 */
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            SharedPrefManager.getInstance(getApplicationContext()).hideSoftKeyBoard(Signup.this);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDitailsCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this, this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setSupportActionBar(toolbar);
        setTitle("Add Post");
        CategoriesListMethod();
        PurposeListMethod();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        arrayList = getIntent().getParcelableArrayListExtra("AddPostselecetedImage");
        if (arrayList != null)
            Log.d("AddPostSelectedImages", arrayList.toString());


        if (arrayList != null) {
            String c_m_path = "";
            recycer_view_add_post.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            addPostImageAdapter = new AddPostImageAdapter(arrayList, AddPost.this);
            for (int i = 0; i < arrayList.size(); i++) {
                c_m_path += arrayList.get(i).toString() + "\n";
            }
//            ap_book_description_et.setText(c_m_path);
        }
        recycer_view_add_post.setAdapter(addPostImageAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPostMethod();
            }
        });

        ap_categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(AddPost.this, cat_id_array.get(position), Toast.LENGTH_SHORT).show();
                if (cat_id_array != null && cat_id_array.size() != 0)
                    cat_id = cat_id_array.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ap_purpose_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (purpose_id_array != null && purpose_id_array.size() != 0)
                    purpose_id = purpose_id_array.get(position);
//                    Toast.makeText(AddPost.this,purpose_id_array.get(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        init();
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
        super.onBackPressed();  // optional depending on your needs
    }

    public void CategoriesListMethod() {
        try {
            JSONObject dataObj = new JSONObject();
            JSONObject resqObj = new JSONObject();
            resqObj.put("method", "category_list");
            resqObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", resqObj.toString());
            Log.d("category_list_data", objectNew.toString());
            new WebTask(AddPost.this, WebUrls.BASE_URL, objectNew, AddPost.this, RequestCode.CODE_Category_List, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PurposeListMethod() {
        try {
            JSONObject dataObj = new JSONObject();

            JSONObject resqObj = new JSONObject();
            resqObj.put("method", "purpose_list");
            resqObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", resqObj.toString());
            Log.d("purpose_list_data", objectNew.toString());
            new WebTask(AddPost.this, WebUrls.BASE_URL, objectNew, AddPost.this, RequestCode.CODE_Purpose_List, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String GetPath(Uri uri) {
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

    public void AddPostMethod() {
        if (TextUtils.isEmpty(ap_book_name.getText().toString().trim())) {
            ap_book_name.setError(getString(R.string.enter_book_name));
            ap_book_name.requestFocus();
        } else if (TextUtils.isEmpty(ap_book_auth_name_et.getText().toString().trim())) {
            ap_book_auth_name_et.setError(getString(R.string.enter_author_name));
            ap_book_auth_name_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_book_no_page_et.getText().toString().trim())) {
            ap_book_no_page_et.setError(getString(R.string.enter_page_no));
            ap_book_no_page_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_book_description_et.getText().toString().trim())) {
            ap_book_description_et.setError(getString(R.string.description));
            ap_book_description_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_book_mrp_et.getText().toString().trim())) {
            ap_book_mrp_et.setError(getString(R.string.enter_mrp));
            ap_book_mrp_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_book_publication_add_et.getText().toString().trim())) {
            ap_book_publication_add_et.setError(getString(R.string.enter_publication));
            ap_book_publication_add_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_location_et.getText().toString().trim())) {
            ap_location_et.setError(getString(R.string.enter_location_a));
            ap_location_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_address_et.getText().toString().trim())) {
            ap_address_et.setError(getString(R.string.street));
            ap_address_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_landmark_et.getText().toString().trim())) {
            ap_landmark_et.setError(getString(R.string.enter_landmark));
            ap_landmark_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_contact_no_et.getText().toString().trim())) {
            ap_contact_no_et.setError(getString(R.string.enter_contact_number));
            ap_contact_no_et.requestFocus();
        } else if (TextUtils.isEmpty(ap_alt_contact_no_et.getText().toString().trim())) {
            ap_alt_contact_no_et.setError(getString(R.string.enter_contact_number));
            ap_alt_contact_no_et.requestFocus();
        } else if (latlng == null) {
            Toast.makeText(AddPost.this, "Please select location from google suggestion", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
                dataObj.put("book_name", ap_book_name.getText().toString().trim());
                dataObj.put("cat_id", cat_id);
                dataObj.put("author_name", ap_book_auth_name_et.getText().toString().trim());
                dataObj.put("page_no", ap_book_no_page_et.getText().toString().trim());
                dataObj.put("description", ap_book_description_et.getText().toString().trim());
                dataObj.put("mrp", ap_book_mrp_et.getText().toString().trim());
                dataObj.put("selling_price", ap_book_selling_price_et.getText().toString().trim());
                dataObj.put("publication", ap_book_publication_add_et.getText().toString().trim());
                dataObj.put("location", ap_location_et.getText().toString().trim());
                dataObj.put("address", ap_address_et.getText().toString().trim());
                dataObj.put("landmarks", ap_landmark_et.getText().toString().trim());
                dataObj.put("contact_no", ap_contact_no_et.getText().toString().trim());
                dataObj.put("alternate_no", ap_alt_contact_no_et.getText().toString().trim());
                dataObj.put("pupose", purpose_id);
                dataObj.put("latitude", lat);
                dataObj.put("longitude", lng);

                JSONObject resqObj = new JSONObject();
                resqObj.put("method", "api_addbook");
                resqObj.put("data", dataObj);

//                HashMap objectNew = new HashMap();
//                objectNew.put("request", resqObj.toString());
                Log.d("api_addbook_data", resqObj.toString());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String jsonObjData = String.valueOf(resqObj);
                RequestBody objdatabody = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);

                ArrayList<MultipartBody.Part> photos = new ArrayList<>();
                MultipartBody.Part filePartmultipleImages, filePartmultipleImagesPro;
//                RequestBody requestBodyProfile = RequestBody.create(MediaType.parse("image/*"), Profile_file);
//                filePartmultipleImagesPro = MultipartBody.Part.createFormData("image", Profile_file.getName(), requestBodyProfile);

                for (int i = 0; i < arrayList.size(); i++) {
                    File file = null;
                    file = new File(String.valueOf(arrayList.get(i).getPath()));

                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    filePartmultipleImages = MultipartBody.Part.createFormData("attachment[]", file.getName(), requestBody);
                    photos.add(filePartmultipleImages);
                }

                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                Call<JsonObject> call = getResponse.AddPost(
                        objdatabody,
                        photos
                );

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response.body().toString());
                                Log.d("", "response_body" + response.body().toString());
                                JSONArray dataObj = obj.optJSONArray("data");

                                if (obj.optString("status").compareTo("success") == 0) {
                                    Intent intent = new Intent(AddPost.this, HomeScreen.class);
                                    Toast.makeText(AddPost.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        if (t instanceof SocketTimeoutException) {
                            String message = "Socket Time out. Please try again.";
                            Toast.makeText(AddPost.this, message, Toast.LENGTH_SHORT).show();
                            System.out.println("fail response.." + t.toString());
                            progressDialog.dismiss();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Category_List == taskcode) {
            Log.d("category_list_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null && jsonObject.optString("status").compareTo("success") == 0) {

                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.optJSONObject(i);
                        cat_id_array.add(dataObj.optString("id"));
                        cat_name_array.add(dataObj.optString("cat_name"));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat_name_array);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ap_categories_spinner.setAdapter(arrayAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (RequestCode.CODE_Purpose_List == taskcode) {
            Log.d("purpose_list_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null && jsonObject.optString("status").compareTo("success") == 0) {

                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.optJSONObject(i);
                        purpose_id_array.add(dataObj.optString("id"));
                        purpose_name_array.add(dataObj.optString("purpose_name"));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, purpose_name_array);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ap_purpose_spinner.setAdapter(arrayAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        ap_location_et.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);
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
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOexception" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
            //Toast.makeText(getContext(),address.toString(),Toast.LENGTH_LONG).show();
            //moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

//    private void uploadBitmap() {
//        if (TextUtils.isEmpty(ap_book_name.getText().toString().trim())){
//            ap_book_name.setError(getString(R.string.enter_book_name));
//            ap_book_name.requestFocus();
//        }else if (TextUtils.isEmpty(ap_book_auth_name_et.getText().toString().trim())){
//            ap_book_auth_name_et.setError(getString(R.string.enter_author_name));
//            ap_book_auth_name_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_book_no_page_et.getText().toString().trim())){
//            ap_book_no_page_et.setError(getString(R.string.enter_page_no));
//            ap_book_no_page_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_book_description_et.getText().toString().trim())){
//            ap_book_description_et.setError(getString(R.string.description));
//            ap_book_description_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_book_mrp_et.getText().toString().trim())){
//            ap_book_mrp_et.setError(getString(R.string.enter_mrp));
//            ap_book_mrp_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_book_publication_add_et.getText().toString().trim())){
//            ap_book_publication_add_et.setError(getString(R.string.enter_publication));
//            ap_book_publication_add_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_location_et.getText().toString().trim())){
//            ap_location_et.setError(getString(R.string.enter_location_a));
//            ap_location_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_address_et.getText().toString().trim())){
//            ap_address_et.setError(getString(R.string.street));
//            ap_address_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_landmark_et.getText().toString().trim())){
//            ap_landmark_et.setError(getString(R.string.enter_landmark));
//            ap_landmark_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_contact_no_et.getText().toString().trim())){
//            ap_contact_no_et.setError(getString(R.string.enter_contact_number));
//            ap_contact_no_et.requestFocus();
//        }else if (TextUtils.isEmpty(ap_alt_contact_no_et.getText().toString().trim())){
//            ap_alt_contact_no_et.setError(getString(R.string.enter_contact_number));
//            ap_alt_contact_no_et.requestFocus();
//        }else if (latlng==null){
//            Toast.makeText(AddPost.this,"Please select location from google suggestion",Toast.LENGTH_SHORT).show();
//        }else {
//            try {
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
//                dataObj.put("book_name", ap_book_name.getText().toString().trim());
//                dataObj.put("cat_id", cat_id);
//                dataObj.put("author_name", ap_book_auth_name_et.getText().toString().trim());
//                dataObj.put("page_no", ap_book_no_page_et.getText().toString().trim());
//                dataObj.put("description", ap_book_description_et.getText().toString().trim());
//                dataObj.put("mrp", ap_book_mrp_et.getText().toString().trim());
//                dataObj.put("publication", ap_book_publication_add_et.getText().toString().trim());
//                dataObj.put("location", ap_location_et.getText().toString().trim());
//                dataObj.put("address", ap_address_et.getText().toString().trim());
//                dataObj.put("landmarks", ap_landmark_et.getText().toString().trim());
//                dataObj.put("contact_no", ap_contact_no_et.getText().toString().trim());
//                dataObj.put("alternate_no", ap_alt_contact_no_et.getText().toString().trim());
//                dataObj.put("pupose", purpose_id);
//                dataObj.put("latitude", lat);
//                dataObj.put("longitude", lng);
//
//                final JSONObject resqObj = new JSONObject();
//                resqObj.put("method", "api_addbook");
//                resqObj.put("data", dataObj);
//
////                HashMap objectNew = new HashMap();
////                objectNew.put("request", resqObj.toString());
//                Log.d("api_addbook_data", resqObj.toString());
//                final ProgressDialog progressDialog = new ProgressDialog(this);
//                progressDialog.setMessage("Uploading, please wait...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                //getting the tag from the edittext
////        final String tags = editTextTags.getText().toString().trim();
//
//                //our custom volley request
//                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, WebUrls.BASE_URL,
//                        new Response.Listener<NetworkResponse>() {
//                            @Override
//                            public void onResponse(NetworkResponse response) {
//                                JSONObject obj = null;
//                                try {
//                                    obj = new JSONObject(response.toString());
//                                    Log.d("","response_body"+  response.toString());
//                                    JSONArray dataObj = obj.optJSONArray("data");
//
//                                    if (obj.optString("status").compareTo("success") == 0) {
//                                        Intent intent = new Intent(AddPost.this, HomeScreen.class);
//                                        Toast.makeText(AddPost.this, obj.getString("message"), Toast.LENGTH_LONG).show();
//                                        startActivity(intent);
//                                        progressDialog.dismiss();
//                                        finish();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                progressDialog.dismiss();
//                                NetworkResponse networkResponse = error.networkResponse;
//                                String errorMessage = "Unknown error";
//                                if (networkResponse == null) {
//                                    if (error.getClass().equals(TimeoutError.class)) {
//                                        errorMessage = "Request timeout";
//                                    } else if (error.getClass().equals(NoConnectionError.class)) {
//                                        errorMessage = "Failed to connect server";
//                                    }
//                                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
//                                } else {
//                                    String result = new String(networkResponse.data);
//                                    try {
//                                        JSONObject response = new JSONObject(result);
//                                        JSONObject errorObje = response.getJSONObject("error");
//                                        String status = errorObje.getString("statusCode");
//                                        String message = errorObje.getString("message");
//                                        Log.e("Error Status", status);
//                                        Log.e("Error Message", message);
//                                        errorMessage = message;
//                                        if (networkResponse.statusCode == 404) {
//                                            errorMessage = "Resource not found";
//                                        } else if (networkResponse.statusCode == 401) {
//                                            errorMessage = message + " Please login again";
//                                        } else if (networkResponse.statusCode == 400) {
//                                            errorMessage = message + " Check your inputs";
//                                        } else if (networkResponse.statusCode == 500) {
//                                            errorMessage = message + " Something is getting wrong";
//                                        }
//                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
////                        Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();
//                                Log.i("Error", errorMessage);
//                                error.printStackTrace();
//                            }
//                        }) {
//
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("request",resqObj.toString());
//                        return params;
//                    }
//
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        HashMap<String, String> header_param = new HashMap<>();
////                header_param.put("Authorization",SharedPrefManager.getInstance(context).getRegPeopleId());
////                header_param.put("ln",SharedPrefManager.getLangId(Signup.this,RequestCode.LangId));
//                        return header_param;
//                    }
//
//
//                    @Override
//                    protected Map<String, DataPart> getByteData() {
//                        Map<String, DataPart> params = new HashMap<>();
//                        long imagename = System.currentTimeMillis();
//                        for (int i = 0; i < arrayList.size(); i++) {
//                            File file = null;
//                            file = new File(String.valueOf(arrayList.get(i).getPath()));
//
//                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),  file);
//                            filePartmultipleImages = MultipartBody.Part.createFormData("attachment[]", file.getName(), requestBody);
//                            photos.add(filePartmultipleImages);
//                        }
//                        params.put("attachment[]", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
//                        return params;
//                    }
//                };
//
//                //adding the request to volley
//                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        60000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                Volley.newRequestQueue(this).add(volleyMultipartRequest);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }

}
