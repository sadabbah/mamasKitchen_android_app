package com.example.saeedaldabbah.mamaskitchen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail, mPasswd;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.etEmail);
        mPasswd = findViewById(R.id.etPasswd);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void RegisterNewUser(View view){

        Intent intent = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(intent);
    }
    public void SignInExistingUser(View view){
        attemptLogin();
    }

    private void attemptLogin(){
        String email = mEmail.getText().toString();
        String pass = mPasswd.getText().toString();

        if (email.equals("") || pass.equals("")){
            return;
        }
        Toast.makeText(this, "Login in progress...",Toast.LENGTH_SHORT).show();

        mFirebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, (task -> {
                    Log.d("LoginActivity","signInWithEmail() onComplete: "+task.isSuccessful());
                    if (!task.isSuccessful()){
                        Log.d("LoginActivity", "problem signing in: "+task.getException());
                        showErrorDialog("There was a problem signing in.");
                    }
                    else {
                        Intent intent = new Intent(this, HomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }));
    }
    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Login Attempt error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
