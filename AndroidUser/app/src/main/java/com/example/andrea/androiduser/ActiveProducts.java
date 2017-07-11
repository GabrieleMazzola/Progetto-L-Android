package com.example.andrea.androiduser;

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

/**
 * Created by Andrea on 09/07/2017.
 */

public class ActiveProducts extends AppCompatActivity {

    Button statusButton;
    TextView validTextView;
    RequestQueue requestQueue;
    StringBuilder sBuilderUserInfo;

    Map<String, Product> products;
    List<Sale> validSalesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid_ticket);
        requestQueue = Volley.newRequestQueue(ActiveProducts.this.getApplicationContext());
        Intent intent = getIntent();

        requestQueue = Volley.newRequestQueue(ActiveProducts.this.getApplicationContext());


        validTextView = (TextView) findViewById(R.id.validText);
        sBuilderUserInfo = new StringBuilder();

        popolateProducts();

    }

    private void popolateProducts() {
        JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, InfoHandler.TYPES_API
                , null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("REDS","parseTicketTypes");
                        parseTicketTypes(response);
                        createHistoryVolley();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActiveProducts.this, "Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void createHistoryVolley(){
        String json_url = InfoHandler.MY_VALID_TICKETS_API+InfoHandler.getUsername(getApplicationContext());
        JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                , null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        parseTickets(response);
                        orderValidSaleList();
                        validTextView.setText(printTickets());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActiveProducts.this,"Something went wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
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
                Log.i("REDS","parseTicketTypes ITERATION");
                switch (type.charAt(0)){
                    case'T':
                        products.put(type , new SimpleTicket(description,type,cost,duration));
                        break;
                    case'S':
                        products.put(type , new SimpleSeason(description,type,cost,duration));
                        break;
                    default:
                        Toast.makeText(ActiveProducts.this,"Product not found", Toast.LENGTH_LONG).show();
                        break;
                }
            }
            Log.i("REDS","parseTicketTypes FINE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseTickets(JSONObject obj){
        try {
            JSONArray ticketArray = (JSONArray)obj.get(JsonFields.DATA.toString());
            validSalesList = new ArrayList<>();
            Log.i("REDS","parseTickets");

            for(int i = 0; i < ticketArray.length();i++){

                JSONObject ticket = (JSONObject) ticketArray.get(i);
                Log.i("REDS","parseTickets ITERATION " + i);
                Log.i("REDS","object: " + ticket.toString());

                String sellDate = (String) ticket.get(MyTickets.SALEDATE.toString());
                Log.i("REDS","parseTickets SALE");
                String type =(String) ticket.get(MyTickets.TYPE.toString());
                Log.i("REDS","parseTickets TYPE");
                String sellerMachineIp = (String) ticket.get(MyTickets.SELLERMACHINEIP.toString());
                Log.i("REDS","parseTickets IP");
                String username = (String) ticket.get(MyTickets.USERNAME.toString());
                Log.i("REDS","parseTickets USERNAME");
                Integer serialCode = (Integer) ticket.get(MyTickets.SERIALCODE.toString());
                Log.i("REDS","parseTickets SERIAL");


                Date saleDate = DateOperations.getInstance().parse(sellDate);

                Log.i("REDS","parseTickets PARSING");


                Sale sale = new Sale(saleDate, Long.valueOf(serialCode.toString()), username, products.get(type), sellerMachineIp );
                Log.i("REDS","parseTickets CREAZIONE SALE");

                validSalesList.add(sale);
                Log.i("REDS","parseTickets AGGIUNTA SALE");

            }

            Log.i("REDS","parseTickets FINE");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
/*
    private void setButtonBackground(Button btn){
        if(checkValidity() == 1){
            btn.setBackgroundColor(Color.GREEN);
        }
        else{
            btn.setBackgroundColor(Color.RED);
        }
    }
*/
    private void orderValidSaleList(){
        Collections.sort(validSalesList,Sale.saleComparator);
    }

    private String printTickets() {

        StringBuilder sb = new StringBuilder("TICKETS\n\n");
        for(Sale sale : validSalesList){
            sb.append("\n------------------------------");
            sb.append(sale.toString());
            sb.append("\n\n");
        }
        return sb.toString();
    }
}
