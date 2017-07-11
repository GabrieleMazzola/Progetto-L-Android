package com.example.andrea.androiduser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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

/**
 * Created by Andrea on 04/06/2017.
 */

public class SignIn extends AppCompatActivity {

    RequestQueue requestQueue;
    final StringBuilder sBuilderUserInfo = new StringBuilder();
    TextView name,surname,username,cf,psw1,psw2,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final Intent toUserHub = new Intent(this,UserHub.class);

        final StringBuilder stringBuilder = new StringBuilder();

        requestQueue = Volley.newRequestQueue(SignIn.this.getApplicationContext());

        name = (TextView) findViewById(R.id.name);
        surname = (TextView) findViewById(R.id.surname);
        username = (TextView) findViewById(R.id.usernameReg);
        cf = (TextView) findViewById(R.id.cf);
        psw1 = (TextView) findViewById(R.id.psw1);
        psw2 = (TextView) findViewById(R.id.psw2);
        email = (TextView) findViewById(R.id.email);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new Button.OnClickListener() {
                                        public void onClick(View v) {

                                            if(     name.getText().toString().equals("") ||
                                                    surname.getText().toString().equals("") ||
                                                    username.getText().toString().equals("") ||
                                                    cf.getText().toString().equals("") ||
                                                    psw1.getText().toString().equals("") ||
                                                    psw2.getText().toString().equals("") ||
                                                    email.getText().toString().equals(""))
                                            {
                                                Toast.makeText(SignIn.this,"Invalid Input", Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                            if(psw1.getText().toString().equals(psw2.getText().toString())) {
                                            }else{
                                                Toast.makeText(SignIn.this,"Password Mismatch", Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                            String json_url = InfoHandler.REGISTRATION_API + name.getText().toString() + "/" + surname.getText().toString()+
                                                                "/" + cf.getText().toString() + "/" + username.getText().toString() + "/" + psw1.getText().toString()
                                                                + "/" + email.getText().toString();
                                            Log.i("CREATEUSER",json_url);
                                            JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url, null,

                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            try {
                                                                Toast.makeText(SignIn.this, response.getString(JsonFields.DATA.toString()), Toast.LENGTH_LONG).show();
                                                                if (response.getString(JsonFields.DATA.toString()).equals("true")) {
                                                                    Toast.makeText(SignIn.this, response.getString(JsonFields.DATA.toString()), Toast.LENGTH_LONG).show();
                                                                    InfoHandler.saveLogin(getApplicationContext(), username.getText().toString(), psw1.getText().toString());
                                                                    startActivity(toUserHub);
                                                                }
                                                            } catch (JSONException e) {
                                                                Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(SignIn.this, "Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );
                                            requestQueue.add(myJsonObjectRequest);
                                        }
                                    }
        );
    }

}
