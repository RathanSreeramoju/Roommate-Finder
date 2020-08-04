package com.example.roommatefinder.Activty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.singletone.ProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText emailId, password;
    private MySharedPref sharedPref;
    private Context  context;
    private Intent intent ;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        intent = new Intent(LoginActivity.this, MainActivity.class);
        //init MySharedPref
        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        System.out.println("shared pref obj__"+sharedPref);

        mAuth=FirebaseAuth.getInstance();

        emailId=findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progressbar);


        findViewById(R.id.textView1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    private void userLogin(){
//        ProgressDialog.progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        final String Email=emailId.getText().toString().trim();
        String Password=password.getText().toString().trim();
        if(Email.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            emailId.setError("Email id required");
            emailId.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            emailId.setError("Please enter a valid Email");
            emailId.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            password.setError("password id required");
            password.requestFocus();
            return;
        }
        if(Password.length()<4){
            password.setError("Minimum length of Password should be 6");
            password.requestFocus();
            return;
        }



        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                ProgressDialog.progressDialog.dismiss();

//                System.out.println("is email verified___"+mAuth.getCurrentUser().isEmailVerified());
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        if(mAuth.getCurrentUser()!=null){

                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                progressBar.setVisibility(View.INVISIBLE);
//                           sharedPref.loggedin=true;
                                sharedPref.setAppLogIn(true);
                                System.out.println("emailID___"+Email);
                                sharedPref.setEmailID(Email);
                                sharedPref.setUserId(mAuth.getCurrentUser().getUid());
                                sharedPref.getUserLoggedInData();

                                System.out.println( " UserId" + mAuth.getCurrentUser().getUid()+"  userId"+mAuth.getUid()+"user_id_"+sharedPref.UserId);

                                intent.putExtra("emailId",Email);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Please Verify Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.textView1:

                startActivity(new Intent(this, SignupActivity.class));
                finish();
                break;
            case R.id.button2:
                userLogin();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}