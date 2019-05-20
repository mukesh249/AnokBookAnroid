package com.anokbook.Activites;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.Adapters.NotificationAdapter;
import com.anokbook.Models.NotificationModel;
import com.anokbook.R;

public class Notification extends AppCompatActivity {

    @BindView(R.id.profile_edit_toobar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_notification)
    RecyclerView recycler_view_notification;

    NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ButterKnife.bind(this,this);

        setSupportActionBar(toolbar);
        setTitle("Notification");
        for (int i =0;i<10;i++){
            NotificationModel notificationModel = new NotificationModel();
            notificationModelArrayList.add(notificationModel);
        }

        recycler_view_notification.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        notificationAdapter = new NotificationAdapter(notificationModelArrayList,Notification.this);
        recycler_view_notification.setAdapter(notificationAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
}
