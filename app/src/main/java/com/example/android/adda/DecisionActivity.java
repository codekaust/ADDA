package com.example.android.adda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DecisionActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);

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
                }
                else {
                    startActivity(new Intent(DecisionActivity.this,HomeActivity.class));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
