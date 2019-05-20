package com.anokbook.Activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import butterknife.BindView;
import com.anokbook.R;

public class FliterPopup extends AppCompatActivity {

    @BindView(R.id.filter_spinner) Spinner filter_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fliter_popup);

    }
}
