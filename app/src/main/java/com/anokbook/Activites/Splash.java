package com.anokbook.Activites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.R;
import io.fabric.sdk.android.Fabric;

public class Splash extends AppCompatActivity {
    private static String TAG = "Splash";
    private Boolean mLocationPermissionsGranted = false;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
//        Crashlytics.getInstance().crash();
        setContentView(R.layout.activity_splash);

        if(getPermission()){
            initApp();
        }

    }

    private boolean getPermission(){
        Log.d(TAG, "get permissions method");
        String[] permissions ={Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_CALL_LOG,
//                Manifest.permission.RECORD_AUDIO
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {

                Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = false");
                ActivityCompat.requestPermissions(this, permissions, Constrants.LOCATION_PERMISSION_REQUEST_CODE);
                // Permission Denied
                Toast.makeText(Splash.this, "Some Permission is Denied, please allow permission for that the app can work.", Toast.LENGTH_SHORT)
                        .show();
            }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Splash.this, "Some Permission is Denied, please allow permission for that the app can work.", Toast.LENGTH_SHORT)
                        .show();
            }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Splash.this, "Some Permission is Denied, please allow permission for that the app can work.", Toast.LENGTH_SHORT)
                        .show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions, Constrants.LOCATION_PERMISSION_REQUEST_CODE);
                Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = false");
            }
        }
        else
        {
            mLocationPermissionsGranted = true;
            Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = true");
        }
        return mLocationPermissionsGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case Constrants.LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            initApp();
//                            finish();
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    initApp();
                }
            }
        }
    }
    private void initApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (SharedPrefManager.getInstance(Splash.this).getIsLoggedIn(false)) {
                    intent = new Intent(Splash.this, HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    intent = new Intent(Splash.this, IntroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2500);
    }
}
