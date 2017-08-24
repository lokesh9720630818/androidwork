package com.example.sunsom.gradering;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class searchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editsearchbook,et_details;
    private Button buttonsearch;
    TextView t1,t2,t3,t5,t6,reviews;
    RatingBar rb;
    Float x,y,z;
    Button submitrate;
    LinearLayout ll;
    SessionManager session;
    String bookname,r,busername=" ",bpassword="",bbookname="", gbookname=" ",gwriter=" ",grating=" ",gdetails=" ",gvotes=" ",name,email;
    private ProgressDialog loading;
    TextView showw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity);
        t1=(TextView)findViewById(R.id.showbookname);
        ll=(LinearLayout)findViewById(R.id.ll3);
        t2=(TextView)findViewById(R.id.showwriter);
        t3=(TextView)findViewById(R.id.showbookrating);
        t5=(TextView)findViewById(R.id.detail);
        et_details=(EditText)findViewById(R.id.detailss);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#5e9c00")));

        t6=(TextView)findViewById(R.id.votes);
        rb=(RatingBar)findViewById(R.id.ratingbar);
        submitrate=(Button)findViewById(R.id.RateButton);
        editsearchbook = (EditText) findViewById(R.id.searchbook);
        buttonsearch=(Button)findViewById(R.id.sbookbutton);
        session=new SessionManager(getApplicationContext());
        buttonsearch.setOnClickListener(this);
        TextView lblName = (TextView) findViewById(R.id.showname);
        ll.setVisibility(View.INVISIBLE);
            submitrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (session.isLoggedIn()) {
                        if (bbookname.toUpperCase().equals(gbookname.toUpperCase())) {
                            Toast.makeText(getApplicationContext(), "Already rated", Toast.LENGTH_LONG).show();
                        }
                        else if(et_details.getText().toString().equals(" ")){
                            Toast.makeText(getApplicationContext(),"Please write a review!",Toast.LENGTH_LONG).show();

                        }
                        else {
                            x = rb.getRating();
                            y = Float.parseFloat(grating);
                            z = Float.parseFloat(gvotes);
                            z++;
                            y = (x + (z - 1) * y) / z;
                            r = y.toString();
                            updateData();
                            addbook();
                            Toast.makeText(getApplicationContext(), "Thanks for rating the book ", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please login", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        // Button logout

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        if(session.isLoggedIn()){
            session.checkLogin();

            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // name
            name = user.get(SessionManager.KEY_NAME);

            // email
            email = user.get(SessionManager.KEY_EMAIL);
            // displaying user data
            lblName.setText(name);
            lblName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(searchActivity.this, userpageActivity.class);
                    startActivity(i);
                }
            });
        }
        else{
            lblName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(searchActivity.this, loginActivity.class);
                    startActivity(i);
                }
            });
        }

    }
    
    private void updateData() {
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        String url = Config.DATA_URL_RATE+gbookname.trim().replace(" ","%20")+"&gvotes="+z.toString().trim()+"&rating="+r.trim();
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(searchActivity.this,"connetion slow",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(searchActivity.this,searchActivity.class);
                        startActivity(i);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addbook() {
        String url = Config.DATA_URL_ADDBOOK+name.trim()+"&password="+email.trim()+"&bookname="+gbookname.trim().replace(" ","%20")+"&rating="+x.toString().trim()+"&details="+et_details.getText().toString().replace(" ","%20");
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(searchActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getbookData() {
        //Toast.makeText(getApplicationContext(),busername+" "+bpassword+" "+bbookname,Toast.LENGTH_LONG).show();
        String url = Config.DATA_URL_searchBOOK+name+"&password="+email+"&bookname="+editsearchbook.getText().toString().trim().toUpperCase().replace(" ","%20");
       //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                check(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(searchActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
       // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void check(String response){
        try {
         //   Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY_GBOOK);
            JSONObject collegeData = result.getJSONObject(0);
            busername = collegeData.getString(Config.KEY_USERNAME);
            bpassword = collegeData.getString(Config.KEY_BPASSWORD);
            bbookname = collegeData.getString(Config.KEY_BBOOKNAME);
          //  Toast.makeText(getApplicationContext(),"bbookname"+bbookname+"   gbookname"+gbookname,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getData() {
        bookname = editsearchbook.getText().toString().trim();
        bookname.toUpperCase();
        if (bookname.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = Config.DATA_URL_Book+editsearchbook.getText().toString().trim().toUpperCase().replace(" ","%20");

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(searchActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //Toast.makeText(getApplicationContext(),"koi ni  ",Toast.LENGTH_LONG).show();
    }
    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY_BOOK);
            JSONObject collegeData = result.getJSONObject(0);
            gbookname = collegeData.getString(Config.KEY_BOOKNAME);
            gwriter = collegeData.getString(Config.KEY_WRITER);
            grating = collegeData.getString(Config.KEY_RATING);
            gdetails=collegeData.getString(Config.KEY_DETAIL);
            gvotes=collegeData.getString(Config.KEY_VOTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bookname.toUpperCase().equals(gbookname.toUpperCase())){
            ll.setVisibility(View.VISIBLE);
            t1.setText("BOOKNAME: "+gbookname);
            t2.setText("WRITER: "+gwriter);
            t3.setText("RATING: "+grating);
            t5.setText("DETAIL: "+gdetails);
            t6.setText("VOTES: "+gvotes);
            ll.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Sorry! No book found.",Toast.LENGTH_LONG).show();
           }

    }


    @Override
    public void onClick(View v) {
        getData();
        getbookData();
    }

}
