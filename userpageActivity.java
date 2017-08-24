package com.example.sunsom.gradering;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class userpageActivity extends AppCompatActivity {


    // Session Manager Class
    SessionManager session;
    Button b1;
    homepage user;

    // Button Logout

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpageactivity);

        // Session class instance
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#03ae3c")));
        session = new SessionManager(getApplicationContext());
        b1=(Button)findViewById(R.id.search_button2);



        TextView lblName = (TextView) findViewById(R.id.name3);
        TextView logout = (TextView) findViewById(R.id.logout);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(userpageActivity.this,searchActivity.class);
                startActivity(intent);
            }
        });


        // Button logout
       // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        final HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
        lblName.setText(Html.fromHtml(name));
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                session.logoutUser();
            }
        });


    }

}