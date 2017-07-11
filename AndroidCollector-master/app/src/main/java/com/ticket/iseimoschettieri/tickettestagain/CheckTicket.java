package com.ticket.iseimoschettieri.tickettestagain;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.ticket.iseimoschettieri.tickettestagain.jsonenumerations.JsonFields;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 01/06/2017.
 */

public class CheckTicket extends AppCompatActivity {

    TextView ticketCodeTextView;
    RequestQueue requestQueue;
    final StringBuilder sBuilderUserInfo = new StringBuilder();
    final StringBuilder sBuilderUrl = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ticket);
        requestQueue=requestQueue = Volley.newRequestQueue(CheckTicket.this.getApplicationContext());
        Intent intent = getIntent();

       ticketCodeTextView = (TextView) findViewById(R.id.cf);
       Button checkTicketButton = (Button) findViewById(R.id.checkTicket);
       checkTicketButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                sBuilderUrl.setLength(0);
                sBuilderUrl.append(InfoHandler.CHECK_TICKET_API);
                sBuilderUrl.append(ticketCodeTextView.getText().toString().trim());
                JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, sBuilderUrl.toString()
                        , null,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toast.makeText(CheckTicket.this,response.getString(JsonFields.DATA.toString()), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    Toast.makeText(CheckTicket.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CheckTicket.this,"Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                ) {@Override
                public Map< String, String > getHeaders() throws AuthFailureError {
                    HashMap< String, String > headers = new HashMap < String, String > ();
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
        });
    }
}
