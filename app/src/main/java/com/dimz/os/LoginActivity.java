package com.dimz.os;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout til_username, til_password;
    Button bt_login;
    Button tv_register;
    String username, password;
    RequestQueue requestQueue;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        c = this;
        setTitle("Login");//set title of the activity
        initialize(); //Initialize all the widgets present in the layout
        requestQueue = Volley.newRequestQueue(LoginActivity.this);//Creating the RequestQueue
        //Login Button Clicked
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ini codenya//
//                SharedPrefs.saveSharedSetting(LoginActivity.this, "DimzCode", "false");

                username = til_username.getEditText().getText().toString();
                password = til_password.getEditText().getText().toString();
                if (validateUsername(username) && validatePassword(password)) { //Username and Password Validation
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Logging You In");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    LoginRequest loginRequest = new LoginRequest(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Login Response", response);
                            progressDialog.dismiss();
                            // Response from the server is in the form if a JSON, so we need a JSON Object
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    Intent loginSuccess = new Intent(LoginActivity.this, MainActivity.class);
                                    // kita bakal nyimpen hasil ini ke sharedPref, biar bisa diakses di activity mana pun
                                    //Passing all received data from server to next activity
//                                    loginSuccess.putExtra("name", jsonObject.getString("name"));
//                                    loginSuccess.putExtra("mobile", jsonObject.getString("mobile"));
//                                    loginSuccess.putExtra("email", jsonObject.getString("email"));
//                                    loginSuccess.putExtra("age", jsonObject.getString("age"));
//                                    loginSuccess.putExtra("url", jsonObject.getString("url"));

                                    // simpan data kembalian login ke sharedPref
                                    SharedPrefs.saveSharedSetting(c, SharedPrefs.PREF_NAME, jsonObject.getString("name"));
                                    SharedPrefs.saveSharedSetting(c, SharedPrefs.PREF_EMAIL, jsonObject.getString("email"));
                                    SharedPrefs.saveSharedSetting(c, SharedPrefs.PREF_AGE, jsonObject.getString("age"));
                                    SharedPrefs.saveSharedSetting(c, SharedPrefs.PREF_MOBILE, jsonObject.getString("mobile"));
                                    SharedPrefs.saveSharedSetting(c, SharedPrefs.PREF_URL, jsonObject.getString("url"));

                                    // ubah nilai sharedPref jadi true ketika login sukses
                                    SharedPrefs.saveSharedSetting(LoginActivity.this, SharedPrefs.KEY_LOGIN_NAME, true);

                                    startActivity(loginSuccess);
                                    finish();
                                } else {
                                    if (jsonObject.getString("status").equals("INVALID"))
                                        Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                    else {
                                        Toast.makeText(LoginActivity.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                                    }
                                    // ubah nilai sharedPref jadi false ketika login gagal buat pengecekan di SplashActivity
                                    SharedPrefs.saveSharedSetting(LoginActivity.this, SharedPrefs.KEY_LOGIN_NAME, false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(LoginActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(LoginActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(loginRequest);

                }

            }
        });
        //Don't Have An Account TextView Clicked
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void initialize() {
        til_username = (TextInputLayout) findViewById(R.id.til_username);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_register = (Button) findViewById(R.id.tv_register);
    }

    private boolean validateUsername(String string) {
        //Validating the entered USERNAME
        if (string.equals("")) {
            til_username.setError("Enter a Username");
            return false;
        } else if (string.length() > 50) {
            til_username.setError("Maximum 50 Characters");
            return false;
        } else if (string.length() < 6) {
            til_username.setError("Minimum 6 Characters");
            return false;
        }
        til_username.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword(String string) {
        //Validating the entered PASSWORD
        if (string.equals("")) {
            til_password.setError("Enter Your Password");
            return false;
        } else if (string.length() > 50) {
            til_password.setError("Maximum 50 Characters");
            return false;
        } else if (string.length() < 8) {
            til_password.setError("Minimum 8 Characters");
            return false;
        }
        til_password.setErrorEnabled(false);
        return true;
    }
}