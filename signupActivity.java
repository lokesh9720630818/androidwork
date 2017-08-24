package com.example.sunsom.gradering;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class signupActivity extends AppCompatActivity {

    private EditText etname;
    private EditText etusername;
    private EditText etpassword;
    private EditText etcpassword;
    private EditText etphone;
    private EditText etemail;
    homepage sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);
        etname=(EditText)findViewById(R.id.name2);
        etusername=(EditText)findViewById(R.id.username2);
        etpassword=(EditText)findViewById(R.id.password2);
        etcpassword=(EditText)findViewById(R.id.cpassword2);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#03ae3c")));


        etemail=(EditText)findViewById(R.id.email2);
        etphone=(EditText)findViewById(R.id.phone2);
    }
    public void insert(View view){
        int flag=0;
        String name=etname.getText().toString();
        String username=etusername.getText().toString();
        String password=etpassword.getText().toString();
        String cpassword=etcpassword.getText().toString();
        String phone=etphone.getText().toString();
        String email=etemail.getText().toString();
                if (name.length() <= 3) {
                    etname.setError("Enter your name");
                    etname.requestFocus();
                    flag=1;

                }
                if (username.length() <= 5) {
                    etusername.setError("Username contains at least 6 character");
                    etusername.requestFocus();
                    flag=1;

                }
                if (etpassword.getText().toString().length() <= 7) {
                    etpassword.setError("Password must have at least 8 character");
                    etpassword.requestFocus();
                    flag=1;

                }
                if (!password.equals(cpassword)) {
                    etcpassword.setError("Password should match");
                    etcpassword.requestFocus();
                    flag=1;

                }
                if (etphone.getText().toString().length()<= 9) {
                    etphone.setError("Enter your mobile number");
                    etphone.requestFocus();
                    flag=1;
                }
                String str="gmail.com";
                if (etemail.getText().toString().endsWith(str)) {
                    etemail.setError("Enter correct email");
                    etemail.requestFocus();
                    flag=1;

                }
                if(flag==0)
                    insertToDatabase(name,username,password,phone,email);
    }

    private void insertToDatabase(final String name,final String username,final String password,final String phone,final String email){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("phone", phone));
                nameValuePairs.add(new BasicNameValuePair("email", email));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://ratebooks.000webhostapp.com/user_info.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                Intent i=new Intent(signupActivity.this,loginActivity.class);
                startActivity(i);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, username, password, phone, email);
    }
}
