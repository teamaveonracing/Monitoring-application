package com.prince.authentication_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity {

    TextView go_to_sign_in;
    Button button_signup;
    EditText editTextEmail;
    EditText editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        go_to_sign_in=findViewById(R.id.go_to_sign_in);
        button_signup=findViewById(R.id.signup_button);
        editTextEmail=findViewById(R.id.signup_username);
        editTextPassword=findViewById(R.id.signup_password);
        mAuth = FirebaseAuth.getInstance();


        button_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String emailSignup=editTextEmail.getText().toString().trim();
                String passwordSignup=editTextPassword.getText().toString().trim();

                if(emailSignup.isEmpty()){
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailSignup).matches()){
                    editTextEmail.setError("Please enter a valid email");
                    editTextEmail.requestFocus();
                    return;
                }
                if(passwordSignup.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                }
                if(passwordSignup.length()<6){
                    editTextPassword.setError("Minimum length of password should be 6");
                    editTextPassword.requestFocus();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(emailSignup,passwordSignup).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignupActivity.this,ProfileActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignupActivity.this, "you are already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });

        go_to_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
