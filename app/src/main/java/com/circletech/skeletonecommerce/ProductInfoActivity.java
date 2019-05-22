package com.circletech.skeletonecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductInfoActivity extends AppCompatActivity {

    TextView textViewProductId, textViewProductName, textViewProductDescription, textViewProductPrice, textViewProductQuantity;
    ImageView imageViewProductImage;

    RelativeLayout relativeLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);

        textViewProductId = findViewById(R.id.activity_productinfo_textViewProductId);
        textViewProductName = findViewById(R.id.activity_productinfo_textViewProductName);
        textViewProductDescription = findViewById(R.id.activity_productinfo_textViewProductDescription);
        textViewProductPrice = findViewById(R.id.activity_productinfo_textViewProductPrice);
        textViewProductQuantity = findViewById(R.id.activity_productinfo_textViewProductQuantity);

        imageViewProductImage = findViewById(R.id.activity_productinfo_imageViewProductImage);

        relativeLayout = findViewById(R.id.activity_productinfo_relativeLayout);

        progressBar = findViewById(R.id.activity_productinfo_progressBar);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("PRODUCT_ID");

        getProductInfo(productId);
    }

    public void getProductInfo(final String productId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.URL_GET_PRODUCT_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponseObj = new JSONObject(response);
                    if (!jsonResponseObj.getBoolean("error")) {

                        JSONArray jsonProductArray = jsonResponseObj.getJSONArray("product");
                        JSONObject jsonProductInfo = jsonProductArray.getJSONObject(0);

                        textViewProductId.setText(String.valueOf(jsonProductInfo.getInt("productId")));
                        textViewProductName.setText(jsonProductInfo.getString("productName"));
                        textViewProductDescription.setText(jsonProductInfo.getString("productDescription"));
                        textViewProductPrice.setText(getApplication().getResources().getString(R.string.activity_productinfo_productprice, jsonProductInfo.getDouble("productPrice")));
                        textViewProductQuantity.setText(getApplicationContext().getResources().getString(R.string.activity_productinfo_productquantity, jsonProductInfo.getInt("productQuantity")));

                        Glide.with(ProductInfoActivity.this)
                                .load(jsonProductInfo.getString("productImage"))
                                .into(imageViewProductImage);
                    }
                    else {
                        Toast.makeText(ProductInfoActivity.this, jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductInfoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productId", productId);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
