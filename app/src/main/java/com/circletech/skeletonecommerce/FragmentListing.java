package com.circletech.skeletonecommerce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentListing extends Fragment {

    List<Product> productList;

    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);
        recyclerView = view.findViewById(R.id.fragment_listing_RecyclerView);
        progressBar = view.findViewById(R.id.fragment_listing_ProgressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        productList = new ArrayList<>();
        getProducts();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Home");
    }

    public void getProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.URL_GET_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponseObj = new JSONObject(response);
                    if (!jsonResponseObj.getBoolean("error")) {

                        JSONArray jsonProductArray = jsonResponseObj.getJSONArray("products");

                        for (int i = 0; i < jsonProductArray.length(); i++) {

                            JSONObject jsonProductDetails = jsonProductArray.getJSONObject(i);

                            productList.add(new Product(
                                    jsonProductDetails.getInt("productId"),
                                    jsonProductDetails.getString("productName"),
                                    jsonProductDetails.getString("productDescription"),
                                    jsonProductDetails.getInt("productQuantity"),
                                    jsonProductDetails.getDouble("productPrice"),
                                    jsonProductDetails.getString("productImage")
                            ));
                        }

                        ProductAdapter productAdapter = new ProductAdapter(getActivity(), productList);
                        recyclerView.setAdapter(productAdapter);
                    }
                    else {
                        Toast.makeText(getActivity(), jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}