package com.circletech.skeletonecommerce;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextUserName, editTextPassword;
    TextView textViewRegisterAcc;
    Button btnLogin;

    public static UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = findViewById(R.id.login_editTextUserName);
        editTextPassword = findViewById(R.id.login_editTextPassword);
        btnLogin = findViewById(R.id.login_btnLogin);
        textViewRegisterAcc = findViewById(R.id.login_textViewRegisterAcc);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.getText().length() == 0 || editTextPassword.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                }
                else {
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
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    setAccountInfo(object.getJSONArray("account"));

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void loginAccount(String userName, String password) {

        HashMap<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_LOGIN_ACCOUNT, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void setAccountInfo(JSONArray account) throws JSONException {
        JSONObject obj = account.getJSONObject(0);
        userAccount = new UserAccount(
                obj.getString("userName"),
                obj.getString("email")
        );
    }

    public static UserAccount getUserAccount() {
        return userAccount;
    }
}
