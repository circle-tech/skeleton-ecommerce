package com.circletech.skeletonecommerce;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FragmentAddProduct extends Fragment {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextProductName, editTextProductDescription, editTextProductPrice, editTextProductQuantity;
    Button btnAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addproduct, container, false);
        editTextProductName = view.findViewById(R.id.fragment_additem_editTextProductName);
        editTextProductDescription = view.findViewById(R.id.fragment_additem_editTextProductDescription);
        editTextProductPrice = view.findViewById(R.id.fragment_additem_editTextPrice);
        editTextProductQuantity = view.findViewById(R.id.fragment_additem_editTextProductQuantity);
        btnAdd = view.findViewById(R.id.fragment_additem_btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextProductName.getText().length() == 0 || editTextProductDescription.getText().length() == 0 || editTextProductPrice.getText().length() == 0 || editTextProductQuantity.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please fill in all the details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    addProduct(editTextProductName.getText().toString().trim(), editTextProductDescription.getText().toString().trim(), editTextProductPrice.getText().toString().trim(), editTextProductQuantity.getText().toString().trim());
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add New Product");
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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
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

    public void addProduct(String productName, String productDescription, String productPrice, String productQuantity) {

        HashMap<String, String> params = new HashMap<>();
        params.put("productName", productName);
        params.put("productDescription", productDescription);
        params.put("productPrice", productPrice);
        params.put("productQuantity", productQuantity);

        FragmentAddProduct.PerformNetworkRequest request = new FragmentAddProduct.PerformNetworkRequest(API.URL_ADD_PRODUCT, params, CODE_POST_REQUEST);
        request.execute();
    }
}