package com.example.andrea.androiduser;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.andrea.androiduser.jsonenumerations.JsonFields;

import com.example.andrea.androiduser.jsonenumerations.TicketTypes;
import com.example.andrea.androiduser.tickets.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 01/06/2017.
 */

public class BuyTicket extends AppCompatActivity {

    TextView creditCardTextView1;               //EditText
    TextView creditCardTextView2;
    TextView creditCardTextView3;
    TextView creditCardTextView4;

    StringBuilder sBuilderCreditCard = new StringBuilder();

    Spinner types;
    RequestQueue requestQueue;
    final StringBuilder sBuilderUserInfo = new StringBuilder();
    private Map<String, Product> productMap;

    //boolean firstTimeEnteringCrediCardNumber=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        requestQueue = Volley.newRequestQueue(BuyTicket.this.getApplicationContext());
        Intent intent = getIntent();

        productMap = new HashMap();

        getTicketTypes();
        types = (Spinner) findViewById(R.id.types);

        creditCardTextView1 = (EditText) findViewById(R.id.creditcard1);
        creditCardTextView2 = (TextView) findViewById(R.id.creditcard2);
        creditCardTextView3 = (TextView) findViewById(R.id.creditcard3);
        creditCardTextView4 = (TextView) findViewById(R.id.creditcard4);
/*
        creditCardTextView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCardTextView1.setFocusable(false);
                creditCardTextView2.setFocusable(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(firstTimeEnteringCrediCardNumber==true){
                    if(creditCardTextView1.getText().length()==4){//guarda se cambia la lunghezza con un toast
                        creditCardTextView1.setFocusable(false);
                        creditCardTextView2.setFocusable(true);
                    }
                    firstTimeEnteringCrediCardNumber=false;
                }

            }
        });
*/
        Button buyTicketButton = (Button) findViewById(R.id.buy);
        buyTicketButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                sBuilderCreditCard.append(creditCardTextView1.getText().toString());
                sBuilderCreditCard.append(creditCardTextView2.getText().toString());
                sBuilderCreditCard.append(creditCardTextView3.getText().toString());
                sBuilderCreditCard.append(creditCardTextView4.getText().toString());

                String textCreditCard = sBuilderCreditCard.toString();
                sBuilderCreditCard.setLength(0);

                if(textCreditCard.equals("")){
                    Toast.makeText(BuyTicket.this,"Insert Credit Card Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if(textCreditCard.matches("[0-9]+") && textCreditCard.length()==16){
                }else{
                    Toast.makeText(BuyTicket.this,"Invalid Card Number", Toast.LENGTH_LONG).show();
                    return;
                }


                String selectedType = types.getSelectedItem().toString();
                Product selectedProduct=null;
                for(Product p : productMap.values()){
                    if(selectedType.contains(p.getDescription()+" ") ){
                        selectedProduct = p;
                    }
                }

                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE,(int)selectedProduct.getDuration());
                String expiryDate = DateOperations.getInstance().toString(now.getTime());

                String json_url = InfoHandler.BUY_TICKET_API+InfoHandler.getUsername(getApplicationContext())+"/"+
                        textCreditCard+"/"+selectedProduct.getType();
                JsonObjectRequest myJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                        , null,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    Toast.makeText(BuyTicket.this,response.getString(JsonFields.DATA.toString()), Toast.LENGTH_LONG).show();
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
                        parseTickets(response);
                        populateSpinner();
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

    private void parseTickets(JSONObject obj){
        try {
            JSONArray ticketArray = (JSONArray)obj.get(JsonFields.DATA.toString());
            for(int i = 0; i < ticketArray.length();i++){
                JSONObject ticket = (JSONObject) ticketArray.get(i);

                int duration = (Integer)ticket.get(TicketTypes.DURATION.toString());
                String type = (String)ticket.get(TicketTypes.TYPE.toString());
                double cost = (Double)ticket.get(TicketTypes.COST.toString());
                String description = (String)ticket.get(TicketTypes.DESCRIPTION.toString());
                switch (type.charAt(0)){
                    case'T':
                        productMap.put(type , new SimpleTicket(description,type,cost,duration));
                        break;
                    case'S':
                        productMap.put(type , new SimpleSeason(description,type,cost/duration,duration));
                        break;
                    default:
                        Toast.makeText(BuyTicket.this,"Product not found", Toast.LENGTH_LONG).show();
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void populateSpinner () {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item);
        types.setAdapter(adapter);
        List<CharSequence> list = new ArrayList<CharSequence>();
        for (Product p : productMap.values()) {
            list.add(p.getDescription() + " - " + p.getCost() + "€ - " + (int)(p.getDuration()) + " m");
        }
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
