package com.circletech.skeletonecommerce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentAddProduct extends Fragment {

    EditText editTextProductName, editTextProductDescription, editTextProductPrice, editTextProductQuantity;
    Button btnAdd;

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addproduct, container, false);
        editTextProductName = view.findViewById(R.id.fragment_addproduct_editTextProductName);
        editTextProductDescription = view.findViewById(R.id.fragment_addproduct_editTextProductDescription);
        editTextProductPrice = view.findViewById(R.id.fragment_addproduct_editTextPrice);
        editTextProductQuantity = view.findViewById(R.id.fragment_addproduct_editTextProductQuantity);
        btnAdd = view.findViewById(R.id.fragment_addproduct_btnAdd);
        progressBar = view.findViewById(R.id.fragment_addproduct_progressBar);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextProductName.getText().length() == 0 || editTextProductDescription.getText().length() == 0 || editTextProductPrice.getText().length() == 0 || editTextProductQuantity.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please fill in all the details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    addProduct(editTextProductName.getText().toString().trim(), editTextProductDescription.getText().toString().trim(), editTextProductPrice.getText().toString().trim(), editTextProductQuantity.getText().toString().trim());
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Add New Product");
    }

    public void addProduct(final String productName, final String productDescription, final String productPrice, final String productQuantity) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.URL_ADD_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponseObj = new JSONObject(response);

                    if (!jsonResponseObj.getBoolean("error")) {
                        Toast.makeText(getActivity(), jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productName", productName);
                params.put("productDescription", productDescription);
                params.put("productPrice", productPrice);
                params.put("productQuantity", productQuantity);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}