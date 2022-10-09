package com.example.blood_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class donor extends AppCompatActivity {

    EditText et_name,et_loc,et_email,et_pass,et_num;
    Button btn_reg;
    TextView tv_login;
    FusedLocationProviderClient mFusedLocationClient;
    double log;
    double lat;
    String address="";
    // creating object


    int PERMISSION_ID = 44;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //initialize the firebase
        mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("donor");




        // assiging id to the object
        et_name=findViewById(R.id.et_name);
        et_loc=findViewById(R.id.et_loc);
        et_email=findViewById(R.id.et_email);
        et_pass=findViewById(R.id.et_pass);
        et_num=findViewById(R.id.et_contact);
        tv_login=findViewById(R.id.tv_login);
        btn_reg=findViewById(R.id.btn_register);

        // fetch location

        et_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                Toast.makeText(donor.this, "log:"+log+"\nlat:"+lat, Toast.LENGTH_SHORT).show();
                //Toast.makeText(donor.this, "", Toast.LENGTH_SHORT).show();

                // converrting latitude to address
                lattoaddress(lat,log);
            }


        });





        //Toast.makeText(this, "name:"+name+"\nloc:"+loc+"\nemail:"+email+"\npass:"+pass+"\nnum:"+num, Toast.LENGTH_SHORT).show();




        // adding listener to the buttons

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent=new Intent(getApplicationContext(),login.class);
                startActivity(intent);*/

                Intent move=new Intent(getApplicationContext(),login.class);
                startActivity(move);

            }
        });


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> m= new HashMap<>();  //hashmap object for personal details

                String name,loc,email,pass,num;

                // store in string

                name=et_name.getText().toString();
                loc=et_loc.getText().toString();
                email=et_email.getText().toString();
                pass=et_pass.getText().toString();
                num=et_num.getText().toString();




                //add value in hashmap
                m.put("name",name);
                m.put("location",address);
                m.put("number",num);
                m.put("latitude",lat);
                m.put("logitude",log);
                m.put("A","0");
                m.put("B","0");
                m.put("o","0");
                m.put("AB","0");
                m.put("a","0");
                m.put("b","0");
                m.put("O","0");
                m.put("ab","0");


                //text field validation

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(donor.this, "please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(loc))
                {
                    Toast.makeText(donor.this, "please enter location", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(donor.this, "please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(num))
                {
                    Toast.makeText(donor.this, "please enter contact number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(donor.this, "please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }


                mauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(donor.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {

                            myref.child(mauth.getUid()).setValue(m);

                            finish();

                            //send verification mail
                            mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(donor.this, "check your mail for verification", Toast.LENGTH_SHORT).show();

                                    }

                                    else
                                    {
                                        Toast.makeText(donor.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            Toast.makeText(donor.this, "registered successfully", Toast.LENGTH_LONG).show();

                            Intent go=new Intent(getApplicationContext(),login.class);
                            startActivity(go);

                        }

                        else
                        {
                            try
                            {
                                throw task.getException();

                            }catch (FirebaseAuthWeakPasswordException e)
                            {
                                et_pass.setError("your password is to weak");
                                et_pass.requestFocus();
                            }

                            catch (FirebaseAuthInvalidCredentialsException e)
                            {
                                et_email.setError("your email is invalid");
                                et_email.requestFocus();
                            }

                            catch (FirebaseAuthUserCollisionException e)
                            {
                                et_email.setError("your email is already exists");
                                et_email.requestFocus();
                            }

                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            Toast.makeText(donor.this, "registeration failed", Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
        });




    }


    private void lattoaddress(double lat,double log) {

        Geocoder geocoder=new Geocoder(donor.this, Locale.getDefault());
        try {
            List<Address> addresses=geocoder.getFromLocation(lat,log,1);
            if(address!=null)
            {
                Address returnAddress=addresses.get(0);
                StringBuilder stringBuilderReturnaddress=new StringBuilder("");

                for (int i=0;i<=returnAddress.getMaxAddressLineIndex();i++)
                {
                    stringBuilderReturnaddress.append(returnAddress.getAddressLine(i)).append("\n");
                }
                address=stringBuilderReturnaddress.toString();


                et_loc.setText(address);
            }


        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            lat=location.getLatitude();
                            log=location.getLongitude();
                            //et_loc.setText(location.getLatitude() + "");
                           // longitTextView.setText(location.getLongitude() + "");
                        }

                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
       
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       // mLocationRequest.setInterval(5);
       // mLocationRequest.setFastestInterval(0);
        //mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Void> voidTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            lat=mLastLocation.getLatitude();
            log=mLastLocation.getLongitude();
            //et_loc.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}