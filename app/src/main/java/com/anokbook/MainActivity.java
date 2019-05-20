package com.anokbook;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public RequestManager mGlideRequestManager;
    ImageView iv_image;
    ArrayList<Uri> selectedUriList;
    Uri selectedUri;
    private ViewGroup mSelectedImagesContainer;
    private LinearLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (LinearLayout) findViewById(R.id.main_layout);
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Clickedd", Toast.LENGTH_SHORT).show();
            }
        });

        mGlideRequestManager = Glide.with(this);

        iv_image = (ImageView) findViewById(R.id.iv_image);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

//        setSingleShowButton();
        setMultiShowButton();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //    private void setSingleShowButton() {
//
//
//        Button btn_single_show = (Button) findViewById(R.id.btn_single_show);
//        btn_single_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PermissionListener permissionlistener = new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        Intent intent = new Intent(getApplicationContext(), ImagePickerDemo.class);
//                        intent.putExtra("picker", "single");
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//                        Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//
//                };
//
//                TedPermission.with(MainActivity.this)
//                        .setPermissionListener(permissionlistener)
//                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .check();
//
//            }
//        });
//    }

    private void setMultiShowButton() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(getApplicationContext(), ImagePickerDemo.class);
                intent.putExtra("picker", "multi");
                startActivity(intent);
                finish();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

//        Button btn_multi_show = (Button) findViewById(R.id.btn_multi_show);
//        btn_multi_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                PermissionListener permissionlistener = new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        Intent intent = new Intent(getApplicationContext(), ImagePickerDemo.class);
//                        intent.putExtra("picker", "multi");
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//                        Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//
//                };
//
//                TedPermission.with(MainActivity.this)
//                        .setPermissionListener(permissionlistener)
//                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .check();
//
//            }
//        });

    }


    private void showUriList(ArrayList<Uri> uriList) {
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();

        iv_image.setVisibility(View.GONE);
        mSelectedImagesContainer.setVisibility(View.VISIBLE);

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


        for (Uri uri : uriList) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            Glide.with(this)
                    .load(uri.toString())
//                    .fitCenter()
                    .into(thumbnail);

            mSelectedImagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));


        }

    }
}
