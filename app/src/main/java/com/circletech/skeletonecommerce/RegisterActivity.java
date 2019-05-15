package com.circletech.skeletonecommerce;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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

    public void registerAccount(String userName, String email, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("email", email);
        params.put("password", password);

        RegisterActivity.PerformNetworkRequest request = new RegisterActivity.PerformNetworkRequest(API.URL_REGISTER_ACCOUNT, params, CODE_POST_REQUEST);
        request.execute();
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
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
}
