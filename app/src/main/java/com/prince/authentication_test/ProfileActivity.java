package com.prince.authentication_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    TextView tvId;
    Button signout;
    Button go_to_map;
    Button tempGraph;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF="sharedPrefs";
    public static final String UID="userId";
    public static final String PASSWORD="password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvId=findViewById(R.id.tvId);
        signout=findViewById(R.id.button_signout);
        mAuth=FirebaseAuth.getInstance();
        go_to_map=findViewById(R.id.go_to_map);
        tempGraph=findViewById(R.id.temp_Graph);



        sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();

        go_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                editor.putString(UID," ");
                editor.putString(PASSWORD," ");

                editor.commit();

                Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        tempGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,GraphActivity.class);
                startActivity(intent);
                Log.d("refffffffffffffffffffffffffffffffffffffffffffff", "values:mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
            }
        });
    }
}
