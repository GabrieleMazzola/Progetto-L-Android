package com.ticket.iseimoschettieri.tickettestagain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class CollectorHub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
/*
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(214,35,12)));
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkRed));
        }*/
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final Intent toLoginPage = new Intent(this,Login.class);
        final Intent toCheckTicket = new Intent(this,CheckTicket.class);
        final Intent toMakeFine = new Intent(this,MakeFine.class);
        //String message = intent.getStringExtra("COLLECTOR_NAME");
        // Capture the layout's TextView and set the string as its text
        //TextView loggedCheers = (TextView) findViewById(R.id.loggedMessage);
        //loggedCheers.setText("Logged in as: "+ message);

        Button logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoHandler.logout(getApplicationContext());
                startActivity(toLoginPage);
            }
        });

        Button checkTicketButton = (Button) findViewById(R.id.checkTicket);
        checkTicketButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toCheckTicket);
            }
        });

        Button makefineButton = (Button) findViewById(R.id.makeFine);
        makefineButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toMakeFine);
            }
        });
    }

}
