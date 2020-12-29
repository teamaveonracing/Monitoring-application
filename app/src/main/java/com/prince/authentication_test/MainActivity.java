package com.prince.authentication_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {

    TextView go_to_sign_up;
    EditText sign_in_username;
    EditText signin_password;
    Button signin_button;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF="sharedPrefs";
    public static final String UID="userId";
    public static final String PASSWORD="password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        mAuth=FirebaseAuth.getInstance();
        final String emailSignin=sharedPreferences.getString(UID,"");
        final String passwordSignin=sharedPreferences.getString(PASSWORD,"");

        mAuth.signInWithEmailAndPassword(emailSignin,passwordSignin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent intent=new Intent(MainActivity.this,ProfileActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage()+" or not logged in on this device", Toast.LENGTH_SHORT).show();
                }
            }
        });












        go_to_sign_up=findViewById(R.id.go_to_sign_up);
        sign_in_username=findViewById(R.id.signin_username);
        signin_password=findViewById(R.id.signin_password);
        signin_button=findViewById(R.id.signin_button);

        go_to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailSignin=sign_in_username.getText().toString().trim();
                final String passwordSignin=signin_password.getText().toString().trim();

                if(emailSignin.isEmpty()){
                    sign_in_username.setError("Email is required");
                    sign_in_username.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailSignin).matches()){
                    sign_in_username.setError("Please enter a valid email");
                    sign_in_username.requestFocus();
                    return;
                }
                if(passwordSignin.isEmpty()) {
                    signin_password.setError("Password is required");
                    signin_password.requestFocus();
                    return;
                }
                if(passwordSignin.length()<6){
                    signin_password.setError("Minimum length of password should be 6");
                    signin_password.requestFocus();
                    return;
                }


                mAuth.signInWithEmailAndPassword(emailSignin,passwordSignin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){

                         editor.putString(UID,emailSignin);
                         editor.putString(PASSWORD,passwordSignin);
                         editor.commit();



                       Intent intent=new Intent(MainActivity.this,ProfileActivity.class);

                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(intent);
                     }else{
                         Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                    }
                });
            }
        });


    }
}
