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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText editEmail;
    private EditText editPassword;
    private Button btnSignIn;
    private TextView toSignUpTextView;
    private TextView textView;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    public static Activity signInacty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInacty=this;

        editEmail=(EditText)findViewById(R.id.id_editEmail_signInActivity);
        editPassword=(EditText)findViewById(R.id.id_editPassword_signInActivity);
        btnSignIn=(Button)findViewById(R.id.id_signInButton_SignInActivity);
        toSignUpTextView=(TextView)findViewById(R.id.id_proceedToSignUpTextView_SignInActivity);
        textView=(TextView)findViewById(R.id.id_textView_SignInActivity);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        toSignUpTextView.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==toSignUpTextView){
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        }

        if (v==btnSignIn){
            String email=editEmail.getText().toString().trim();
            String password=editPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                textView.setText("Please fill both fields and try again.");
            }
            else{
                progressDialog.setMessage("Signing In");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            textView.setText("");
                            startActivity(new Intent(SignInActivity.this,DecisionActivity.class));
                            MainActivity.mainActy.finish();
                        }
                        else{
                            textView.setText("Couldn't Sign In.\nPlease try again.");
                        }
                    }
                });
            }
        }

    }
}
