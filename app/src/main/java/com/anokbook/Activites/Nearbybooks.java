package com.anokbook.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Adapters.PlaceAutocompleteAdapter;
import com.anokbook.Adapters.ShowingCategoriesAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Models.PlaceInfo;
import com.anokbook.Models.ShowingCategoriesModel;
import com.anokbook.R;

public class Nearbybooks extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener , WebCompleteTask {

    @BindView(R.id.showing_categories_recyclerView) RecyclerView showing_categories_recyclerView;
    @BindView(R.id.back_btn) ImageView back_btn;
    @BindView(R.id.current_loction_rel) RelativeLayout current_loction_rel;
    @BindView(R.id.search_et) AutoCompleteTextView search_et;
    @BindView(R.id.clear_icon) ImageView clear_icon;
    @BindView(R.id.current_icon) ImageView current_icon;
    @BindView(R.id.loca_cat_tv) TextView search_submit;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERSMISSION_REQUEST_CODE =3;

    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    private final static String TAG = "Nearbybooks";

    ShowingCategoriesAdapter showingCategoriesAdapter;
    ArrayList<ShowingCategoriesModel> showingCategoriesModelArrayList = new ArrayList<>();
    public static double latti,longi;
    LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearbybooks);
        ButterKnife.bind(this,this);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        showing_categories_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        current_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        if (TextUtils.isEmpty(search_et.getText().toString())){
            clear_icon.setVisibility(View.GONE);
        }else {
            Selection.setSelection(search_et.getText(),search_et.getText().toString().length());
            clear_icon.setVisibility(View.VISIBLE);
        }
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(search_et.getText().toString())){
                    clear_icon.setVisibility(View.GONE);
                }else {
                    clear_icon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clear_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText("");
                clear_icon.setVisibility(View.GONE);
            }
        });
        search_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(search_et.getText().toString())){
                    search_et.setError(getString(R.string.enter_address));
                    search_et.requestFocus();
                }else if (latlng!=null) {
                    Intent intent = new Intent(Nearbybooks.this, HomeScreen.class);
                    intent.putExtra("Adrress", search_et.getText().toString().trim());
                    intent.putExtra("Cat_id", ShowingCategoriesAdapter.select_cat_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        CategoriesListMethod();
        init();
    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERSMISSION_REQUEST_CODE);

        } else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener<Location>()
                                           {
                                               @Override
                                               public void onComplete(@NonNull Task<Location> task) {
                                                   if (task.isSuccessful()){
                                                       Location currentLocation = (Location)task.getResult();
                                                       if (currentLocation!=null){
                                                            latti = currentLocation.getLatitude();
                                                            longi = currentLocation.getLongitude();

                                                           Geocoder geocoder = new Geocoder(Nearbybooks.this, Locale.getDefault());
                                                           try{
                                                               List<Address> addresses = geocoder.getFromLocation(latti,longi,1);
                                                               Address addressobj = addresses.get(0);
//                                                                addressobj.getAddressLine(0);
                                                               String add = addressobj.getAddressLine(0);
                                                               search_et.setText(add);
                                                               search_et.setSelection(add.length());
                                                           }catch (IOException e){
                                                               e.printStackTrace();
                                                           }
                                                       }
                                                   }else {
                                                       search_et.setText("Unable to find current location . Try again later");
                                                   }
                                               }
                                           }
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERSMISSION_REQUEST_CODE:
                getLocation();
                break;
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

        search_et.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,
                LAT_LNG_BOUNDS,null);
        search_et.setAdapter(mPlaceAutocompleteAdapter);

        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        String searchString = search_et.getText().toString();
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

            latlng = mPlace.getLatLng();
            latti = latlng.latitude;
            longi = latlng.longitude;
//            lat = String.valueOf(latti);
//            lng = String.valueOf(longi);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            new WebTask(Nearbybooks.this, WebUrls.BASE_URL,objectNew,Nearbybooks.this, RequestCode.CODE_Category_List,1);
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
                        ShowingCategoriesModel showingCategoriesModel= new ShowingCategoriesModel();
                        showingCategoriesModel.setId(dataObj.optString("id"));
                        showingCategoriesModel.setName(dataObj.optString("cat_name"));
                        showingCategoriesModelArrayList.add(showingCategoriesModel);
                    }
                    showingCategoriesAdapter = new ShowingCategoriesAdapter(showingCategoriesModelArrayList,this);
                    showing_categories_recyclerView.setAdapter(showingCategoriesAdapter);

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
