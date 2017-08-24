package com.example.sunsom.gradering;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import android.net.ConnectivityManager;

public class homepage extends AppCompatActivity {
    Button login1;
    TextView register1,skip,t1,t2;
    private static int SPLASH_TIME_OUT = 3000;
    SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/GreatVibes-Regular.otf");

        t1.setTypeface(custom_font);
        t2.setTypeface(custom_font);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#03ae3c")));

        sm=new SessionManager(getApplicationContext());
        register1=(TextView)findViewById(R.id.register1);
        skip=(TextView)findViewById(R.id.skip);
        skip.setTypeface(custom_font);
        login1=(Button)findViewById(R.id.login1);
        if (sm.isLoggedIn()) {
            Intent i = new Intent(homepage.this, searchActivity.class);
            startActivity(i);
        }
        if(!checkInternetConenction()){
            Toast.makeText(getApplicationContext(),"Please connect to the internet",Toast.LENGTH_LONG).show();
        }
        else {
            if (sm.isLoggedIn()) {
                Intent j = new Intent(homepage.this, searchActivity.class);
                startActivity(j);
            }
            login1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sm.isLoggedIn()) {
                        Toast.makeText(getApplicationContext(), "Already login", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(homepage.this, searchActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(homepage.this, loginActivity.class);
                        startActivity(i);
                    }
                }
            });
            register1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sm.isLoggedIn()) {
                        Toast.makeText(getApplicationContext(), "Already login.Please logout to register.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(homepage.this, userpageActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(homepage.this, signupActivity.class);
                        startActivity(i);
                    }
                }
            });
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(homepage.this, searchActivity.class);
                    startActivity(i);
                }
            });
        }
    }
    public boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }

}
