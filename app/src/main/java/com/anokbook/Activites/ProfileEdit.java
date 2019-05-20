package com.anokbook.Activites;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.iceteck.silicompressorr.SiliCompressor;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import com.anokbook.Models.PlaceInfo;
import com.anokbook.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileEdit extends AppCompatActivity implements WebCompleteTask ,GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.edit_profile_pic) SelectableRoundedImageView edit_profile_pic;
    @BindView(R.id.profile_edit_toobar) Toolbar toolbar;
    @BindView(R.id.edit_profile_pic_icon) ImageView edit_profile_pic_icon;
    @BindView(R.id.pe_name_et) EditText pe_name_et;
    @BindView(R.id.pe_mobile_et) TextView pe_mobile_et;
    @BindView(R.id.pe_email_et) EditText pe_email_et;
    @BindView(R.id.pe_location_et) AutoCompleteTextView pe_location_et;
    @BindView(R.id.pe_street_et) EditText pe_street_et;
    @BindView(R.id.pe_landmark_et) EditText pe_landmark_et;
    @BindView(R.id.submit) Button submit;

    private int REQUEST_CAMERA_PERMISSION =1;
    String image_url;
    Uri imageUri;
    File fileImage, compressedImage,Profile_file;
    String strCompressedFileImage;
    Bitmap bitmap;

    private final static String TAG = "ProfileEdit";
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    double lat,lng;
    LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        ButterKnife.bind(this,this);

        setSupportActionBar(toolbar);
        setTitle("Edit Profile");

        //get bitmap of the image
        try {
            //getting bitmap object from uri
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                    Uri.parse(MyApplication.getUserPic(ProfileEdit.this,Constrants.UserPic)));
            //displaying selected image to imageview
            edit_profile_pic.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        edit_profile_pic_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChoose();
            }
        });

        edit_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChoose();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBitmap(bitmap);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        UserProfileMethod();
        init();
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
    public void ImageChoose(){
        final Dialog dialog = new Dialog(ProfileEdit.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_profile);
        dialog.show();
        LinearLayout txtGallery = (LinearLayout) dialog.findViewById(R.id.layoutGallery);
        LinearLayout txtCamera = (LinearLayout) dialog.findViewById(R.id.layoutCamera);
        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(ProfileEdit.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileEdit.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        selectCameraImage();
                        dialog.dismiss();
                    }
                } else {
                    selectCameraImage();
                    dialog.dismiss();
                }
            }
        });
        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(ProfileEdit.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileEdit.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    } else {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
//                startActivityForResult(i, 100);
                    }
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
                }
            }
        });
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
    private void selectCameraImage() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 300);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                System.out.println(numbers[0]);
                if (numbers[0].equals("image")) {
                    File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/anonkbook");

                    new ImageCompressionAsyncTask(ProfileEdit.this).execute(data.getData().toString(),
                            fileImage.getPath());
                }
            }
        }else if (requestCode == 300) {
            Uri selectedImageUri = null;
            String filePath = null;
            selectedImageUri = imageUri;

            if (selectedImageUri != null) {
                try {
                    String filemanagerstring = selectedImageUri.getPath();
                    String selectedImagePath = getPath(selectedImageUri);
                    if (selectedImagePath != null) {
                        filePath = selectedImagePath;

                    } else if (filemanagerstring != null) {
                        filePath = filemanagerstring;
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown path",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Internal error",
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }

            File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/anonkbook");
            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTask(ProfileEdit.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }
        }else {

        }

    }
    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public
        ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Utility.showProgress(Signup.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(ProfileEdit.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
//            Utility.close();
            if (s != null) {
                strCompressedFileImage = s;
                Profile_file = new File(s);
                Uri compressUri = Uri.fromFile(Profile_file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(ProfileEdit.this.getContentResolver(), compressUri);
//                    Log.e("rgwrbhfh",getLoginStatus+"");
//                    if (getLoginStatus.equals("customer"))
//                    {
                        edit_profile_pic.setImageBitmap(bitmap);
//                    }
//                    if (getLoginStatus.equals("labour")) {
//                        profilePicLabour.setImageBitmap(bitmap);
//                    }
//                    if (getLoginStatus.equals("groupAdmin")) {
//                        profilePicA.setImageBitmap(bitmap);
//                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }
    private void uploadBitmap(Bitmap bitmap) {
//        if (latlng!=null){
            if (bitmap==null){
                edit_profile_pic.buildDrawingCache();
                bitmap = edit_profile_pic.getDrawingCache();
                //create a file to write bitmap data
                Profile_file = new File(ProfileEdit.this.getCacheDir(), System.currentTimeMillis()+"");
                try {
                    Profile_file.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(Profile_file);
                    fos.write(bitmapdata);
//                        fos.flush();
//                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
//        HashMap objectNew = new HashMap();
            JSONObject resqObj = new JSONObject();
            try {
                JSONObject dataObj = new JSONObject();
                dataObj.put("user_id",SharedPrefManager.getUserID(Constrants.UserId));
                dataObj.put("email",pe_email_et.getText().toString().trim());
                dataObj.put("street",pe_street_et.getText().toString().trim());
                dataObj.put("first_name",pe_name_et.getText().toString().trim());
                dataObj.put("landmarks",pe_landmark_et.getText().toString().trim());
                dataObj.put("location",pe_location_et.getText().toString().trim());
                dataObj.put("lat",lat);
                dataObj.put("long",lng);

                resqObj.put("method","update_profile");
                resqObj.put("data",dataObj);

//            objectNew.put("request",resqObj.toString());
                Log.d("user_profile_data",dataObj.toString());
            }catch (JSONException e){
                e.printStackTrace();
            }
            String jsonObjData = String.valueOf(resqObj);
            RequestBody objdata = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObjData);


            MultipartBody.Part filePartmultipleImages,filePartmultipleImagesPro;
            RequestBody requestBodyProfile = RequestBody.create(MediaType.parse("image/*"),  Profile_file);
            filePartmultipleImagesPro = MultipartBody.Part.createFormData("profile_img", Profile_file.getName(), requestBodyProfile);

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<JsonObject> call = getResponse.PostContract(
                    filePartmultipleImagesPro,
                    objdata);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                        dismissProgressBar();
                    if (response.isSuccessful()) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.body().toString());
                            Log.d("","response_body"+  response.body().toString());

                            if (obj.optString("status").compareTo("success") == 0) {
                                try {
//                                Intent intent = new Intent(ProfileEdit.this, Login.class);
                                    Toast.makeText(ProfileEdit.this, obj.getString("message"), Toast.LENGTH_LONG).show();
//                                startActivity(intent);
                                    progressDialog.dismiss();
                                    finish();
                                } catch (Exception e) {
                                }
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileEdit.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            JSONObject responseJ = new JSONObject(response.errorBody().string());
                            System.out.println("error response " + responseJ.toString());
                            JSONObject errorObje = responseJ.getJSONObject("error");
                            String status = errorObje.getString("statusCode");
                            String message = errorObje.getString("message");
                            Log.e("Error Status", status);
                            Log.e("Error Message", message);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileEdit.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("fail response.." + t.toString());

                }
            });
//        }else {
//            Toast.makeText(ProfileEdit.this, "please select the location from google suggestion", Toast.LENGTH_LONG).show();
//        }
    }
    public void UserProfileMethod(){
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id",SharedPrefManager.getUserID(Constrants.UserId));

            JSONObject resqObj = new JSONObject();
            resqObj.put("method","user_profile");
            resqObj.put("data",dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request",resqObj.toString());
            Log.d("user_profile_data",objectNew.toString());
            new WebTask(ProfileEdit.this, WebUrls.BASE_URL,objectNew,ProfileEdit.this, RequestCode.CODE_UserProfile,1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("user_profile_res",response);
        if (RequestCode.CODE_UserProfile == taskcode){
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject!=null && jsonObject.optString("status").compareTo("success")==0){
                    JSONArray dataarray = jsonObject.optJSONArray("data");
                    JSONObject dataobj = dataarray.getJSONObject(0);
                    if (dataobj!=null){
                        pe_name_et.setText(dataobj.optString("name"));
                        pe_mobile_et.setText("+91-"+dataobj.optString("phone"));
                        pe_email_et.setText(dataobj.optString("email"));
                        pe_street_et.setText(dataobj.optString("street"));
                        pe_landmark_et.setText(dataobj.optString("landmarks"));
                        pe_location_et.setText(dataobj.optString("location"));
                        Picasso.with(this).load(WebUrls.IMG_BASE_URL+WebUrls.Image_Url+dataobj.optString("profile_img"))
                                .fit()
                                .placeholder(R.drawable.avatar).into(edit_profile_pic);
                    }

                }else {
                    Toast.makeText(ProfileEdit.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
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

        pe_location_et.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,
                LAT_LNG_BOUNDS,null);
        pe_location_et.setAdapter(mPlaceAutocompleteAdapter);

        pe_location_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        String searchString = pe_location_et.getText().toString();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
