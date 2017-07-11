package com.ticket.iseimoschettieri.tickettestagain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ticket.iseimoschettieri.tickettestagain.jsonenumerations.JsonFields;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 01/06/2017.
 */

public class MakeFine extends AppCompatActivity {

    TextView fiscalCodeTextView, amountTextView;
    RequestQueue requestQueue;
    final StringBuilder sBuilderUserInfo = new StringBuilder();
    final StringBuilder sBuilderUrl = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_fine);

        requestQueue = Volley.newRequestQueue(MakeFine.this.getApplicationContext());
        Intent intent = getIntent();

        fiscalCodeTextView = (TextView) findViewById(R.id.cf);
        amountTextView = (TextView) findViewById(R.id.amount);

        Button checkTicketButton = (Button) findViewById(R.id.makeFine);
        checkTicketButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {   //id=username+startingnumber cf amount username

                sBuilderUrl.setLength(0);
                sBuilderUrl.append(InfoHandler.getUsername(getApplicationContext()));
                sBuilderUrl.append(InfoHandler.getStartingNumber(getApplicationContext()).toString());
                sBuilderUrl.append("/");
                sBuilderUrl.append(fiscalCodeTextView.getText().toString().trim());
                sBuilderUrl.append("/");
                sBuilderUrl.append(amountTextView.getText().toString().trim());
                sBuilderUrl.append("/");
                sBuilderUrl.append(InfoHandler.getUsername(getApplicationContext()));

                makeVolleyFine(sBuilderUrl.toString());
            }
        });
    }

    private void makeVolleyFine(final String fine) {
        JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                InfoHandler.MAKE_FINE_API + fine, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            InfoHandler.incrementStartingNumber(getApplicationContext());
                            Toast.makeText(MakeFine.this, response.getString(JsonFields.DATA.toString()), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(MakeFine.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //InfoHandler.addOffLineFine(getApplicationContext(),fine);
                        Toast.makeText(MakeFine.this, "Something went wrong " /*+ InfoHandler.getOffLineFine(getApplicationContext())*/, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                sBuilderUserInfo.setLength(0);
                sBuilderUserInfo.append(InfoHandler.getUsername(getApplicationContext()));
                sBuilderUserInfo.append(":");
                sBuilderUserInfo.append(InfoHandler.getPassword(getApplicationContext()));
                String encodedCredentials = Base64.encodeToString(sBuilderUserInfo.toString().getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);

                return headers;
            }
        };
        requestQueue.add(myJsonObjectRequest);
    }
}

