package com.anokbook.Activites;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.anokbook.R;
import com.kv.retrorequestlib.RetroRequest;
import com.kv.retrorequestlib.helper.ResponseDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendMonyeOnBank extends AppCompatActivity implements ResponseDelegate {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        MyApplication.getInstance().StatusBarColor(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_monye_on_bank);

        ButterKnife.bind(this, this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void serverRequestForSendMoneyToBank() {
        RetroRequest request = new RetroRequest(this, this);
        request.setBaseUrl("http://escortapp.co.in/");//set base_url of api
        request.setPath1("anokbook");//set path
        request.setPath2("api");
        request.setPath3("apiname");
        request.setPath4("");//optional
        //request.addHeader("Authorization", " ");// optional
        request.setRequestMethod(RetroRequest.REQUEST_METHOD_GET);//set the type of api {GET, POST, DELETE, PUT} Default GET
        request.setTag(ResponseType.SIGNUP);//for use multiple request in same activity  set unique tag
        request.putQuery("example", "1234");//for put query and body for send data use as key value pair -> &user_id=1234
        //request.putFile("file1", new File(filepath));//for send file,   only work with POST and PUT api
        //request.setShowRetrySnack(true);//Default false - for show Retry Snackbar on No Internet Connection.
        //request.setShowToast(true);//Default true  - for show Toast on Failure, No Internet
        request.execute(true);//for execute the request
    }*/

    @Override
    public void onSuccess(int tag, String response) {

    }

    @Override
    public void onNoNetwork(int tag, String message) {

    }

    @Override
    public void onFailure(int tag, int response_code, String message) {

    }
}
