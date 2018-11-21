package com.example.saeedaldabbah.mamaskitchen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.PrimitiveIterator;

public class RegisterActivity extends AppCompatActivity {

    private static final String CHAT_PREFS ="chat_prefs";
    private static final String DISPLAY_NAME_KEY = "user_name";
    private EditText uName, PassWd, cPasswd, eMail;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uName = findViewById(R.id.etUName);
        eMail = findViewById(R.id.etEmail);
        PassWd = findViewById(R.id.etPasswd);
        cPasswd = findViewById(R.id.etConfirmP);

        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    public void SignUp(View view){
        // reset errors.
        eMail.setError(null);
        PassWd.setError(null);

        // store values at the time of the login attempt.
        String Email = eMail.getText().toString();
        String Password = PassWd.getText().toString();

        boolean calncel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(Password) && !isPasswordValid(Password)){
            PassWd.setError("Password too short or does not match.");
            focusView = PassWd;
            calncel = true;
        }
        if (TextUtils.isEmpty(Email)){
            eMail.setError("This field is required.");
            focusView = eMail;
            calncel = true;
        }
        else if (!isEmailValid(Email)){
            eMail.setError("This email address is invalid.");
            focusView = eMail;
            calncel = true;
        }
        if (calncel){
            focusView.requestFocus();
        }
        else{
            CreateFirebaseUser();
        }
    }

    private boolean isEmailValid(String email){
        return email.contains("@");
    }
    private boolean isPasswordValid(String password){
        String confirmpassword = cPasswd.getText().toString();
        return password.equals(confirmpassword) && password.length() >= 7;
    }
    private void CreateFirebaseUser(){
        String mail = eMail.getText().toString();
        String password =  PassWd.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(this,(task)->{
            if (task.isSuccessful()){
                Toast.makeText(RegisterActivity.this,"Registration is successful", Toast.LENGTH_SHORT).show();
                SaveDisplayName();
                Intent intent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
            else{
                FirebaseAuthException e= (FirebaseAuthException) task.getException();
                showErrorDialog("Failed Registration: "+e.getMessage());
                Log.e("RegisterActivity", "Failed Registration", e);
            }
        });
    }
    private void showErrorDialog(String message){

        new AlertDialog.Builder(this)
                .setTitle("Registration Attempt Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void SaveDisplayName(){
        String displayName = uName.getText().toString();
        SharedPreferences prefs = getSharedPreferences( CHAT_PREFS, MODE_PRIVATE);
    }
}
