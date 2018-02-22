package com.genesis.taxiontouch.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.genesis.taxiontouch.R;
import com.genesis.taxiontouch.setip.IpConfig;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserRegistrationActivity extends AppCompatActivity {
    EditText nameEditText = null;
    EditText phoneNumberEditText = null;
    EditText passwordEditText = null;
    Button signUpButton = null;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        nameEditText = findViewById(R.id.nameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEditText.getText().toString().equals("")) {
                    nameEditText.setError("Please enter name");
                } else if (phoneNumberEditText.getText().toString().equals("")) {
                    phoneNumberEditText.setError("Please enter phone number");
                } else if (passwordEditText.getText().toString().equals("")) {
                    passwordEditText.setError("Please enter a password");
                } else {
                    String name = nameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String phone = phoneNumberEditText.getText().toString();

                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UserRegistrationActivity.this);
                    if (ActivityCompat.checkSelfPermission(UserRegistrationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserRegistrationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(UserRegistrationActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        String latitude=location.getLatitude()+"";
                                        Toast.makeText(UserRegistrationActivity.this, latitude, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }
    class SignUp extends AsyncTask<String,Void,String>{
        String REGISTRATION_URL="http//:"+ IpConfig.ip+"taxi_on_touch/user_reg.php";

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
