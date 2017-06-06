package com.example.andrea.androiduser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 04/06/2017.
 */

public class History extends AppCompatActivity {

    TextView historyTextView;
    RequestQueue requestQueue;
    final StringBuilder sBuilderUserInfo = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        requestQueue = Volley.newRequestQueue(History.this.getApplicationContext());


        historyTextView = (TextView) findViewById(R.id.historyText);

                String json_url = InfoHandler.MY_TICKETS_API+InfoHandler.getUsername(getApplicationContext());
                JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                        , null,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String tickets = parseTickets(response);
                                historyTextView.setText(tickets);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(History.this,"Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
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

    private String parseTickets(JSONObject obj){
        try {
            StringBuilder sb = new StringBuilder("\n\nTICKETS\n\n----------\n");
            JSONArray ticketArray = (JSONArray)obj.get("data");
            for(int i = 0; i < ticketArray.length();i++){
                JSONObject ticket = (JSONObject) ticketArray.get(i);
                sb.append("\n\tID: ").append(ticket.get("id"));
                sb.append("\n\tScadenza: ").append(DateOperations.getInstance().parse((String)ticket.get("expire")).toString());
                sb.append("\n\tTipo: ").append(ticket.get("type"));
                sb.append("\n\n----------\n");
            }
            return sb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "ERROR PARSING";
    }
}
