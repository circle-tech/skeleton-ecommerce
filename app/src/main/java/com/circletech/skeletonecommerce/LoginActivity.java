package com.circletech.skeletonecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Session userSession;

    EditText editTextUserName, editTextPassword;
    TextView textViewRegisterAcc;
    Button btnLogin;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSession = new Session(getApplicationContext());

        editTextUserName = findViewById(R.id.login_editTextUserName);
        editTextPassword = findViewById(R.id.login_editTextPassword);
        btnLogin = findViewById(R.id.login_btnLogin);
        textViewRegisterAcc = findViewById(R.id.login_textViewRegisterAcc);
        progressBar = findViewById(R.id.login_progressBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.getText().length() == 0 || editTextPassword.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginAccount(editTextUserName.getText().toString().trim(), editTextPassword.getText().toString().trim());
                }
            }
        });

        textViewRegisterAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginAccount(final String userName, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.URL_LOGIN_ACCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponseObj = new JSONObject(response);
                    if (!jsonResponseObj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject jsonAccountObj = jsonResponseObj.getJSONArray("account").getJSONObject(0);
                        userSession.createLoginSession(jsonAccountObj.getString("userName"), jsonAccountObj.getString("email"));

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, jsonResponseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
