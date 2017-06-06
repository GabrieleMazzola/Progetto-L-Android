package com.example.andrea.androiduser;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.example.andrea.androiduser.tickets.Products;
import com.example.andrea.androiduser.tickets.SimpleTicket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 01/06/2017.
 */

public class BuyTicket extends AppCompatActivity {

    TextView creditCardTextView;
    Spinner types;
    RequestQueue requestQueue;
    final StringBuilder sBuilderUserInfo = new StringBuilder();
    private Map<String, Products> productMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        requestQueue = Volley.newRequestQueue(BuyTicket.this.getApplicationContext());
        Intent intent = getIntent();

        productMap = new HashMap();

        getTicketTypes();
        types = (Spinner) findViewById(R.id.types);

        creditCardTextView = (TextView) findViewById(R.id.creditcard);

        Button buyTicketButton = (Button) findViewById(R.id.buy);
        buyTicketButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = creditCardTextView.getText().toString();

                if(text.equals("")){
                    Toast.makeText(BuyTicket.this,"Insert Credit Card Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if(text.matches("[0-9]+") && text.length()==16){
                }else{
                    Toast.makeText(BuyTicket.this,"Invalid Card Number", Toast.LENGTH_LONG).show();
                    return;
                }


                String selectedType = types.getSelectedItem().toString();
                Products selectedProduct=null;
                for(Products p : productMap.values()){
                    if(selectedType.contains(p.getName()+" ") ){
                        selectedProduct = p;
                    }
                }

                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE,(int)selectedProduct.getDuration());
                String expiryDate = DateOperations.getInstance().toString(now.getTime());

                String json_url = InfoHandler.BUY_TICKET_API+InfoHandler.getUsername(getApplicationContext())+"/"+creditCardTextView.getText().toString()+"/"+expiryDate+"/"+selectedProduct.getId();//+types.getSelectedItem().toString();
                JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                        , null,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    Toast.makeText(BuyTicket.this,response.getString("data"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    Toast.makeText(BuyTicket.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(BuyTicket.this,"Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void getTicketTypes() {
        JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, InfoHandler.TYPES_API
                , null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String types = parseTickets(response);
                        populateSpinner();
                        Toast.makeText(BuyTicket.this, types, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BuyTicket.this, "Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
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

    private String parseTickets(JSONObject obj){
        try {
            JSONArray ticketArray = (JSONArray)obj.get("data");
            for(int i = 0; i < ticketArray.length();i++){
                JSONObject ticket = (JSONObject) ticketArray.get(i);

                double duration = (Double)ticket.get("duration");
                String id = (String)ticket.get("id");
                double cost = (Double)ticket.get("cost");
                String name = (String)ticket.get("name");
                productMap.put(id , new SimpleTicket(name,id,cost,duration));
            }
            return "ciao";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "ERROR PARSING";
    }

    public void populateSpinner () {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item);
        types.setAdapter(adapter);
        List<CharSequence> list = new ArrayList<CharSequence>();
        for (Products p : productMap.values()) {
            list.add(p.getName() + " - " + p.getCost() + "€ - " + (int)(p.getDuration()) + " m");
        }
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
