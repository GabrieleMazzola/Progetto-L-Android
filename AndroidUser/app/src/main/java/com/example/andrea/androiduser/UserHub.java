package com.example.andrea.androiduser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andrea on 03/06/2017.
 */

public class UserHub extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_hub);

        setTitle("Ciao " + InfoHandler.getUsername(getApplicationContext()));

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final Intent toLoginPage = new Intent(this,Login.class);
        final Intent toBuyTicket = new Intent(this,BuyTicket.class);
        final Intent toHistory = new Intent(this,History.class);
        final Intent toActiveProducts = new Intent(this,ActiveProducts.class);


        Button logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoHandler.logout(getApplicationContext());
                startActivity(toLoginPage);
            }
        });

        Button buyTicketButton = (Button) findViewById(R.id.buyTicket);
        buyTicketButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toBuyTicket);
            }
        });

        Button historyButton = (Button) findViewById(R.id.history);
        historyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toHistory);
            }
        });

        Button validButton = (Button) findViewById(R.id.validButton);
        validButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toActiveProducts);
            }
        });
    }

}
