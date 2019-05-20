package com.anokbook.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.anokbook.R;

public class SearchView extends AppCompatActivity{

    private final static String TAG = "SearchView";

    @BindView(R.id.back_btn) ImageView back_btn;
    @BindView(R.id.search_et) AutoCompleteTextView et_address;
    @BindView(R.id.clear_icon) ImageView clear_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        ButterKnife.bind(this,this);

        if (TextUtils.isEmpty(et_address.getText().toString())){
            clear_icon.setVisibility(View.GONE);
        }else {
            clear_icon.setVisibility(View.VISIBLE);
        }
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_address.getText().toString())){
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
                et_address.setText("");
                clear_icon.setVisibility(View.GONE);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
