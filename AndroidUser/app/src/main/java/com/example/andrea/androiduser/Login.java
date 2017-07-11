package com.example.andrea.androiduser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.andrea.androiduser.jsonenumerations.JsonFields;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button myButton, goToSignInButton,go;
    TextView username, password,config;
    RequestQueue requestQueue;
    public EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Intent toUserHub = new Intent(this,UserHub.class);
        final Intent toSignIn = new Intent(this,SignIn.class);

        final StringBuilder stringBuilder = new StringBuilder();
        requestQueue = Volley.newRequestQueue(Login.this.getApplicationContext());


        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);

        goToSignInButton =(Button) findViewById(R.id.goToSignIn);
        goToSignInButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    startActivity(toSignIn);
                }
            }
        );

        config = (TextView) findViewById(R.id.configText);
        go = (Button) findViewById(R.id.configButton);
        go.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                String configuration = config.getText().toString();
                InfoHandler.updateHOST(configuration);
                hideConfigurationEgg();
                Toast.makeText(Login.this,"HOST updated BOSS", Toast.LENGTH_LONG).show();
                return;
            }
        });
        hideConfigurationEgg();


        myButton = (Button) findViewById(R.id.login);
        myButton.setOnClickListener(new Button.OnClickListener() {
                                        public void onClick(View v) {

                                            if(username.getText().toString().equals("config") && password.getText().toString().equals("")){
                                                showConfigurationEgg();
                                                return;
                                            }

                                            if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                                                Toast.makeText(Login.this,"Invalid Input", Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                            JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,InfoHandler.USERLOGIN_API, null,

                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            try {
                                                                Toast.makeText(Login.this,response.getString(JsonFields.DATA.toString()), Toast.LENGTH_LONG).show();
                                                                if(response.getString(JsonFields.DATA.toString()).equals("true")){
                                                                    Toast.makeText(Login.this, response.getString(JsonFields.DATA.toString()), Toast.LENGTH_LONG).show();

                                                                    InfoHandler.saveLogin(getApplicationContext(),username.getText().toString().trim(),password.getText().toString().trim());
                                                                    editText = (EditText)findViewById(R.id.username);
                                                                    editText.setText("");
                                                                    editText = (EditText)findViewById(R.id.password);
                                                                    editText.setText("");

                                                                    startActivity(toUserHub);
                                                                }
                                                            } catch (JSONException e) {
                                                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(Login.this,"Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
                                                            username.setText(error.getMessage());
                                                        }
                                                    }
                                            ) {@Override
                                            public Map< String, String > getHeaders() throws AuthFailureError {
                                                HashMap< String, String > headers = new HashMap < String, String > ();
                                                stringBuilder.setLength(0);
                                                stringBuilder.append(username.getText().toString().trim());
                                                stringBuilder.append(":");
                                                stringBuilder.append(password.getText().toString().trim());
                                                String encodedCredentials = Base64.encodeToString(stringBuilder.toString().getBytes(), Base64.NO_WRAP);
                                                headers.put("Authorization", "Basic " + encodedCredentials);

                                                return headers;
                                            }
                                            };
                                            requestQueue.add(myJsonObjectRequest);
                                        }
                                    }
        );
    }

    private void hideConfigurationEgg() {
        config.setVisibility(View.INVISIBLE);
        go.setVisibility(View.INVISIBLE);
    }
    private void showConfigurationEgg() {
        config.setVisibility(View.VISIBLE);
        go.setVisibility(View.VISIBLE);
    }
}
