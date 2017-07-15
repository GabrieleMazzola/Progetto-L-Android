package com.example.andrea.androiduser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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
import com.example.andrea.androiduser.jsonenumerations.JsonFields;
import com.example.andrea.androiduser.jsonenumerations.MyTickets;
import com.example.andrea.androiduser.jsonenumerations.TicketTypes;
import com.example.andrea.androiduser.tickets.Product;
import com.example.andrea.androiduser.tickets.Sale;
import com.example.andrea.androiduser.tickets.SimpleSeason;
import com.example.andrea.androiduser.tickets.SimpleTicket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.andrea.androiduser.tickets.Sale.saleComparator;

/**
 * Created by Andrea on 04/06/2017.
 */

public class History extends AppCompatActivity {

    TextView historyTextView;
    RequestQueue requestQueue;
    final StringBuilder sBuilderUserInfo = new StringBuilder();

    Map<String, Product> products;
    List<Sale> salesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        requestQueue = Volley.newRequestQueue(History.this.getApplicationContext());
        Intent intent = getIntent();

        historyTextView = (TextView) findViewById(R.id.historyText);

        popolateProducts();
    }

    private void popolateProducts() {
        JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, InfoHandler.TYPES_API
                , null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseTicketTypes(response);
                        createHistoryVolley();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(History.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
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

    public void parseTicketTypes(JSONObject obj){
        try {
            JSONArray ticketArray = (JSONArray)obj.get(JsonFields.DATA.toString());
            products = new HashMap<>();

            for(int i = 0; i < ticketArray.length();i++){
                JSONObject ticket = (JSONObject) ticketArray.get(i);

                int duration = (Integer)ticket.get(TicketTypes.DURATION.toString());
                String type = (String)ticket.get(TicketTypes.TYPE.toString());
                double cost = (Double)ticket.get(TicketTypes.COST.toString());
                String description = (String)ticket.get(TicketTypes.DESCRIPTION.toString());
                switch (type.charAt(0)){
                    case'T':
                        products.put(type , new SimpleTicket(description,type,cost,duration));
                        break;
                    case'S':
                        products.put(type , new SimpleSeason(description,type,cost,duration));
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createHistoryVolley(){
        String json_url = InfoHandler.MY_TICKETS_API+InfoHandler.getUsername(getApplicationContext());
        JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                , null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        parseTickets(response);
                        orderSaleList();
                        historyTextView.setText(printTickets());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(History.this,"Something went wrong :(", Toast.LENGTH_SHORT).show();
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

    private String printTickets() {
        StringBuilder sb = new StringBuilder("TICKETS\n\n");
        for(Sale sale : salesList){
            sb.append("\n------------------------------");
            sb.append(sale.toString());
            sb.append("\n\n");
        }
        return sb.toString();
    }

    private void orderSaleList(){
        Collections.sort(salesList,Sale.saleComparator);
    }


    private void parseTickets(JSONObject obj){
        try {
            JSONArray ticketArray = (JSONArray)obj.get(JsonFields.DATA.toString());
            salesList = new ArrayList<>();

            for(int i = 0; i < ticketArray.length();i++){

                JSONObject ticket = (JSONObject) ticketArray.get(i);

                String sellDate = (String) ticket.get(MyTickets.SALEDATE.toString());
                String type =(String) ticket.get(MyTickets.TYPE.toString());
                String sellerMachineIp = (String) ticket.get(MyTickets.SELLERMACHINEIP.toString());
                String username = (String) ticket.get(MyTickets.USERNAME.toString());
                Integer serialCode = (Integer) ticket.get(MyTickets.SERIALCODE.toString());

                Date saleDate = DateOperations.getInstance().parse(sellDate);

                Sale sale = new Sale(saleDate, Long.valueOf(serialCode.toString()), username, products.get(type), sellerMachineIp );

                salesList.add(sale);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
