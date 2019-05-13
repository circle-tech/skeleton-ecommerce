package com.circletech.skeletonecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProductInfoActivity extends AppCompatActivity {

    TextView textViewProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("PRODUCT_ID");

        textViewProductId = findViewById(R.id.activity_productinfo_textViewProductId);
        textViewProductId.setText(productId);
    }
}
