package com.example.android.adda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail;
    private EditText editPassword;
    private Button btnRegister;
    private TextView textView;
    private TextView proceedToSignInActivityTextView;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public static Activity signUpActy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpActy=this;

        editEmail=(EditText)findViewById(R.id.id_editEmail_SignUpActivity);
        editPassword=(EditText)findViewById(R.id.id_editPassword_SignUpActivity);
        btnRegister=(Button)findViewById(R.id.id_buttonRegister_SignUpActivity);
        textView=(TextView)findViewById(R.id.id_textView_SignUpActivity);
        proceedToSignInActivityTextView=(TextView)findViewById(R.id.id_proceedToSignInTextView_SignUpActivity);

        progressDialog=new ProgressDialog(this);

        databaseReference=FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth=FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(this);
        proceedToSignInActivityTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==btnRegister){
            final String email=editEmail.getText().toString().trim();
            final String password =editPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                textView.setText("Please fill both fields and try again.");
            }
            else{

                if(password.length()<6){
                    textView.setText("Password must contain at least 6 character.");
                }

                progressDialog.setMessage("Registering User...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();


                            databaseReference.child(firebaseAuth.getCurrentUser().getUid().toString()).child("Username").setValue("");

                            Toast.makeText(SignUpActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                            textView.setText("");


                            progressDialog.setMessage("Signing In");
                            progressDialog.show();
                            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressDialog.dismiss();

                                    if(task.isSuccessful()){
                                        startActivity(new Intent(SignUpActivity.this,Boarding.class));
                                        MainActivity.mainActy.finish();
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(SignUpActivity.this, "Couldn't SignIn.\nPlease try again.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                                        finish();
                                    }
                                }
                            });

                        }
                        else{
                            progressDialog.dismiss();
                            textView.setText("Couldn't Register\nPlease try again.");
                        }
                    }
                });

            }
        }

        if (v==proceedToSignInActivityTextView){
            Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }

    }
}


