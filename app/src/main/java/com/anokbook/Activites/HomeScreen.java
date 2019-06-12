package com.anokbook.Activites;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anokbook.Adapters.HomeBookListAdapter;
import com.anokbook.Adapters.ShowingCategoriesAdapter;
import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.MainActivity;
import com.anokbook.Models.HomeBookListModel;
import com.anokbook.R;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreen extends AppCompatActivity implements WebCompleteTask {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    public static Boolean gridview, listview;
    public static HomeScreen mInstance;
    static HomeBookListAdapter homeBookListAdapter;
    static ArrayList<HomeBookListModel> homeBookListAdapterArrayList = new ArrayList<>();
    private static String TAG = "HomeScreen";
    final Context context = this;
    public Boolean up = false, down = false;

    String dist_string = "", order_status_string = "", sort_by_string = "", cat_id_string = "", min_price_string = "", max_price_string = "";

    @BindView(R.id.search_tv)
    TextView search_tv;
    @BindView(R.id.current_dis)
    ImageView current_dis;
    @BindView(R.id.km_5)
    TextView km_5;
    @BindView(R.id.km_10)
    TextView km_10;
    @BindView(R.id.km_15)
    TextView km_15;
    @BindView(R.id.km_20)
    TextView km_20;
    @BindView(R.id.km_25)
    TextView km_25;
    @BindView(R.id.list_view_img)
    ImageView list_view_img;
    @BindView(R.id.grid_view_img)
    ImageView grid_view_img;
    @BindView(R.id.spinner_sorting)
    Spinner spinner;
    @BindView(R.id.book_recyclerview)
    RecyclerView book_recyclerview;
    @BindView(R.id.bottom_appbar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.setting_rel)
    RelativeLayout setting;
    @BindView(R.id.home_rel)
    RelativeLayout home;
    @BindView(R.id.profile_rel)
    RelativeLayout profile;
    @BindView(R.id.my_ads_rel)
    RelativeLayout about;
    @BindView(R.id.FloatingActionButtonAddEmp)
    FloatingActionButton floatingActionButtonAddEmp;
    @BindView(R.id.filter_icon)
    ImageView filter_icon;
    @BindView(R.id.search_icon)
    ImageView search_icon;
    @BindView(R.id.top_action_bar)
    RelativeLayout top_action_bar;
    @BindView(R.id.km_layout)
    RelativeLayout km_layout;
    @BindView(R.id.view_type)
    RelativeLayout view_type;
    @BindView(R.id.bottom_rel)
    RelativeLayout bottom_rel;
    @BindView(R.id.notification_icon)
    ImageView notification_icon;
    @BindView(R.id.prof_tv)
    TextView prof_tv;
    @BindView(R.id.myads_tv)
    TextView myads_tv;
    @BindView(R.id.setting_tv)
    TextView setting_tv;
    CrystalSeekbar rangeSeekbar4;
    TextView distance_min_tv;
    TextView distance_max_tv;
    CrystalRangeSeekbar rangeSeekbar2;
    TextView min_price_tv;
    TextView max_price_tv;
    ImageView cross_icon;
    LocationManager locationManager;
    @BindView(R.id.empty_lin)
    LinearLayout empty_lin;
    @BindView(R.id.cur_rel)
    RelativeLayout cur_rel;
    @BindView(R.id.search_et)
    AutoCompleteTextView search_et;
    @BindView(R.id.clear_icon)
    ImageView clear_icon;
    @BindView(R.id.search_submit)
    ImageView search_submit;
    @BindView(R.id.search_lay)
    RelativeLayout search_lay;
    @BindView(R.id.all_cate)
    TextView all_cate;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> cat_id_array = new ArrayList<>();
    ArrayList<String> cat_name_array = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Spinner filter_spinner;

    public static HomeScreen getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this, this);

        mInstance = this;
        listview = true;
        gridview = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        empty_lin.setVisibility(View.VISIBLE);
        book_recyclerview.setVisibility(View.GONE);
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Nearbybooks.class));
            }
        });

        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Notification.class));
            }
        });

        if (ShowingCategoriesAdapter.select_cat != null) {
            if (getIntent().getExtras() != null) {
                search_tv.setText(getIntent().getExtras().getString("Adrress", ""));
                all_cate.setText(ShowingCategoriesAdapter.select_cat);
                cat_id_string = getIntent().getExtras().getString("Cat_id", "");
                All_Book_List_Method();
            }
        } else {
            getLocation();
        }
        km_5.setBackground(null);
        km_10.setBackground(null);
        km_15.setBackground(null);
        km_20.setBackground(null);
        km_25.setBackground(null);
        current_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km_5.setBackground(null);
                km_10.setBackground(null);
                km_15.setBackground(null);
                km_20.setBackground(null);
                km_25.setBackground(null);
                km_5.setTextColor(getResources().getColor(R.color.black));
                km_10.setTextColor(getResources().getColor(R.color.black));
                km_15.setTextColor(getResources().getColor(R.color.black));
                km_20.setTextColor(getResources().getColor(R.color.black));
                km_25.setTextColor(getResources().getColor(R.color.black));
                dist_string = "";
                getLocation();
            }
        });
        km_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km_5.setBackground(getResources().getDrawable(R.drawable.select_km_back));
                km_10.setBackground(null);
                km_15.setBackground(null);
                km_20.setBackground(null);
                km_25.setBackground(null);

                dist_string = "5";
                km_5.setTextColor(getResources().getColor(R.color.white));
                km_10.setTextColor(getResources().getColor(R.color.black));
                km_15.setTextColor(getResources().getColor(R.color.black));
                km_20.setTextColor(getResources().getColor(R.color.black));
                km_25.setTextColor(getResources().getColor(R.color.black));
                All_Book_List_Method();
            }
        });
        km_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km_5.setBackground(null);
                km_10.setBackground(getResources().getDrawable(R.drawable.select_km_back));
                km_15.setBackground(null);
                km_20.setBackground(null);
                km_25.setBackground(null);
                dist_string = "10";
                km_5.setTextColor(getResources().getColor(R.color.black));
                km_10.setTextColor(getResources().getColor(R.color.white));
                km_15.setTextColor(getResources().getColor(R.color.black));
                km_20.setTextColor(getResources().getColor(R.color.black));
                km_25.setTextColor(getResources().getColor(R.color.black));
                All_Book_List_Method();
            }
        });
        km_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km_5.setBackground(null);
                km_10.setBackground(null);
                km_15.setBackground(getResources().getDrawable(R.drawable.select_km_back));
                km_20.setBackground(null);
                km_25.setBackground(null);
                dist_string = "15";
                km_5.setTextColor(getResources().getColor(R.color.black));
                km_10.setTextColor(getResources().getColor(R.color.black));
                km_15.setTextColor(getResources().getColor(R.color.white));
                km_20.setTextColor(getResources().getColor(R.color.black));
                km_25.setTextColor(getResources().getColor(R.color.black));
                All_Book_List_Method();
            }
        });
        km_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km_5.setBackground(null);
                km_10.setBackground(null);
                km_15.setBackground(null);
                km_20.setBackground(getResources().getDrawable(R.drawable.select_km_back));
                km_25.setBackground(null);
                dist_string = "20";
                km_5.setTextColor(getResources().getColor(R.color.black));
                km_10.setTextColor(getResources().getColor(R.color.black));
                km_15.setTextColor(getResources().getColor(R.color.black));
                km_20.setTextColor(getResources().getColor(R.color.white));
                km_25.setTextColor(getResources().getColor(R.color.black));
                All_Book_List_Method();
            }
        });
        km_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km_5.setBackground(null);
                km_10.setBackground(null);
                km_15.setBackground(null);
                km_20.setBackground(null);
                km_25.setBackground(getResources().getDrawable(R.drawable.select_km_back));
                dist_string = "25";
                km_5.setTextColor(getResources().getColor(R.color.black));
                km_10.setTextColor(getResources().getColor(R.color.black));
                km_15.setTextColor(getResources().getColor(R.color.black));
                km_20.setTextColor(getResources().getColor(R.color.black));
                km_25.setTextColor(getResources().getColor(R.color.white));
                All_Book_List_Method();
            }
        });

        book_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        homeBookListAdapter = new HomeBookListAdapter(homeBookListAdapterArrayList, HomeScreen.this);
        book_recyclerview.setAdapter(homeBookListAdapter);
        list_view_img.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        grid_view_img.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.light_grey), PorterDuff.Mode.SRC_IN);
        list_view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview = true;
                gridview = false;
                list_view_img.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                grid_view_img.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.light_grey), PorterDuff.Mode.SRC_IN);
                book_recyclerview.setLayoutManager(new LinearLayoutManager(HomeScreen.this, LinearLayoutManager.VERTICAL, false));
                homeBookListAdapter = new HomeBookListAdapter(homeBookListAdapterArrayList, HomeScreen.this);
                book_recyclerview.setAdapter(homeBookListAdapter);
                All_Book_List_Method_without_pRo();
            }
        });
        grid_view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview = false;
                gridview = true;
                list_view_img.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.light_grey), PorterDuff.Mode.SRC_IN);
                grid_view_img.setColorFilter(ContextCompat.getColor(HomeScreen.this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                book_recyclerview.setLayoutManager(new GridLayoutManager(HomeScreen.this, 2));
                homeBookListAdapter = new HomeBookListAdapter(homeBookListAdapterArrayList, HomeScreen.this);
                book_recyclerview.setAdapter(homeBookListAdapter);
                All_Book_List_Method_without_pRo();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinnner_layout) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
//                    ((TextView)v.findViewById(android.R.id.text1)).setText("Sort By");
//                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        adapter.setDropDownViewResource(R.layout.spinnner_layout);
        adapter.add("Low to High");
        adapter.add("High to Low");
        adapter.add("Near to Far");
        adapter.add("Sort by");
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                final String item = (String) spinner.getItemAtPosition(position);
                if (position == 3) {
                    return;
                } else {
                    if (position == 0) {
                        sort_by_string = "price";
                        order_status_string = "1";
                    } else if (position == 1) {
                        sort_by_string = "price";
                        order_status_string = "2";
                    } else {
                        sort_by_string = "";
                        order_status_string = "";
                    }
                    All_Book_List_Method();
//                    Toast.makeText(HomeScreen.this, item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });

        floatingActionButtonAddEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, MainActivity.class));
                finish();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, Request.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(setting_tv, "toolbar_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeScreen.this, pairs);
                startActivity(intent, options.toBundle());
//                Toast.makeText(HomeScreen.this,"Setting",Toast.LENGTH_SHORT).show();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(HomeScreen.this,"Home",Toast.LENGTH_SHORT).show();
                ShowingCategoriesAdapter.select_cat = null;
                ShowingCategoriesAdapter.select_cat_id = null;
                sort_by_string = "";
                order_status_string = "";
                getLocation();
//                Intent intent = new Intent(HomeScreen.this, HomeScreen.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                finish();
//                startActivity(intent);
//                recreate();

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, MyAds.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(myads_tv, "toolbar_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeScreen.this, pairs);
                startActivity(intent, options.toBundle());

//                finish();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, ProfileList.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(prof_tv, "toolbar_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeScreen.this, pairs);
                startActivity(intent, options.toBundle());
//                Toast.makeText(HomeScreen.this,"Profile",Toast.LENGTH_SHORT).show();
            }
        });
        filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popupmethod();
            }
        });

        cur_rel.setVisibility(View.VISIBLE);
        search_lay.setVisibility(View.GONE);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_rel.setVisibility(View.GONE);
                search_lay.setVisibility(View.VISIBLE);
            }
        });
        search_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(search_et.getText().toString().trim())) {
//                    all_cate.setText(getString(R.string.all_categories));
                } else {
                    all_cate.setText(search_et.getText().toString().trim());
                }
                cur_rel.setVisibility(View.VISIBLE);
                search_lay.setVisibility(View.GONE);
            }
        });

        if (TextUtils.isEmpty(search_et.getText().toString())) {
            clear_icon.setVisibility(View.GONE);
        } else {
            Selection.setSelection(search_et.getText(), search_et.getText().toString().length());
            clear_icon.setVisibility(View.VISIBLE);
        }
//        search_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (TextUtils.isEmpty(search_et.getText().toString())){
//                    clear_icon.setVisibility(View.GONE);
//                }else {
//                    clear_icon.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        clear_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText("");
                clear_icon.setVisibility(View.GONE);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                All_Book_List_Method();
            }
        });
        //-----------------------------------------Searching prduct-------------------------------------
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(search_et.getText().toString())) {
                    clear_icon.setVisibility(View.GONE);
                } else {
                    clear_icon.setVisibility(View.VISIBLE);
                }
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                homeBookListAdapter.getFilter().filter(search_et.getText());
//                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
//-----------------------------------------Searching prduct-------------------------------------

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LocationPermission();
//        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        } else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Location> task) {
                                                   if (task.isSuccessful()) {
                                                       Location currentLocation = (Location) task.getResult();
                                                       if (currentLocation != null) {
                                                           Nearbybooks.latti = currentLocation.getLatitude();
                                                           Nearbybooks.longi = currentLocation.getLongitude();

                                                           Geocoder geocoder = new Geocoder(HomeScreen.this, Locale.getDefault());
                                                           try {
                                                               List<Address> addresses = geocoder.getFromLocation(Nearbybooks.latti, Nearbybooks.longi, 1);
                                                               Address addressobj = addresses.get(0);
//                                                                addressobj.getAddressLine(0);
                                                               search_tv.setText(addressobj.getAddressLine(0));
                                                               All_Book_List_Method();
                                                           } catch (IOException e) {
                                                               e.printStackTrace();
                                                           }
                                                       }
                                                   } else {
                                                       search_tv.setText("Unable to find current location . Try again later");
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
            case LOCATION_PERMISSION_REQUEST_CODE:
//                getLocation();
                break;
        }
    }

    public void LocationPermission() {
        //        LocationServiceOn_Off();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            showSettingsAlert();
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                Log.i("About GPS", "GPS is Enabled in your devide");
//                getLocation();
                // Toast.makeText(ctx,"enable",Toast.LENGTH_SHORT).show();
            } else {
                //showAlert
                showSettingsAlert();
//            context.startActivity(new Intent(context, DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.gps_setting);
        // Setting Dialog Message
        alertDialog.setMessage(R.string.gps_setting_menu);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    public void Popupmethod() {
        final Dialog dialog = new Dialog(this, R.style.MyTheme);
        dialog.setContentView(R.layout.activity_fliter_popup);
        CategoriesListMethod();
        filter_spinner = (Spinner) dialog.findViewById(R.id.filter_spinner);
        Button applybtn = (Button) dialog.findViewById(R.id.apply_btn);
        rangeSeekbar4 = (CrystalSeekbar) dialog.findViewById(R.id.rangeSeekbar4);
        distance_min_tv = (TextView) dialog.findViewById(R.id.distance_min_tv);
        distance_max_tv = (TextView) dialog.findViewById(R.id.distance_max_tv);

        rangeSeekbar2 = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar2);
        min_price_tv = (TextView) dialog.findViewById(R.id.min_price_tv);
        max_price_tv = (TextView) dialog.findViewById(R.id.max_price_tv);
        cross_icon = (ImageView) dialog.findViewById(R.id.cross_icon);

        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rangeSeekbar4.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distance_min_tv.setText("0 km");
                dist_string = value + "";
                distance_max_tv.setText(value + " km");
            }
        });
        rangeSeekbar2.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                min_price_string = minValue.toString();
                max_price_string = maxValue.toString();
                min_price_tv.setText("Rs " + minValue);
                max_price_tv.setText("Rs " + maxValue);
            }
        });

        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) filter_spinner.getItemAtPosition(position);
                if (cat_id_array != null && cat_id_array.size() != 0)
                    cat_id_string = cat_id_array.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km_5.setBackground(null);
                km_10.setBackground(null);
                km_15.setBackground(null);
                km_20.setBackground(null);
                km_25.setBackground(null);
                km_5.setTextColor(getResources().getColor(R.color.black));
                km_10.setTextColor(getResources().getColor(R.color.black));
                km_15.setTextColor(getResources().getColor(R.color.black));
                km_20.setTextColor(getResources().getColor(R.color.black));
                km_25.setTextColor(getResources().getColor(R.color.black));
                All_Book_List_Method();
                dialog.dismiss();
//                Toast.makeText(context,dist_string+min_price_string+max_price_string+cat_id_string+"Apply",Toast.LENGTH_SHORT).show();
            }
        });


        // show it
        dialog.show();
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
            new WebTask(HomeScreen.this, WebUrls.BASE_URL, objectNew, HomeScreen.this, RequestCode.CODE_Category_List, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void All_Book_List_Method_without_pRo() {
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            dataObj.put("lat", Nearbybooks.latti);
            dataObj.put("long", Nearbybooks.longi);
            dataObj.put("distance", dist_string);
            dataObj.put("cat_id", cat_id_string);
            dataObj.put("min_price", min_price_string);
            dataObj.put("max_price", max_price_string);
            dataObj.put("order_status", order_status_string);
            dataObj.put("sort_by", sort_by_string);


            JSONObject resqObj = new JSONObject();
            resqObj.put("method", "book_list_all");
            resqObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", resqObj.toString());
            Log.d("book_list_all_data", objectNew.toString());
            new WebTask(HomeScreen.this, WebUrls.BASE_URL, objectNew, HomeScreen.this, RequestCode.CODE_Book_List_All, 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void All_Book_List_Method() {
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            dataObj.put("lat", Nearbybooks.latti);
            dataObj.put("long", Nearbybooks.longi);
            dataObj.put("distance", dist_string);
            dataObj.put("cat_id", cat_id_string);
            dataObj.put("min_price", min_price_string);
            dataObj.put("max_price", max_price_string);
            dataObj.put("order_status", order_status_string);
            dataObj.put("sort_by", sort_by_string);

            JSONObject resqObj = new JSONObject();
            resqObj.put("method", "book_list_all");
            resqObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", resqObj.toString());
            Log.d("book_list_all_data", objectNew.toString());
            new WebTask(HomeScreen.this, WebUrls.BASE_URL, objectNew, HomeScreen.this, RequestCode.CODE_Book_List_All, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("book_list_all_res", response);
        if (RequestCode.CODE_Book_List_All == taskcode) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null && jsonObject.optString("status").compareTo("success") == 0) {
                    homeBookListAdapterArrayList.clear();
                    JSONArray dataarray = jsonObject.optJSONArray("data");
                    if (dataarray != null && dataarray.length() > 0) {
                        empty_lin.setVisibility(View.GONE);
                        book_recyclerview.setVisibility(View.VISIBLE);
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject dataobj = dataarray.getJSONObject(i);
                            HomeBookListModel homeBookListModel = new HomeBookListModel();
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
                            homeBookListModel.setCat_name(dataobj.optString("cat_name"));
                            homeBookListModel.setDistance(dataobj.optDouble("distance"));
                            if (dataobj.optString("user_like_status") != null)
                                homeBookListModel.setUser_like_status(dataobj.optString("user_like_status"));

                            String date = dataobj.optString("created_at");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date newdate = simpleDateFormat.parse(date);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                            homeBookListModel.setCreated_at(dateFormat.format(newdate) + "");

                            homeBookListModel.setUpdated_at(dataobj.optString("updated_at"));

                            JSONArray attachmentArray = dataobj.optJSONArray("attachment");
                            if (attachmentArray != null) {
                                for (int j = 0; j < attachmentArray.length(); j++) {
                                    JSONObject attObj = attachmentArray.getJSONObject(j);
                                    homeBookListModel.setBook_imag(attachmentArray.getJSONObject(0).optString("name"));
                                    homeBookListModel.setAttachment_id(attObj.optString("id"));
                                    homeBookListModel.setAttachment_type(attObj.optString("type"));
                                    homeBookListModel.setAttachment_name(attObj.optString("name"));
                                }
                            }
                            homeBookListAdapterArrayList.add(homeBookListModel);
                        }
//                        homeBookListAdapter.setOnItemClickListener(HomeScreen.this);
                        homeBookListAdapter.notifyDataSetChanged();
                    } else {
                        empty_lin.setVisibility(View.VISIBLE);
                        book_recyclerview.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(HomeScreen.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Category_List == taskcode) {
            Log.d("category_list_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null && jsonObject.optString("status").compareTo("success") == 0) {
                    cat_id_array.clear();
                    cat_name_array.clear();
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.optJSONObject(i);
                        cat_id_array.add(dataObj.optString("id"));
                        cat_name_array.add(dataObj.optString("cat_name"));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat_name_array);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filter_spinner.setAdapter(arrayAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void runLayoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.item_animation_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    //    @Override
//    public void onItemClick(int position,View view) {
////        HomeBookListModel homeBookListModel = homeBookListModelArrayList.get(posi);
//        Intent intent = new Intent(context, IdoDetail.class);
//        intent.putExtra("book_id",homeBookListAdapterArrayList.get(position).getId());
//        intent.putExtra("user_id",homeBookListAdapterArrayList.get(position).getUser_id());
//        context.startActivity(intent);
//    }
    public void onCdsfaick(View clickedView) {
        // save rect of view in screen coordinates
        final Rect viewRect = new Rect();
        clickedView.getGlobalVisibleRect(viewRect);

        // create Explode transition with epicenter
        Explode explode = new Explode();
        explode.setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(Transition transition) {
                return viewRect;
            }
        });
        explode.setDuration(1000);
        TransitionManager.beginDelayedTransition(book_recyclerview, explode);

        // remove all views from Recycler View
        book_recyclerview.setVisibility(View.GONE);
    }
}
