package com.anokbook.Activites;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.R;

public class Request extends AppCompatActivity {

    @BindView(R.id.received_tv)
    TextView received_tv;
    @BindView(R.id.sent_tv)
    TextView sent_tv;
    @BindView(R.id.request_for_book_tv)
    TextView request_for_book_tv;
    @BindView(R.id.toolbar_profile_list)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this,this);
        setSupportActionBar(toolbar);
        setTitle("Requests");

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        sent_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Request.this,Given.class));
            }
        });
        received_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Request.this,ReceivedRequest.class));
            }
        });
        request_for_book_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Request.this,RequestForABook.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(request_for_book_tv,"Toolbar_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Request.this,pairs);
                startActivity(intent,options.toBundle());
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
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
}
