package com.nitukbt19.StudentBuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nitukbt19.StudentBuddy.Models.Users;
import com.nitukbt19.StudentBuddy.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Welcome "+binding.inputUsername.getText().toString());
        binding.alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                //matching confirm password
                if(binding.inputPasswordRegister2.getText().toString().equals(binding.inputConfirmPassword1.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(binding.etEmailAddress.getText().toString(), binding.inputPasswordRegister2.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Users user=new Users(binding.inputUsername.getText().toString(),binding.etEmailAddress.getText().toString(),binding.inputPasswordRegister2.getText().toString());
                                        String id=task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(user);
                                        Toast.makeText(RegisterActivity.this, "Successfully Registered in Student Buddy", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }else{
                    binding.inputPasswordRegister2.setText("");
                    binding.inputConfirmPassword1.setText("");
                    binding.inputPasswordRegister2.setHintTextColor(Color.RED);
                    binding.inputConfirmPassword1.setHintTextColor(Color.RED);
                }

            }
        });

    }
}