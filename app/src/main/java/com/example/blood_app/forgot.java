package com.example.blood_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class forgot extends AppCompatActivity {

    EditText et_mail2;
    Button btn_forgot;

    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        et_mail2=findViewById(R.id.et_email2);
        btn_forgot=findViewById(R.id.btn_forgot);

        mauth=FirebaseAuth.getInstance();

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=et_mail2.getText().toString();

                if(TextUtils.isEmpty(mail))
                {

                    Toast.makeText(forgot.this, "please enter mail for reset password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mauth.sendPasswordResetEmail(mail).addOnCompleteListener(forgot.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(forgot.this, "link sent", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(forgot.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}