package com.example.android.adda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DecisionActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public static Activity decActy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);

        decActy=this;

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Signing In");
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.show();

        databaseReference.child(firebaseAuth.getCurrentUser().getUid().toString()).child("Username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(TextUtils.isEmpty(dataSnapshot.getValue().toString())){

                    startActivity(new Intent(DecisionActivity.this,SetUpUsernameActivity.class));
                    progressDialog.dismiss();
                    if(SignInActivity.signInacty!=null){
                        SignInActivity.signInacty.finish();
                    }
                    if(SetUpUsernameActivity.setUnameActy!=null){
                        SetUpUsernameActivity.setUnameActy.finish();
                    }
                    if(SignUpActivity.signUpActy!=null){
                        SignUpActivity.signUpActy.finish();
                    }
                }
                else {
                    startActivity(new Intent(DecisionActivity.this,HomeActivity.class));
                    progressDialog.dismiss();
                    if(SignInActivity.signInacty!=null){
                        SignInActivity.signInacty.finish();
                    }
                    if(SetUpUsernameActivity.setUnameActy!=null){
                        SetUpUsernameActivity.setUnameActy.finish();
                    }
                    if(SignUpActivity.signUpActy!=null){
                        SignUpActivity.signUpActy.finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
