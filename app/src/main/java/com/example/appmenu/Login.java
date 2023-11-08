package com.example.appmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Login extends AppCompatActivity {


    TextView createAccountBtnTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        createAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(Login.this,Account.class)));
    }
}