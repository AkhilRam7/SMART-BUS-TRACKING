package com.example.akhil.project2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button bSignup;
    EditText etFirstName,etLastName,etEmail,etMobileNumber,etPassword,etConfirmPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase db;
    private DatabaseReference myref;
    private ProgressBar progressbar;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "BusAppPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_PHONE = "phone";

    //
    RequestQueue requestQueue;
    String url ="http://192.168.43.113/insertuser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        bSignup = (Button) findViewById(R.id.bSignup);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();

        progressbar = (ProgressBar)findViewById(R.id.progressBar);

        pref = this.getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        db = FirebaseDatabase.getInstance();
        myref = db.getReference();

        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pass = etPassword.getText().toString();
                String CPass = etConfirmPassword.getText().toString();
                if (etFirstName.getText().toString().length() == 0) {
                    Toast.makeText(Register.this, "FirstName is missing!", Toast.LENGTH_LONG).show();
                } else if (etMobileNumber.getText().toString().length() == 0) {
                    Toast.makeText(Register.this, "MobileNumber is missing!", Toast.LENGTH_LONG).show();
                } else if (Pass.length() < 8) {
                    Toast.makeText(Register.this, "Password must contain 8 characters", Toast.LENGTH_LONG).show();
                } else if (Pass.equals(CPass)) {


                    String firstName = etFirstName.getText().toString();
                    String lastName = etLastName.getText().toString();
                    String email = etEmail.getText().toString();
                    String phone= etMobileNumber.getText().toString();

                    HashMap<String,String> dataMap = new HashMap<>();
                    dataMap.put("FirstName",firstName);
                    dataMap.put("LastName",lastName);
                    dataMap.put("Email",email);

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    myref.child(user.getUid()).setValue(dataMap);
                    registeruser();
                    dataentry(firstName,lastName,email,phone,Pass);

                } else {
                    Toast.makeText(Register.this, "Password did not match", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        progressbar.setVisibility(View.GONE);
    }

    public void registeruser(){
        String id = etMobileNumber.getText().toString();
        String password = etPassword.getText().toString();
        id = id + "@smartbus.com";


        progressbar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("check", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        progressbar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Your are a Registered user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent otpp = new Intent(Register.this,MainActivity.class);
                            createLoginSession(etMobileNumber.getText().toString());
                            startActivity(otpp);
                            finish();
                        }

                    }
                });


    }



    public void dataentry(final String a ,final String b,final String c,final String d,final String e){




        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters  = new HashMap<String, String>();
                Log.d("check","start");
                parameters.put("fname", a.toString());
                parameters.put("lname",b.toString());
                parameters.put("email",c.toString());
                parameters.put("phone",d.toString());
                parameters.put("pass",e.toString());
                Log.d("check",d.toString());
                Log.d("check","start");

                return parameters;

            }
        };
        requestQueue.add(request);
    }

    public void createLoginSession(String name){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PHONE, name);
        editor.commit();
    }


}


