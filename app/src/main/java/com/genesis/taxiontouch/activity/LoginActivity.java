package com.genesis.taxiontouch.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.genesis.taxiontouch.R;
import com.genesis.taxiontouch.setip.IpConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    TextView newuserTextView=null;
    EditText usernameEditText=null;
    EditText passwordEditText=null;
    Button loginButton=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText=findViewById(R.id.usernameEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        loginButton=findViewById(R.id.loginButton);
        newuserTextView=findViewById(R.id.newuserTextView);
        newuserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,UserRegistrationActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameEditText.getText().toString().equals("")){
                    usernameEditText.setError("Please enter the username");
                }else if (passwordEditText.getText().toString().equals("")){
                    passwordEditText.setError("Please enter the password");
                }else {
                    String username=usernameEditText.getText().toString();
                    String password=passwordEditText.getText().toString();
                    LoginTask loginTask=new LoginTask();
                    loginTask.execute(username,password);
                }
            }
        });
    }
    class LoginTask extends AsyncTask<String,Void,String>{
        String LOGIN_URL="http//:"+ IpConfig.ip+"taxi_on_touch/user_login.php";

        @Override
        protected String doInBackground(String... strings) {
            String username=strings[0];
            String password=strings[1];
            try {
                URL url=new URL(LOGIN_URL);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line+"\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                String code;
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("server_response");
                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                code=jsonObject1.getString("code");
                if(code.equals("true")){
                    String name=jsonObject1.getString("name");
                    String address=jsonObject1.getString("address");
                    Intent intent=new Intent(LoginActivity.this,UserHomeActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("address",address);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }
}
