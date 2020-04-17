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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    EditText nameReg,secondNameReg,emailReg,password1Reg,password2Reg;
    TextView showMessageReg;
    ProgressBar progressBarReg;
    Button buttonReg;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameReg=findViewById(R.id.nameReg);
        secondNameReg=findViewById(R.id.secondnameReg);
        emailReg=findViewById(R.id.emailReg);
        password1Reg=findViewById(R.id.password1Reg);
        password2Reg=findViewById(R.id.password2Reg);
        buttonReg=findViewById(R.id.buttonReg);
        progressBarReg=findViewById(R.id.progressBarReg);
        showMessageReg=findViewById(R.id.showMessageReg);
        auth=FirebaseAuth.getInstance();
        progressBarReg.setVisibility(View.INVISIBLE);
        showMessageReg.setVisibility(View.INVISIBLE);


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Visibility
                buttonReg.setVisibility(View.INVISIBLE);
                progressBarReg.setVisibility(View.VISIBLE);

                // Strings
                final String name = nameReg.getText().toString();
                final String email = emailReg.getText().toString();
                final String password1=password1Reg.getText().toString();
                final String password2 = password2Reg.getText().toString();


                // Apply Conditions for Correct Input

                if (email.isEmpty()|| name.isEmpty()||password1.isEmpty()||password2.isEmpty()){
                    if(password1 == password2){
                        showMessage("Wrong Input ! \n Field is Empty",1);
                    }else{
                        showMessage("Password is Icorrect ",1);
                    }
                }
                else{
                    
                    // every thing is ok then Create User Account 
                    createUserAccount(name,email,password1);

                }


            }
        });




    }

    private void updateUserInfo(String name, FirebaseUser currentUser) {

        //
        UserProfileChangeRequest userProfileChangeRequest=new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext()," Updated Succesfully ",Toast.LENGTH_LONG).show();
                        homeActivity();
                    }
                });

    }

    private void createUserAccount(final String name, String email, String password1) {

        auth.createUserWithEmailAndPassword(email,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Account IS Created
//                            Toast.makeText(getApplicationContext(), "Account Is Created Successfully", Toast.LENGTH_LONG).show();
                            showMessage("Account Is Created Successfully",0);
                            //
                            updateUserInfo(name,auth.getCurrentUser());


                        }else{
                            showMessage("Account Creation Failed "+ task.getException().getMessage(),1);
                            buttonReg.setVisibility(View.VISIBLE);
                            progressBarReg.setVisibility(View.INVISIBLE);
                        }

                    }
                });
    }

    private void showMessage(String s,int v) {

        // 0 for Toast and 1 for Text
        if(v==0){
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }else if(v==1){
            showMessageReg.setVisibility(View.VISIBLE);
            showMessageReg.setText(s);

        }

    }

    private void homeActivity() {
        Intent homeActivity= new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(homeActivity);

        finish();
    }
}
