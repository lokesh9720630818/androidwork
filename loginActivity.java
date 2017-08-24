package com.example.sunsom.gradering;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editusername,editpassword;
    SessionManager session;
    private Button buttonlogin;
    String username,password,name=" ",gusername=" ",gpassword=" ";
    private ProgressDialog loading;
    TextView showw,h1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        session = new SessionManager(getApplicationContext());
        editusername = (EditText) findViewById(R.id.username1);
        h1=(TextView)findViewById(R.id.h1);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#03ae3c")));


        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/GreatVibes-Regular.otf");

        h1.setTypeface(custom_font);
        editpassword = (EditText) findViewById(R.id.password1);
        buttonlogin=(Button)findViewById(R.id.login2);
        buttonlogin.setOnClickListener(this);
    }

    private void getData() {
        username = editusername.getText().toString().trim();
        password = editpassword.getText().toString().trim();
        if (username.equals("") && password.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = Config.DATA_URL+editpassword.getText().toString().trim();

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
                        Toast.makeText(loginActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.KEY_NAME);
            gusername = collegeData.getString(Config.KEY_USERNAME);
            gpassword = collegeData.getString(Config.KEY_PASSWORD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(gusername.equals(" ") || gpassword.equals(" ")){
            Toast.makeText(getApplication(),"Please register to login",Toast.LENGTH_LONG).show();
        }
        else if(username.equals(gusername) || password.equals(gpassword)){
            Toast.makeText(loginActivity.this, "Login ", Toast.LENGTH_SHORT).show();
            session.createLoginSession(username,password);
            Intent i=new Intent(loginActivity.this,userpageActivity.class);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(getApplication(),"Please register to login",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClick(View v) {
        getData();
    }

}