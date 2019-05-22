package com.circletech.skeletonecommerce;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    Button btnRegister;
    EditText userName, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.register_btnRegister);
        userName = findViewById(R.id.register_editTextUserName);
        email = findViewById(R.id.register_editTextEmail);
        password = findViewById(R.id.register_editTextPassword);
        confirmPassword = findViewById(R.id.register_editTextConfirmPassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().length() == 0 || email.getText().length() == 0 || password.getText().length() == 0 || confirmPassword.getText().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all the details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "The passwords are not matching!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        registerAccount(userName.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim());
                    }
                }
            }
        });
    }

    public void registerAccount(final String userName, final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.URL_REGISTER_ACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponseObj = new JSONObject(response);
                    if (!jsonResponseObj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
          @Override
          protected Map<String, String> getParams() {
              Map<String, String> params = new HashMap<>();
              params.put("userName", userName);
              params.put("email", email);
              params.put("password", password);
              return params;
          }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
