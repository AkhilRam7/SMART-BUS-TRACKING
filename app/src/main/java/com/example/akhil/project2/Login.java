package com.example.akhil.project2;



import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button signin,signup;
    EditText user,pass;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "BusAppPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.userid);
        pass = (EditText)findViewById(R.id.password);
        signin = (Button)findViewById(R.id.in);
        signup = (Button)findViewById(R.id.up);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        pref = this.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


        firebaseAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().length() ==0 || pass.getText().toString().length() ==0){
                    Toast.makeText(Login.this, "MobileNumber is missing!", Toast.LENGTH_LONG).show();
                }
                else if(pass.getText().toString().length()==0){
                    Toast.makeText(Login.this, "Password is missing!", Toast.LENGTH_LONG).show();
                }
                else {
                    loginuser();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void loginuser(){
        String id=user.getText().toString();
        final String password=pass.getText().toString();
        id=id+"@smartbus.com";

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(id,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    createLoginSession(user.getText().toString());
                    Toast.makeText(Login.this, "logged in successfully..", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "login failed. Try again...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void createLoginSession(String name){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PHONE, name);
        editor.commit();
    }


}

