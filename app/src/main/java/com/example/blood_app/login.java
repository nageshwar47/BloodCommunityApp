package com.example.blood_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    private FirebaseAuth mauth;
    TextView et_email1,et_password1;
    TextView tv_registrer,tv_forgot;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // attaching with id

        et_email1=findViewById(R.id.et_email1);
        et_password1=findViewById(R.id.et_password1);
        btn_login=findViewById(R.id.btn_login);
        tv_forgot=findViewById(R.id.tv_forgot);
        tv_registrer=findViewById(R.id.tv_register);

        //database object

        mauth=FirebaseAuth.getInstance();

        // adding action listener to the buttons

        tv_registrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go=new Intent(getApplicationContext(),donor.class);
                startActivity(go);
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go=new Intent(getApplicationContext(),forgot.class);
                startActivity(go);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email,password;
                email=et_email1.getText().toString();
                password=et_password1.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(login.this, "please enter mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(login.this, "please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }



                mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(mauth.getCurrentUser().isEmailVerified())  //check email is verified or not
                            {
                                Toast.makeText(login.this, "login successfully", Toast.LENGTH_LONG).show();
                                finish();
                                Intent go2=new Intent(getApplicationContext(),info.class);
                                startActivity(go2);
                            }

                            else
                            {
                                Toast.makeText(login.this, "first verify the mail", Toast.LENGTH_SHORT).show();

                                //send mail for verification
                                mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(login.this, "check mail for verification", Toast.LENGTH_SHORT).show();
                                        }

                                        else
                                        {
                                            Toast.makeText(login.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }

                        }
                        else
                        {
                            Toast.makeText(login.this, "login failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });





            }
        });



    }
}