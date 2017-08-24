package com.example.sunsom.gradering;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunsom on 24/8/17.
 */

public class reviews extends AppCompatActivity {
    List<String> a=new ArrayList<>();
    List<String> b=new ArrayList<>();
    Button submitrate;
    LinearLayout ll;
    String book_user,value;
    ListViewAdapter lviewadapter;
    ListView lview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        value = getIntent().getExtras().getString("key");
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
        Button b=(Button)findViewById(R.id.lllll);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookreviews();
            }
        });
    }



    private void bookreviews(){
        value.toUpperCase();
        if (value.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }

        String url = Config.DATA_URL_BOOK_REVIEW+value.trim().toUpperCase().replace(" ","%20");

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showreviews(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(reviews.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showreviews(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY_BOOK);
            String s1=" ",s2=" ";
            for(int i=0;i<result.length();i++){
                JSONObject jo=result.getJSONObject(i);
                s1=jo.getString(Config.KEY_USERNAME);
                s2=jo.getString(Config.KEY_DETAILS);
                a.add(i,s1);
                b.add(i,s2);
                if(i>5)
                    break;
                //Toast.makeText(getApplicationContext(),a[i],Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),b[i],Toast.LENGTH_LONG).show();

            }
            if(s1.equals(" ") || s2.equals(" ")){
                Toast.makeText(getApplicationContext(),"No review available",Toast.LENGTH_LONG).show();
            }
            else {
                lviewadapter = new ListViewAdapter(this, a, b);

                System.out.println("adapter => "+lviewadapter.getCount());
                Toast.makeText(getApplicationContext(),"get count "+lviewadapter.getCount(),Toast.LENGTH_LONG).show();

                lview.setAdapter(lviewadapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
