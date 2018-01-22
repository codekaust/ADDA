package com.example.android.adda;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private TextView signIn,signUp;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    public static Activity mainActy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActy=this;

        signIn=(TextView)findViewById(R.id.id_signIn_MainActivity);
        signUp=(TextView)findViewById(R.id.id_signUp_MainActivity);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });


        firebaseAuth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this,DecisionActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(DecisionActivity.decActy!=null){
            DecisionActivity.decActy.finish();
        }

        if(Boarding.boardingacty!=null){
            Boarding.boardingacty.finish();
        }

        if(SetUpUsernameActivity.setUnameActy!=null){
            SetUpUsernameActivity.setUnameActy.finish();
        }
        finish();

    }
}

