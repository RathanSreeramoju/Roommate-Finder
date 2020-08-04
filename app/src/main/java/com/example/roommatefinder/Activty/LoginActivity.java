package com.example.roommatefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText emailId, password;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        emailId=findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        progressBar=findViewById(R.id.progressbar);

        findViewById(R.id.textView1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    private void userLogin(){
        String Email=emailId.getText().toString().trim();
        String Password=password.getText().toString().trim();
        if(Email.isEmpty()){
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
            password.setError("password id required");
            password.requestFocus();
            return;
        }
        if(Password.length()<4){
            password.setError("Minimum length of Password should be 6");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    finish();
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.textView1:
                finish();
                startActivity(new Intent(this, SignupActivity.class));

                break;
            case R.id.button2:
                userLogin();
                break;

        }
    }
}