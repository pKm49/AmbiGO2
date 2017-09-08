package com.ambigo.ambigo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StreamDownloadTask;

public class SignUP extends AppCompatActivity {

    private EditText mName,mMob,mEmail,mPassword;
    private Button mSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private UserInfo mUserInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(SignUP.this, DriverMainActivity.class));
                }
            }
        };
        mName = (EditText)findViewById(R.id.name);
        mEmail = (EditText)findViewById(R.id.email);
        mMob = (EditText)findViewById(R.id.mob_num);
        mPassword = (EditText) findViewById(R.id.password);


        mSignUp = (Button) findViewById(R.id.sign_up);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String mobile = mMob.getText().toString();
                final String password = mPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignUP.this, "SignUP Failed",Toast.LENGTH_LONG).show();
                        }else{
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_id_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                            current_user_id_db.setValue(true);
                            DatabaseReference current_user_name_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child("Name");
                            current_user_name_db.setValue(name);
                            DatabaseReference current_user_mob_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child("Phone_No");
                            current_user_mob_db.setValue(mobile);
                        }
                    }
                });

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
