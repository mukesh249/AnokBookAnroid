package com.anokbook.Activites;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anokbook.Api.RequestCode;
import com.anokbook.Api.WebCompleteTask;
import com.anokbook.Api.WebTask;
import com.anokbook.Api.WebUrls;
import com.anokbook.Common.Constrants;
import com.anokbook.Common.SharedPrefManager;
import com.anokbook.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMoney extends AppCompatActivity implements PaymentResultListener, WebCompleteTask {

    @BindView(R.id.wallet_amout_tv)
    TextView wallet_amout_tv;
    @BindView(R.id.profile_edit_toobar)
    Toolbar toolbar;
    @BindView(R.id.submit_addmoney_btn)
    Button submit_addmoney_btn;
    @BindView(R.id.enter_ammount_et)
    EditText enter_amount_et;
    //    @BindView(R.id.spin_kit) SpinKitView spin_kit;
    int payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_50));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        ButterKnife.bind(this, this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Money");

        if (getIntent().getExtras() != null) {
            wallet_amout_tv.setText(getIntent().getExtras().getString("wallet_amount", ""));
        }
//        spin_kit.setVisibility(View.GONE);
        submit_addmoney_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(enter_amount_et.getText().toString().trim())) {
                    enter_amount_et.setError("Please Enter Amount");
                    enter_amount_et.requestFocus();
                } else {
//                    spin_kit.setVisibility(View.VISIBLE);
                    Paymethod();
                }
            }
        });

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

    public void Paymethod() {
        payment = Integer.parseInt(enter_amount_et.getText().toString());
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.logo);
        final Activity activity = this;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", "COEgtdR93RXnma");
            jsonObject.put("name", "ANOK BOOK");
            jsonObject.put("description", "#123456");
            jsonObject.put("currency", "INR");
            jsonObject.put("amount", payment * 100);
            Log.d("add_money_data", jsonObject + "");
            checkout.open(activity, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
//        spin_kit.setVisibility(View.GONE);
        Log.d("payment_success", s);
        AddMoneyToWallet(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
//        spin_kit.setVisibility(View.GONE);
        Log.d("payment_fail", s);
        Toast.makeText(this, "Your payment failed", Toast.LENGTH_SHORT).show();
    }

    public void AddMoneyToWallet(String payment_id) {
        try {
            JSONObject dataObj = new JSONObject();
            dataObj.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
            dataObj.put("payment_id", payment_id);
            dataObj.put("amount", payment + "");

            JSONObject requiredObj = new JSONObject();
            requiredObj.put("method", "api_addwallet");
            requiredObj.put("data", dataObj);

            HashMap objectNew = new HashMap();
            objectNew.put("request", requiredObj.toString());
            Log.d("addwallet_data", objectNew.toString());
            new WebTask(AddMoney.this, WebUrls.BASE_URL, objectNew, AddMoney.this, RequestCode.CODE_Add_Money_Wallet, 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Add_Money_Wallet == taskcode) {

            try {
                JSONObject object = new JSONObject(response);
                Log.d("addwallet_success", response);
                if (object.optString("status").compareTo("success") == 0) {
                    Toast.makeText(this, "Your Amount " + payment + " successful Add to your wallet", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
