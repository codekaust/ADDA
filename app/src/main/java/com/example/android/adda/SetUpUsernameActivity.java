package com.example.android.adda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;

public class SetUpUsernameActivity extends AppCompatActivity {

    private EditText editUsername;
    private TextView proceedTextView;
    private TextView textView;
    private Toolbar toolbar;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_username);

        toolbar = (Toolbar) findViewById(R.id.id_toolbar_SetUpUsernameActivity);
        setSupportActionBar(toolbar);

        editUsername=(EditText)findViewById(R.id.id_editUsername_SetUpUsernameActivity);
        proceedTextView=(TextView)findViewById(R.id.id_proceedTextView_SetUpUsernameActivity);
        textView=(TextView)findViewById(R.id.id_displayTextView_SetUpUsernameActivity);

        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        proceedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(editUsername.getText().toString())){
                    textView.setText("Please enter chosen username.");
                }
                else{
                    String a ="#"+editUsername.getText().toString().trim();
                    setUsername(a,firebaseAuth.getCurrentUser().getUid().toString());
                    startActivity(new Intent(SetUpUsernameActivity.this,HomeActivity.class));
                }
            }
        });

    }

    public void setUsername(String username, String uid){
        databaseReference.child(uid).child("Username").setValue(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_set_up_username_activity,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.id_logout_menuSetUpUsername){
            firebaseAuth.signOut();
            startActivity(new Intent(SetUpUsernameActivity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
