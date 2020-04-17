package com.example.instaclone_20;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText emailLog,passwordLog;
    ProgressBar progressBarLog;
    TextView showMessageLog,registerLink;
    Button buttonLog;

    Intent HomeActivity, RegisterActivity ;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLog=findViewById(R.id.emailLog);
        passwordLog=findViewById(R.id.passwordLog);
        progressBarLog=findViewById(R.id.progressBarLog);
        buttonLog=findViewById(R.id.buttonLog);
        showMessageLog=findViewById(R.id.showMessageLog);
        registerLink=findViewById(R.id.registerLinkTextView);


        HomeActivity = new Intent(LoginActivity.this, com.example.instaclone_20.HomeActivity.class);
        RegisterActivity= new Intent(this, com.example.instaclone_20.RegisterActivity.class);

        auth=FirebaseAuth.getInstance();

        progressBarLog.setVisibility(View.INVISIBLE);
        showMessageLog.setVisibility(View.INVISIBLE);


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity);
            }
        });




        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLog.setVisibility(View.INVISIBLE);
                progressBarLog.setVisibility(View.VISIBLE);

                final String email = emailLog.getText().toString();
                final String password= passwordLog.getText().toString();

                if (email.isEmpty()||password.isEmpty()){
                    showMessage("Check Your Input",1);
                    buttonLog.setVisibility(View.VISIBLE);
                    progressBarLog.setVisibility(View.INVISIBLE);

                }
                else{
                    logIn(email,password);
                }
            }
        });

    }

    private void logIn(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    showMessage("Log In Succesfully ",0);
                    newActivityOpen();

                }else{
                    showMessage(task.getException().toString(),1);
                    buttonLog.setVisibility(View.VISIBLE);
                    progressBarLog.setVisibility(View.INVISIBLE);

                }
            }
        });

    }

    private void newActivityOpen() {
            startActivity(HomeActivity);
            finish();
    }

    private void showMessage(String s,int v) {

        // 0 for Toast and 1 for Text
        if(v==0){
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }else if(v==1){
            showMessageLog.setVisibility(View.VISIBLE);
            showMessageLog.setText(s);

        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if ( user != null){
            // user is already connected so we need to redirect
            newActivityOpen();

        }

    }
}
