package com.circletech.skeletonecommerce;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductInfoActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

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

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {

                    JSONArray array = object.getJSONArray("product");
                    JSONObject productInfo = array.getJSONObject(0);

                    textViewProductId.setText(String.valueOf(productInfo.getInt("productId")));
                    textViewProductName.setText(productInfo.getString("productName"));
                    textViewProductDescription.setText(productInfo.getString("productDescription"));
                    textViewProductPrice.setText(getApplication().getResources().getString(R.string.activity_productinfo_productprice, productInfo.getDouble("productPrice")));
                    textViewProductQuantity.setText(getApplicationContext().getResources().getString(R.string.activity_productinfo_productquantity, productInfo.getInt("productQuantity")));

                    Glide.with(ProductInfoActivity.this)
                            .load(productInfo.getString("productImage"))
                            .into(imageViewProductImage);

                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(ProductInfoActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            APIRequestHandler requestHandler = new APIRequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    public void getProductInfo(String productId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("productId", productId);

        ProductInfoActivity.PerformNetworkRequest request = new ProductInfoActivity.PerformNetworkRequest(API.URL_GET_PRODUCT_INFO, params, CODE_POST_REQUEST);
        request.execute();
    }
}
