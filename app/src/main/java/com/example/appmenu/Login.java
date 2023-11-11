package com.example.appmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        loginBtn.setOnClickListener((v)-> loginUser());
        createAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(Login.this,Account.class)));
    }
    void loginUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        boolean isValidated = validateData(email,password);
        if (!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);

    }


    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){

                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Login.this,Opciones.class));
                        finish();
                    }else{
                        Utility.showToast(Login.this, "Correo no verificado, Por favor verifique su correo.");
                    }
                }else{
                    Utility.showToast(Login.this, task.getException().getLocalizedMessage());
                }
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        if (email.trim().isEmpty()) {
            emailEditText.setError("El campo de correo está vacío");
            return false;
        }

        if (password.trim().isEmpty()) {
            passwordEditText.setError("El campo de contraseña está vacío");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email no válido");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Contraseña no válida (mínimo 6 caracteres)");
            return false;
        }

        return true;
    }



}